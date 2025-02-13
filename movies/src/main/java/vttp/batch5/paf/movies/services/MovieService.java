package vttp.batch5.paf.movies.services;

import org.bson.Document;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp.batch5.paf.movies.repositories.MongoMovieRepository;
import vttp.batch5.paf.movies.repositories.MySQLMovieRepository;

@Service
public class MovieService {
  @Autowired
  private MongoMovieRepository mongoRepository;

  @Autowired
  private MySQLMovieRepository sqlRepository;

  // TODO: Task 2
  

  // TODO: Task 3
  // You may change the signature of this method by passing any number of parameters
  // and returning any type
  public JsonArray getProlificDirectors(long limit) {
    List<Document> directors = mongoRepository.groupByDirectors(limit);
    JsonArrayBuilder results = Json.createArrayBuilder();
    
    for (Document d : directors) {
      SqlRowSet rs = sqlRepository.getRevenueBudget(d.getList("movie_ids", String.class));
      
      JsonObject record = Json.createObjectBuilder()
        .add("director_name", d.getString("_id"))
        .add("movies_count", d.getInteger("movies_count", 0))
        .add("total_revenue", rs.getDouble("total_revenue"))
        .add("total_budget", rs.getDouble("total_budget"))
        .build();
      results.add(record);
    }
    return results.build();
  }


  // TODO: Task 4
  // You may change the signature of this method by passing any number of parameters
  // and returning any type
  public void generatePDFReport() {

  }

}
