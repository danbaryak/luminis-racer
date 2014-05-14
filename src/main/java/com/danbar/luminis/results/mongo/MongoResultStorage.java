package com.danbar.luminis.results.mongo;

import com.danbar.luminis.results.GameResult;
import com.danbar.luminis.results.ResultStorage;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Game result storage implementation for saving & retrieving game results from MongoDB.
 */
public class MongoResultStorage implements ResultStorage {

    /* Mongo collection name to which results will be saved */
    private static final String COL_RESULTS = "results";

    MongoTemplate db;

    public void setDb(MongoTemplate db) {
        this.db = db;
    }

    @Override
    public void storeResult(GameResult results) {
        MongoGameResult dbResult = new MongoGameResult();
        dbResult.setPlayerName(results.getPlayerName());
        dbResult.setFinishTimeInMillis(results.getFinishTimeInMillis());
        db.save(dbResult, COL_RESULTS);
    }


}
