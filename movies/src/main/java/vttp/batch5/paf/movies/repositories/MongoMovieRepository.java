package vttp.batch5.paf.movies.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;
import vttp.batch5.paf.movies.models.ErrorMessage;
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
    public void logError(ErrorMessage err) {
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
    /*
     *  db.imdb.aggregate([
     *      {$match: {
     *          director: {$ne: ''}
     *      }},
     *      {
     *          $group: {
     *              _id: '$director',
     *              movies_count: {$sum: 1},
     *              movie_ids: {$push: '$_id'}
     *          }
     *      }, 
     *      {$sort: {movies_count:-1}},
     *      {$limit: <limit>}
     *  ])
     */
    public List<Document> groupByDirectors(long limit) {
        MatchOperation notEmpty = Aggregation.match(
            Criteria.where("director").ne("")
        );

        GroupOperation groupByDirector = Aggregation.group("director")
            .count().as("movies_count")
            .push("_id").as("movie_ids");
        
        SortOperation sortMovieCount = Aggregation.sort(Sort.Direction.DESC, "movies_count");

        LimitOperation limitOps = Aggregation.limit(limit);

        Aggregation pipeline = Aggregation.newAggregation(notEmpty, groupByDirector, sortMovieCount, limitOps);
        List<Document> results = template.aggregate(pipeline, Helper.COL_IMDB, Document.class).getMappedResults();

        return results;
    }

}
