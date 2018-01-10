package main.springboot.controller;

import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import main.management.RangerManagement;
import main.model.Roboter;
@RestController
@EnableAutoConfiguration
public class ControllerXML {



	
	@RequestMapping("/xml")
    public Roboter greeting() {
		System.out.println("/xml");
        return RangerManagement.getInstance().getCurrentRoboterData();
    }
	

}