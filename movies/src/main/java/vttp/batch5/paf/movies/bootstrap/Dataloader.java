package vttp.batch5.paf.movies.bootstrap;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.paf.movies.repositories.MongoMovieRepository;
import vttp.batch5.paf.movies.repositories.MySQLMovieRepository;
import static vttp.batch5.paf.movies.utils.Helper.SDF;
import static vttp.batch5.paf.movies.utils.Helper.afterFilterDate;
import vttp.batch5.paf.movies.utils.SqlQuery;

@Component
public class Dataloader {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private MongoTemplate mongoTemplate;

  @Value("${datasource}")
  private String datasource;

  @Autowired
  private DataHandler handler;

  // TODO: Task 2
  public void checkDataLoaded() {
    // Check if SQL database contains data
    SqlRowSet rs = jdbcTemplate.queryForRowSet(SqlQuery.DATALOADED);
    rs.next();
    long sqlSize = rs.getLong("size");

    // Check if Mongo database contains data
    long mongoSize = mongoTemplate.count(new Query(), "imdb");

    if (sqlSize == 0 || mongoSize == 0) {
      batchInsertData();
    }
  }

  public void batchInsertData() {
    try (ZipInputStream in = new ZipInputStream(new BufferedInputStream(new FileInputStream(datasource)))) {
      ZipFile zip = new ZipFile(datasource);
      ZipEntry entry;

      while ((entry = in.getNextEntry()) != null) {
        if (!entry.getName().endsWith(".json")) {
          continue;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(zip.getInputStream(entry)));
        
        String line = reader.readLine();
        
        List<JsonObject> batch = new ArrayList<>();
        while (line != null) {
          String prev = line;
          line = reader.readLine();

          if (batch.size() <= 25 || line == null) {
            JsonReader jsonReader = Json.createReader(new StringReader(prev));
            JsonObject obj = jsonReader.readObject();
            if (afterFilterDate(SDF.parse(obj.getString("release_date")))) {
              batch.add(obj);
            }
            
          } else {
            // Send to repo class for processing
            try {
              handler.handleBatchData(batch);
            } catch (Exception e) {
              System.err.println("Error with batch processing");
              System.err.println(e.getMessage());
              e.printStackTrace();
            }

            // Reset batch
            batch = new ArrayList<>();
            batch.add(Json.createReader(new StringReader(prev)).readObject());
          }
        }
        
      }
      
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }


}
