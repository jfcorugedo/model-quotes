package model.quotes.dao;

import static com.mongodb.client.model.Filters.eq;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import model.quotes.rest.dto.Quote;


import javax.inject.Singleton;
import java.util.UUID;


@Singleton
@RequiredArgsConstructor
public class QuoteMongoDAO implements QuoteDAO {

    private final MongoClient mongoClient;


    @Override
    public Maybe<Quote> findOne(String id) {

        return Flowable.fromPublisher(
            getQuoteCollection()
                .find(eq("_id", id))
                .limit(1)
        ).firstElement();
    }

    @Override
    public Single<Quote> insert(Quote quote) {

        quote.setId(UUID.randomUUID().toString());
        return Single.fromPublisher(
            getQuoteCollection().insertOne(quote)
        ).map(success -> quote);
    }

    private MongoCollection<Quote> getQuoteCollection() {
        return mongoClient
                .getDatabase("model-quotes")
                .getCollection("quotes", Quote.class);
    }
}
