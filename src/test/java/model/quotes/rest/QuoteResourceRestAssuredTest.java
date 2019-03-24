package model.quotes.rest;

import com.mongodb.reactivestreams.client.MongoClient;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import io.restassured.http.ContentType;
import model.quotes.reactor.PrintSubscriber;
import model.quotes.rest.dto.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@MicronautTest
public class QuoteResourceRestAssuredTest {

    @Inject
    private MongoClient mongoClient;

    @Inject
    private EmbeddedServer embeddedServer;

    private static final List<Quote> quotes = Arrays.asList(
        new Quote().setId(1L).setText("Don't leave for tomorrow what you can do today"),
        new Quote().setId(2L).setText("At the beginning I was listening but..."),
        new Quote().setId(3L).setText("A man has to do what a man has to do. - Conan the barbarian"),
        new Quote().setId(4L).setText("Leave for tomorrow what you can do today because you might not have to do it at all")
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

        given()
            .accept(ContentType.JSON)
            .when()
            .get(
                String.format(
                    "%s://%s:%d/quotes/2",
                    embeddedServer.getScheme(),
                    embeddedServer.getHost(),
                    embeddedServer.getPort()
                )
            )
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(2))
            .and()
            .body("text", equalTo("At the beginning I was listening but..."));
    }

    @Test
    public void tryToGetQuoteButNotFound() {

        given()
            .accept(ContentType.JSON)
            .when()
            .get(
                String.format(
                    "%s://%s:%d/quotes/456",
                    embeddedServer.getScheme(),
                    embeddedServer.getHost(),
                    embeddedServer.getPort()
                )
            )
            .then()
            .statusCode(404);
    }
}
