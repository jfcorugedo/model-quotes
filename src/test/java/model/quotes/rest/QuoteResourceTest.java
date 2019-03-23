package model.quotes.rest;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import model.quotes.rest.dto.Quote;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import javax.inject.Inject;

@MicronautTest
public class QuoteResourceTest {

    @Inject
    @Client("/")
    private RxHttpClient httpClient;

    @Test
    public void getRandomQuote() {

        Quote randomQuote = httpClient.toBlocking().retrieve(HttpRequest.GET("/quotes/random"), Quote.class);

        assertAll(
                () -> assertThat( randomQuote.getId().longValue(), greaterThanOrEqualTo(0L)),
                () -> assertThat(
                        randomQuote.getText(),
                        allOf(
                                instanceOf(String.class),
                                not(isEmptyString())
                        )
                )
        );
    }
}
