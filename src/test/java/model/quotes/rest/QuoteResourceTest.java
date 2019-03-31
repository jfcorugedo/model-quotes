package model.quotes.rest;

import com.mongodb.reactivestreams.client.MongoClient;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import model.quotes.reactor.PrintSubscriber;
import model.quotes.rest.dto.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@MicronautTest
public class QuoteResourceTest {

    @Inject
    @Client("/")
    private RxHttpClient httpClient;

    @Inject
    private MongoClient mongoClient;

    private static final List<Quote> quotes = Arrays.asList(
        new Quote().setId("1").setText("Don't leave for tomorrow what you can do today"),
        new Quote().setId("2").setText("At the beginning I was listening but..."),
        new Quote().setId("3").setText("A man has to do what a man has to do. - Conan the barbarian"),
        new Quote().setId("4").setText("Leave for tomorrow what you can do today because you might not have to do it at all")
    );


    @BeforeEach
    public void cleanAndPopulate() throws InterruptedException {

        PrintSubscriber subscriber = new PrintSubscriber(System.out::println);
        mongoClient
            .getDatabase("model-quotes")
            .getCollection("quotes", Quote.class)
            .drop()
            .subscribe(subscriber);
        subscriber.await();

        subscriber = new PrintSubscriber(System.out::println);
        mongoClient
            .getDatabase("model-quotes")
            .getCollection("quotes", Quote.class)
            .insertMany(quotes)
            .subscribe(subscriber);
        subscriber.await();
    }

    @Test
    public void getOneQuote() {

        Quote randomQuote = httpClient.toBlocking().retrieve(HttpRequest.GET("/quotes/2"), Quote.class);

        assertAll(
            () -> assertThat(randomQuote.getId(), equalTo("2")),
            () -> assertThat(
                randomQuote.getText(),
                allOf(
                    instanceOf(String.class),
                    equalTo("At the beginning I was listening but...")
                )
            )
        );
    }
}
