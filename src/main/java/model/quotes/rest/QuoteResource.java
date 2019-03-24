package model.quotes.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import model.quotes.rest.dto.Quote;

import java.util.Arrays;
import java.util.List;

@Controller("/quotes")
public class QuoteResource {

    private static final List<Quote> quotes = Arrays.asList(
        new Quote().setId(1L).setText("Don't leave for tomorrow what you can do today"),
        new Quote().setId(2L).setText("At the beginning I was listening but..."),
        new Quote().setId(3L).setText("A man has to do what a man has to do. - Conan the barbarian"),
        new Quote().setId(4L).setText("Leave for tomorrow what you can do today because you might not have to do it at all")
    );

    @Get(value = "{id}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<Quote> getOne(@PathVariable("id") Long id) {

        return quotes
            .stream()
            .filter(quote -> quote.getId().equals(id))
            .findAny()
            .map(HttpResponse::ok)
            .orElse(HttpResponse.notFound());
    }
}
