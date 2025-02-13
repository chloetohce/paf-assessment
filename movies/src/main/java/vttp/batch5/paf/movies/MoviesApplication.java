package vttp.batch5.paf.movies;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import vttp.batch5.paf.movies.bootstrap.Dataloader;

@SpringBootApplication
public class MoviesApplication implements CommandLineRunner {
	@Autowired
	private Dataloader dataloader;

	public static void main(String[] args) {
		System.out.println("Configure datasource using '--load=<file>'.");

		SpringApplication app = new SpringApplication(MoviesApplication.class);

		ApplicationArguments arguments = new DefaultApplicationArguments(args);
		String datasource = "../data/movies_post_2010.zip";
		if (arguments.containsOption("load")) {
			datasource = arguments.getOptionValues("load").getFirst();
		}
		app.setDefaultProperties(Collections.singletonMap("datasource", datasource));
		
		app.run(args);
	}

	@Override
	public void run(String... args) {
		dataloader.checkDataLoaded();
	}
	

}
