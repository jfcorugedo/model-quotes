package model.quotes.rest;

import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@MicronautTest
public class QuoteResourceRestAssuredTest {

    @Inject
    private EmbeddedServer embeddedServer;

    @Test
    public void getRandomQuote() {

        given()
            .accept(ContentType.JSON)
        .when()
            .get(
                String.format(
                        "%s://%s:%d/quotes/random",
                        embeddedServer.getScheme(),
                        embeddedServer.getHost(),
                        embeddedServer.getPort()
                )
            )
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("id", equalTo(1))
            .and()
            .body("text", equalTo("Don't leave for tomorrow what you can do today"));
    }
}
