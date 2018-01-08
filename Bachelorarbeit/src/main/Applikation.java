package main;

import org.springframework.boot.SpringApplication;

import main.springboot.controller.MyRestController;

/**
 * Diese Klasse dient rein zum Starten von Spring, was dann automatisch die Restcontroller lädt
 * @author Gerhard
 *
 */
public class Applikation {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(MyRestController.class, args);
	}
}
