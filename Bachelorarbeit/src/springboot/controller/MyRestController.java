package springboot.controller;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

import xmlmaker.RoboterWriter;
import model.Roboter;

@RestController
@EnableAutoConfiguration
public class MyRestController {
	@RequestMapping("/")
	String hello() {
		return "Hello!";
	}

	@RequestMapping("/roboter")
	String roboter() throws Exception {
		Roboter x = new Roboter();
		x.setfirma("Makeblock");
		x.setId(1);
		x.setAbstandssensor(true);
		x.setFahrbar(true);
		x.setName("Ranger");
		x.setLinefollower(false);
		RoboterWriter rw = new RoboterWriter();

		return rw.objectToXMLString(x);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(MyRestController.class, args);
	}
}
