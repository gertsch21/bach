package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Diese Klasse dient rein zum Starten von Spring, was dann automatisch die Restcontroller l�dt
 * @author Gerhard
 *
 */
@SpringBootApplication(scanBasePackages={"main.springboot.controller"}) //in welchem package befinden sich die controller
public class Application {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
}
