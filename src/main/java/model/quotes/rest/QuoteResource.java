package model.quotes.rest;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import model.quotes.rest.dto.Quote;

@Controller("/quote/random")
public class QuoteResource {

    @Get(produces = MediaType.APPLICATION_JSON)
    public Quote getRandom() {

        return new Quote().setId(1L).setText("Don't leave for tomorrow what you can do today");
    }
}
