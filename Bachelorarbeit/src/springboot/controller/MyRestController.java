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
	@RequestMapping("/roboter/ranger/moveForward")
	String moveForward() throws Exception {
		RangerManagement.getInstance().moveForward(2);
		return "Roboter faehrt vorwärts";
	}
	@RequestMapping("/roboter/ranger/moveBackward")
	String moveBackward() throws Exception {
		RangerManagement.getInstance().moveBackward(2);
		return "Roboter faehrt zurück";
	}
	@RequestMapping("/roboter/ranger/stop")
	String roboterStop() throws Exception {
		RangerManagement.getInstance().stop();
		return "Roboter steht";
	}
	


	public static void main(String[] args) throws Exception {
		SpringApplication.run(MyRestController.class, args);
	}
}
