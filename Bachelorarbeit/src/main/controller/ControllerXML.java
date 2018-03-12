package main.controller;


import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

import main.management.RangerManagement;
import main.model.Konfiguration;
import main.model.Roboter;

/**
 * Diese Klasse enthaelt alle Rest-Endpoints, welche XML-Inhalte zurueck geben. 
 * @author Gerhard
 *
 */
@RestController
@EnableAutoConfiguration
public class ControllerXML {

	/**
	 * Diese Methode ist fuer die Abfrage der CPS-Daten zustaendig.(Selbstidentifikation)
	 * @return die aktuell-gespeicherten Daten des CPS als XML-formatiert 
	 */
	@RequestMapping("/xml")
    public Roboter xml() {
		System.out.println("/xml");
        return RangerManagement.getInstance().getCurrentRoboterData();
    }
	
	/**
	 * Mit diser Methode wird nur die manuelle Konfiguration beruecksichtigt. 
	 * Dabei wird nicht auf die Eigenerkennung des CPS zurueckgegriffen. 
	 * @return die aktuell-gespeicherten Daten des CPS(jedoch nur die manuellen Eingaben) als XML-formatiert
	 */
	@RequestMapping("/xmlSelbstkonfiguration")
    public Konfiguration xmlSelbstkonfiguration() {
		System.out.println("/xmlSelbstkonfiguration");
        return RangerManagement.getInstance().getKonfiguration();
    }

}