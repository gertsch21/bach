package main;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Diese Klasse dient rein zum Starten von Spring, was dann automatisch die
 * Restcontroller l�dt
 * 
 * @author Gerhard
 *
 */
@SpringBootApplication(scanBasePackages = { "main.controller" }) // in welchem package befinden sich die
																			// controller
public class Application {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}

	
	/**
	 * gibt mir alle beans aus, welche automatisch oder manuell erzeugt wurden
	 * @param ctx
	 * @return
	 */
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println("Beans von Springboot:");

			String[] alleBeans = ctx.getBeanDefinitionNames();
			Arrays.sort(alleBeans);
			for (String bean : alleBeans) {
				System.out.println(" "+bean);
			}

		};
	}
}
