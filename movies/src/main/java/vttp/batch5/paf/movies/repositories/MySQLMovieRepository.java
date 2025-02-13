package vttp.batch5.paf.movies.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.json.JsonObject;
import vttp.batch5.paf.movies.models.Movie;
import vttp.batch5.paf.movies.models.exception.DataLoadException;
import vttp.batch5.paf.movies.utils.SqlQuery;

@Repository
public class MySQLMovieRepository {
  @Autowired
  private JdbcTemplate template;

  // TODO: Task 2.3
  // You can add any number of parameters and return any type from the method
  public void batchInsertMovies(List<JsonObject> batch) {
    try {
      for (JsonObject record : batch) {
        Movie movie = Movie.convertJson(record);
      
        template.update(SqlQuery.INSERT_MOVIE, 
          movie.getImdbId(), movie.getVoteAverage(), movie.getVoteCount(), movie.getReleaseDate(), movie.getRevenue(), 
          movie.getBudget(), movie.getRuntime());
        
      }
    } catch (Exception e) {
      throw new DataLoadException(e.getMessage());
    }
  }

  // TODO: Task 3

}
