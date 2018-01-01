package main;

import org.springframework.boot.SpringApplication;

import main.springboot.controller.MyRestController;

public class Applikation {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(MyRestController.class, args);
	}
}
