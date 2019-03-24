package model.quotes.dao;

import io.reactivex.Maybe;
import model.quotes.rest.dto.Quote;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

@Singleton
public class QuoteMongoDAO implements QuoteDAO {

    private static final List<Quote> quotes = Arrays.asList(
        new Quote().setId(1L).setText("Don't leave for tomorrow what you can do today"),
        new Quote().setId(2L).setText("At the beginning I was listening but..."),
        new Quote().setId(3L).setText("A man has to do what a man has to do. - Conan the barbarian"),
        new Quote().setId(4L).setText("Leave for tomorrow what you can do today because you might not have to do it at all")
    );


    @Override
    public Maybe<Quote> findOne(Long id) {
        return quotes
            .stream()
            .filter(quote -> quote.getId().equals(id))
            .map(Maybe::just)
            .findAny()
            .orElse(Maybe.empty());
    }
}
