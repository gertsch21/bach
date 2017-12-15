package springboot.controller;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

import management.FileLoader;
import management.RangerManagement;
import xmlmaker.RoboterWriter;
import model.Roboter;

@RestController
@EnableAutoConfiguration
public class MyRestController {
	
	
	@RequestMapping("/")
	String hello() {
		try {
			return new FileLoader("files/Startseite.html	").getTextFromFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "interner Fehler";
	}

	@GetMapping("/roboter/ranger")
	String roboter() throws Exception {
		return RangerManagement.getInstance().getRoboterDataAsXML();
	}
	@GetMapping("/roboter/ranger/moveForward")
	String moveForward() throws Exception {
		return "Roboter faehrt vorwärts";
	}
	@GetMapping("/roboter/ranger/moveBackward")
	String moveBackward() throws Exception {
		RangerManagement.getInstance().moveBackward(2);
		return "Roboter faehrt zurück";
	}
	@GetMapping("/roboter/ranger/stop")
	String roboterStop() throws Exception {
		RangerManagement.getInstance().stop();
		return "Roboter steht";
	}
	


	public static void main(String[] args) throws Exception {
		SpringApplication.run(MyRestController.class, args);
	}
}
