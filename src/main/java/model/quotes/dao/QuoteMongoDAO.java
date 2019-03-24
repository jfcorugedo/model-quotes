package model.quotes.dao;

import io.reactivex.Maybe;
import model.quotes.rest.dto.Quote;

import javax.inject.Singleton;

@Singleton
public class QuoteMongoDAO implements QuoteDAO {

    @Override
    public Maybe<Quote> findOne(Long id) {
        return Maybe.empty();
    }

}
