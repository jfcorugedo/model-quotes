package model.quotes.rest;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import model.quotes.rest.dto.Quote;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@MicronautTest
public class QuoteResourceTest {

    @Inject
    @Client("/")
    private RxHttpClient httpClient;

    @Test
    public void getOneQuote() {

        Quote randomQuote = httpClient.toBlocking().retrieve(HttpRequest.GET("/quotes/2"), Quote.class);

        assertAll(
            () -> assertThat(randomQuote.getId().longValue(), equalTo(2L)),
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
