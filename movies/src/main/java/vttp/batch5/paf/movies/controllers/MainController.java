package vttp.batch5.paf.movies.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import vttp.batch5.paf.movies.services.MovieService;


@Controller
@RequestMapping("/api")
public class MainController {
  @Autowired
  private MovieService service;

  // TODO: Task 3
  @GetMapping(path="/summary", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getDirectorsSummary(@RequestParam long count) {
      return ResponseEntity.ok(service.getProlificDirectors(count).toString());
  }
  

  // TODO: Task 4
  @GetMapping(path="/summary/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> generateReport(@RequestParam long count) throws JRException, IOException {
    JasperPrint print = service.generatePDFReport(count);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    JasperExportManager.exportReportToPdfStream(print, out);

    return ResponseEntity.ok()
      .contentType(MediaType.APPLICATION_PDF)
      .header("Content-disposition", "inline; filename=report.pdf")
      .body(out.toByteArray());


  }
  

}
