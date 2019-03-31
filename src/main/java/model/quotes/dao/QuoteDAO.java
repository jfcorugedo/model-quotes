package model.quotes.dao;

import io.reactivex.Maybe;
import io.reactivex.Single;
import model.quotes.rest.dto.Quote;

public interface QuoteDAO {

    public Maybe<Quote> findOne(String id);

    public Single<Quote> insert(Quote quote);
}
