package vttp.batch5.paf.movies.repositories;

import java.util.Collections;
import java.util.List;

import javax.sql.RowSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.json.JsonObject;
import vttp.batch5.paf.movies.models.Movie;
import vttp.batch5.paf.movies.models.exception.DataLoadException;
import vttp.batch5.paf.movies.utils.Helper;
import vttp.batch5.paf.movies.utils.SqlQuery;

@Repository
public class MySQLMovieRepository {
  @Autowired
  private JdbcTemplate template;

  @Autowired
  private NamedParameterJdbcTemplate npjTemplate;

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
  public SqlRowSet getRevenueBudget(List<String> ids) {
    // Using namedparameterjdbctemplate to allow creating prepared statment with dynamic in clause
    SqlRowSet rs = npjTemplate.queryForRowSet(SqlQuery.REVENUE_BUDGET, 
      Collections.singletonMap("listIds", ids));
    rs.next();
    return rs;
  }
}
