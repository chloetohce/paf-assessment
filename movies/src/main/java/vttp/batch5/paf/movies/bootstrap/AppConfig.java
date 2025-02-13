package vttp.batch5.paf.movies.bootstrap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${datasource}")
    private String datasource;

    @Value("${report.name}")
    private String reportName;

    @Value("${report.batch}")
    private String reportBatch;

    @Bean("datasource")
    public String datasource() {
        System.out.println(datasource);
        return datasource;
    }

    @Bean("reportName")
    public String reportName() {
        return reportName;
    }

    @Bean("reportBatch")
    public String reportBatch() {
        return reportBatch;
    }
}
