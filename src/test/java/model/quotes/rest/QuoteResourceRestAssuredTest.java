package model.quotes.rest;

import com.mongodb.reactivestreams.client.MongoClient;
import io.micronaut.http.HttpStatus;
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

    private static final String NEW_QUOTE_JSON = "{ \"text\": \"Easy peace!!\" }";
    @Inject
    private MongoClient mongoClient;

    @Inject
    private EmbeddedServer embeddedServer;

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

        given()
            .accept(ContentType.JSON)
            .when()
            .get(
                getQuotesPath() + "2"
            )
            .then()
            .statusCode(HttpStatus.OK.getCode())
            .contentType(ContentType.JSON)
            .body("id", equalTo("2"))
            .and()
            .body("text", equalTo("At the beginning I was listening but..."));
    }

    @Test
    public void tryToGetQuoteButNotFound() {

        given()
            .accept(ContentType.JSON)
        .when()
            .get(
                getQuotesPath() + "456"
            )
        .then()
            .statusCode(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void insertNewQuote() {

        given()
            .contentType(ContentType.JSON)
            .body(NEW_QUOTE_JSON)
            .when()
            .post(
                getQuotesPath()
            )
            .then()
            .statusCode(HttpStatus.CREATED.getCode())
            .body("id", not(isEmptyOrNullString()));

    }

    private String getQuotesPath() {
        return String.format(
            "%s://%s:%d/quotes/",
            embeddedServer.getScheme(),
            embeddedServer.getHost(),
            embeddedServer.getPort()
        );
    }
}
