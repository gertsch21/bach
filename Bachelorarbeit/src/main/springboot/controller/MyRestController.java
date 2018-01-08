package main.springboot.controller;

import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import main.management.RangerManagement;
@RestController
@EnableAutoConfiguration
public class MyRestController {


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