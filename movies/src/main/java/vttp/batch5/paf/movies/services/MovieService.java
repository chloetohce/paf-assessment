package vttp.batch5.paf.movies.services;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.json.data.JsonDataSource;
import vttp.batch5.paf.movies.repositories.MongoMovieRepository;
import vttp.batch5.paf.movies.repositories.MySQLMovieRepository;

@Service
public class MovieService {
  @Autowired
  private MongoMovieRepository mongoRepository;

  @Autowired
  private MySQLMovieRepository sqlRepository;

  @Autowired
  @Qualifier("reportName")
  private String reportName;

  @Autowired
  @Qualifier("reportBatch")
  private String reportBatch;
  

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
  public JasperPrint generatePDFReport(long limit) throws JRException, FileNotFoundException {

    // TODO remove hardcoding
    JsonObject reportDetails = Json.createObjectBuilder()
      .add("name", reportName)
      .add("batch", reportBatch)
      .build();
    JsonArray dataset = getDataForReport(limit);

    JsonDataSource reportDS = new JsonDataSource(new ByteArrayInputStream(reportDetails.toString().getBytes()));
    JsonDataSource directorsDS = new JsonDataSource(new ByteArrayInputStream(dataset.toString().getBytes()));

    Map<String, Object> params = new HashMap<>();
    params.put("DIRECTOR_TABLE_DATASET", directorsDS);

    InputStream is = new FileInputStream("app/data/director_movies_report.jrxml");
    JasperReport report = JasperCompileManager.compileReport(is);

    JasperPrint print = JasperFillManager.fillReport(report, params, reportDS);

    // JRPdfExporter exporter = new JRPdfExporter();
    // exporter.setExporterInput(new SimpleExporterInput(print));
    // exporter.setExporterOutput(new SimpleOutputStreamExporterOutput("report.pdf"));

    return print;

  }

  private JsonArray getDataForReport(long limit) {
    List<Document> directors = mongoRepository.groupByDirectors(limit);
    JsonArrayBuilder results = Json.createArrayBuilder();
    
    for (Document d : directors) {
      SqlRowSet rs = sqlRepository.getRevenueBudget(d.getList("movie_ids", String.class));
      
      JsonObject record = Json.createObjectBuilder()
        .add("director", d.getString("_id"))
        .add("count", d.getInteger("movies_count", 0))
        .add("revenue", rs.getDouble("total_revenue"))
        .add("budget", rs.getDouble("total_budget"))
        .build();
      results.add(record);
    }
    return results.build();
  }

}
