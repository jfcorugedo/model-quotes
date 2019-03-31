package model.quotes.dao;

import io.reactivex.Maybe;
import model.quotes.rest.dto.Quote;

public interface QuoteDAO {

    public Maybe<Quote> findOne(String id);
}
