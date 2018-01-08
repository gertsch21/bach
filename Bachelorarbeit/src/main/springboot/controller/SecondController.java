package main.springboot.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import main.model.Konfiguration;


@Controller
@EnableAutoConfiguration
public class SecondController {

	@RequestMapping("/")
	String hello() {
		return "start";
	}

	@GetMapping("/steuern")
	String roboter() throws Exception {
		System.out.println("GET Request auf /steuern");
		return "steuer";
	}

    @GetMapping("/konfigurieren")
    public String konfigurationForm(Model model) {
        model.addAttribute("konfiguration", new Konfiguration());
        return "konfiguration";
    }

    @PostMapping("/konfigurieren")
    public String konfigurationSubmit(@ModelAttribute Konfiguration konfiguration) {
    	
    	System.out.println("POST: Konfiguration: Name: "+konfiguration.getName());
    	System.out.println("POST: Konfiguration: RobisName: "+konfiguration.getRobotername());
        return "result";
    }
	
	@GetMapping("/ueber")
	String ueber() throws Exception {
		return "ueber";
	}
}
