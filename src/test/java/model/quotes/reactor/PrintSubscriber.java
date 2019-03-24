package model.quotes.reactor;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class PrintSubscriber implements Subscriber {

    private Consumer<String> logger;

    CountDownLatch latch = new CountDownLatch(1);

    public PrintSubscriber(Consumer<String> logger) {
        this.logger = logger;
    }
    @Override
    public void onSubscribe(Subscription s) {
        logger.accept("Requesting results...");
        s.request(1);

    }

    @Override
    public void onNext(Object o) {
        logger.accept(String.format("Finish: %s", o.toString()));
    }

    @Override
    public void onError(Throwable t) {
        logger.accept(String.format("Error: %s", t.getMessage()));
        t.printStackTrace();
    }

    @Override
    public void onComplete() {
        logger.accept("Completed!");
        latch.countDown();
    }

    public void await() {
        try {
            latch.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("Timeout waiting for onComplete!");
            e.printStackTrace();
        }
    }
}
