package springboot.controller;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

import management.RangerManagement;
import xmlmaker.RoboterWriter;
import model.Roboter;

@RestController
@EnableAutoConfiguration
public class MyRestController {
	@RequestMapping("/")
	String hello() {
		return "Hello!";
	}

	@RequestMapping("/roboter/ranger")
	String roboter() throws Exception {
		return RangerManagement.getInstance().getRoboterDataAsXML();
	}
	@RequestMapping("/roboter/ranger/move")
	String roboterMove() throws Exception {
		RangerManagement.getInstance().testMove();
		return "Roboter faehrt vorwaerts";
	}
	@RequestMapping("/roboter/ranger/stop")
	String roboterStop() throws Exception {
		
		return "Roboter steht";
	}
	


	public static void main(String[] args) throws Exception {
		SpringApplication.run(MyRestController.class, args);
	}
}
