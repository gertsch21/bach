package main.springboot.controller;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import main.UserEingabeRanger;
import main.management.FileLoader;
import main.management.RangerManagement;
import main.model.Roboter;
import main.xmlmaker.RoboterXMLWriter;

@RestController
@EnableAutoConfiguration
public class MyRestController {

	@RequestMapping("/")
	String hello() {
		try {
			return new FileLoader("src/main/resources/static/html/Startseite.html").getTextFromFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "interner Fehler";
	}

	@GetMapping("/steuern")
	String roboter() throws Exception {
		try {
			return new FileLoader("src/main/resources/static/html/Steuerseite.html").getTextFromFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "interner Fehler";
	}

	@RequestMapping(value = "/steuern", method = RequestMethod.POST)
	String roboterDoSomething(@ModelAttribute("roboterData") UserEingabeRanger formData, BindingResult result)
			throws Exception {
		System.out.println("POST Request auf /roboter/ranger");

		int gesch = 0;
		int dauer = 0;
		try {
			gesch = Integer.valueOf(formData.getGeschwindigkeit());
			dauer = Integer.valueOf(formData.getDauer());
			RangerManagement.getInstance().moveForward(gesch, dauer);
		} catch (NumberFormatException e) {
			System.err.println("MyRequestController:roboterDoSomething: Fehler, da keine Zahl uebergeben wurde");
			e.printStackTrace();
		}

		return roboter();
	}


	@GetMapping("/konfigurieren")
	String konfigurieren() throws Exception {
		try {
			return new FileLoader("src/main/resources/static/html/Konfigurationsseite.html").getTextFromFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "interner Fehler";
	}
	
	
	@GetMapping("/ueber")
	String ueber() throws Exception {
		try {
			return new FileLoader("src/main/resources/static/html/Ueberseite.html").getTextFromFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "interner Fehler";
	}
	

	
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MyRestController.class, args);
	}
}
