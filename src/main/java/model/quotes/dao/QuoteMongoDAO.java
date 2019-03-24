package model.quotes.dao;

import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import lombok.RequiredArgsConstructor;
import model.quotes.rest.dto.Quote;

import javax.inject.Singleton;


@Singleton
@RequiredArgsConstructor
public class QuoteMongoDAO implements QuoteDAO {

    private final MongoClient mongoClient;


    @Override
    public Maybe<Quote> findOne(Long id) {

        return Flowable.fromPublisher(
            getQuoteCollection()
                .find(Filters.eq("id", id))
                .limit(1)
        ).firstElement();
    }

    private MongoCollection<Quote> getQuoteCollection() {
        return mongoClient
                .getDatabase("model-quotes")
                .getCollection("quotes", Quote.class);
    }
}
