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
	
	
	@PostMapping("/rechtsDrehen")
	ModelAndView rechtsDrehen() throws Exception {
		try {
			RangerManagement.getInstance().turnRight();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView( "redirect:/steuern"); //nur um auf die get seite weitergeleitet zu werden
	}
	

}