package vttp.batch5.paf.movies.bootstrap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.json.JsonObject;
import vttp.batch5.paf.movies.repositories.MongoMovieRepository;
import vttp.batch5.paf.movies.repositories.MySQLMovieRepository;

@Component
public class DataHandler {
    @Autowired
    private MySQLMovieRepository sqlRepository;

    @Autowired
    private MongoMovieRepository mongoRepository;

    @Transactional
    public void handleBatchData(List<JsonObject> data) {
        sqlRepository.batchInsertMovies(data);
        mongoRepository.batchInsertMovies(data);
    }
}
