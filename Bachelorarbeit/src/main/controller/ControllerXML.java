package main.controller;


import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

import main.management.RangerManagement;
import main.model.Konfiguration;
import main.model.Roboter;

@RestController
@EnableAutoConfiguration
public class ControllerXML {


	@RequestMapping("/xml")
    public Roboter xml() {
		System.out.println("/xml");
        return RangerManagement.getInstance().getCurrentRoboterData();
    }
	
	@RequestMapping("/xmlSelbstkonfiguration")
    public Konfiguration xmlSelbstkonfiguration() {
		System.out.println("/xmlSelbstkonfiguration");
        return RangerManagement.getInstance().getKonfiguration();
    }

}