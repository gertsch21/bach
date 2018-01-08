package main.springboot.controller;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import main.UserEingabeRanger;
import main.management.FileLoader;
import main.management.RangerManagement;
import main.model.Konfiguration;
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
		System.out.println("GET Request auf /steuern");

		try {
			return new FileLoader("src/main/resources/static/html/Steuerseite.html").getTextFromFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "interner Fehler";
	}

	@RequestMapping(value = "/gerade", method = RequestMethod.POST)
	public ModelAndView gerade(@ModelAttribute("userFormData") UserEingabeRanger formData, BindingResult result) throws Exception{
		System.out.println("POST Request auf /gerade");

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
		
		return new ModelAndView( "redirect:/steuern"); //nur um auf die get seite weitergeleitet zu werden
	}
	

	
	//das model brauch ich nur zum testen fuer freemarker, weiss aber noch nicht genau wie es geht
	@GetMapping("/konfigurieren")
	String konfigurieren(Model model) {
		Konfiguration k = new Konfiguration();
		k.setName("Geil Mit Controller gesettet");
		k.setRobotername("Geil, auch Mit Controller gesettet");
		
		model.addAttribute("konfiguration",k);
		try {
			return new FileLoader("src/main/resources/static/html/Konfigurationsseite.html").getTextFromFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "interner Fehler";
	}
	
	@PostMapping("/konfigurieren")
	String konfigurierenPost(@ModelAttribute Konfiguration konfiguration) {
		System.out.println("POST: Name: "+konfiguration.getName());
		System.out.println("POST: Robotername: "+konfiguration.getRobotername());
		
		return "POST: /konfigurieren";
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
	
	@PostMapping("/rechtsDrehen")
	ModelAndView rechtsDrehen() throws Exception {
		try {
			RangerManagement.getInstance().turnRight();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView( "redirect:/steuern"); //nur um auf die get seite weitergeleitet zu werden
	}
	
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(MyRestController.class, args);
	}
}
