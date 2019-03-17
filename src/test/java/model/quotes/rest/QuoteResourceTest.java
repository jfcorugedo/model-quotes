package model.quotes.rest;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import model.quotes.rest.dto.Quote;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
                () -> assertEquals(1L, randomQuote.getId().longValue()),
                () -> assertEquals("Don't leave for tomorrow what you can do today", randomQuote.getText())
        );
    }
}
