package model.quotes.rest;

import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@MicronautTest
public class QuoteResourceRestAssuredTest {

    @Inject
    private EmbeddedServer embeddedServer;

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
