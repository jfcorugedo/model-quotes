package model.quotes.rest;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.*;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import model.quotes.dao.QuoteDAO;
import model.quotes.rest.dto.Quote;


@Controller("/quotes")
@RequiredArgsConstructor
public class QuoteResource {

    private final QuoteDAO quoteDAO;

    @Get(value = "{id}", produces = MediaType.APPLICATION_JSON)
    public Single<MutableHttpResponse<Quote>> getOne(@PathVariable("id") String id) {

        return quoteDAO
            .findOne(id)
            .map(HttpResponse::ok)
            .toSingle(HttpResponse.notFound())
            .onErrorReturn(error -> HttpResponse.serverError());
    }

    @Post
    public Single<MutableHttpResponse<Quote>> insertOne(@Body Quote quote) {

        return quoteDAO
            .insert(quote)
            .map(insertedQuote -> HttpResponse.created(insertedQuote))
            .onErrorReturn(error -> HttpResponse.serverError());
    }
}
