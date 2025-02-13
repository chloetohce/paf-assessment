package vttp.batch5.paf.movies.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;
import vttp.batch5.paf.movies.models.Error;
import vttp.batch5.paf.movies.utils.Helper;

@Repository
public class MongoMovieRepository {
    @Autowired
    private MongoTemplate template;

    // TODO: Task 2.3
    // You can add any number of parameters and return any type from the method
    // You can throw any checked exceptions from the method
    // Write the native Mongo query you implement in the method in the comments
    //
    // native MongoDB query here
    //
    /*
     *  db.imdb.insertMany({
     *      {
     *          _id: <imdb_id>,
     *          title: <title>,
     *          director: <directors>,
     *          overview: <overview>,
     *          tagline: <tagline>,
     *          genres: <genres>,
     *          imdb_rating: <rating>
     *          imdb_votes: <votes>
     *      },
     *      ...
     *  })
     */
    public void batchInsertMovies(List<JsonObject> batch) {
        List<Document> docsToInsert = batch.stream()
            .map(obj -> {
                return new Document()
                    .append("_id", obj.getString("imdb_id"))
                    .append("title", obj.getString("title", ""))
                    .append("director", obj.getString("director", ""))
                    .append("overview", obj.getString("overview", ""))
                    .append("tagline", obj.getString("tagline", ""))
                    .append("genres", obj.getString("genres", ""))
                    .append("imdb_rating", obj.getInt("imdb_rating", 0))
                    .append("imdb_votes", obj.getInt("imdb_votes", 0));
            })
            .toList();
        template.insert(docsToInsert, Helper.COL_IMDB);
    }

    // TODO: Task 2.4
    // You can add any number of parameters and return any type from the method
    // You can throw any checked exceptions from the method
    // Write the native Mongo query you implement in the method in the comments
    //
    // native MongoDB query here
    //
    /*
     *  db.errors.insertOne(
     *      {
     *          imdb_ids: <ids>,
     *          error: <arr of errors>,
     *          timestamp: <timestamp>
     *      }
     *  )
     */
    public void logError(Error err) {
        Document toInsert = new Document()
            .append("imdb_ids", err.getIds())
            .append("error", err.getError())
            .append("timestamp", err.getDate());
        template.insert(toInsert, Helper.COL_ERRORS);

    }

    // TODO: Task 3
    // Write the native Mongo query you implement in the method in the comments
    //
    // native MongoDB query here
    //

}
