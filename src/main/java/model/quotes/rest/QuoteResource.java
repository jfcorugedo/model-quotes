package model.quotes.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import model.quotes.dao.QuoteDAO;
import model.quotes.rest.dto.Quote;


@Controller("/quotes")
@RequiredArgsConstructor
public class QuoteResource {

    private final QuoteDAO quoteDAO;

    @Get(value = "{id}", produces = MediaType.APPLICATION_JSON)
    public Single<MutableHttpResponse<Quote>> getOne(@PathVariable("id") Long id) {

        return quoteDAO
            .findOne(id)
            .map(HttpResponse::ok)
            .toSingle(HttpResponse.notFound());
    }
}
