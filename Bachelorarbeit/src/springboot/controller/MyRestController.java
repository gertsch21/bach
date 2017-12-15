package springboot.controller;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import management.FileLoader;
import management.RangerManagement;
import xmlmaker.RoboterWriter;
import model.Roboter;
import test.UserEingabeRanger;

@RestController
@EnableAutoConfiguration
public class MyRestController {
	
	
	@RequestMapping("/")
	String hello() {
		try {
			return new FileLoader("files/Startseite.html").getTextFromFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "interner Fehler";
	}

	@GetMapping("/roboter/ranger")
	String roboter() throws Exception {
		try {
			return new FileLoader("files/RangerSteuerseite.html").getTextFromFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "interner Fehler";
	}
	
	
	@RequestMapping(value = "/roboter/ranger", method = RequestMethod.POST)
	String roboterDoSomething(@ModelAttribute("roboterData") UserEingabeRanger formData, BindingResult result) throws Exception{
		System.out.println("POST /roboter/ranger");
		System.out.println("Dauer: " + formData.getDauer());
		
		return roboter();
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
