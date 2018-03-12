package main.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import main.management.RangerGetComponentsWithCode;
import main.management.RangerManagement;
import main.model.Komponente;
import main.model.Konfiguration;
import main.model.Usereingaberanger;
import main.model.Vorhandensein;

/**
 * Diese Klasse stellt die wichtigsten Rest-Endpoints zur Verfuegung, jedoch nur die, welche 
 * ein html Dokument zurueckgeben!
 * @author Gerhard
 *
 */
@Controller
@EnableAutoConfiguration
public class ControllerHTML {

	/**
	 * Diese Methode ist fuer die Startseite des CPS zustaendig
	 * @return das Start.html file
	 */
	@RequestMapping({"/","home","start"})
	String start() {
		if(RangerGetComponentsWithCode.getInstance().hatAbstandssensor()) {
			System.out.println("Ultrasonic ist oben");
		}else {
			System.out.println("Ultrasonic ist nicht oben!");
		}
		if(RangerGetComponentsWithCode.getInstance().hatLineFollower()) {
			System.out.println("Linefollower ist oben");
		}else {
			System.out.println("Linefollower ist nicht oben");
		}
		
		return "start";
	}

	/**
	 * Diese Methode ist fuer die Steuerseite des CPS zustaendig
	 * @return das steuer.html file
	 */
	@GetMapping("/steuern")
	String steuernForm(Model model) throws Exception {
		model.addAttribute("usereingabe", new Usereingaberanger());
		return "steuer";
	}

	/**
	 * Diese Methode ist fuer die Konfigurationsseite des CPS zustaendig
	 * @return das konfiguration.html file
	 */
	@GetMapping("/konfigurieren")
	public String konfigurationForm(Model model, HttpServletRequest request) {
		Konfiguration konfiguration = RangerManagement.getInstance().getKonfiguration();
		model.addAttribute("konfiguration", konfiguration);
		return "konfiguration";
	}

	
	/**
	 * Achtung, hier handelt es sich um ein POST-Mapping
	 * Diese Methode mappt die einzelnen Formulareintraege vom HTML file einem Konfigurationsobjekt und speichert
	 * dieses anschliessend in einer datei 
	 * @param konfiguration das vom html-Formular bekommene und auf die Klasse Konfiguration gemappte Konfigurationsobjekt
	 * @return die startseite
	 */
	@PostMapping("/konfigurieren")
	public String konfigurationSubmit(@ModelAttribute Konfiguration konfiguration, HttpServletRequest request) {		
		List<Komponente> konfigurationslisteNeu = new ArrayList<Komponente>();
		
		Konfiguration konfigurationSpeicher = RangerManagement.getInstance().getKonfiguration();
		
		for(Komponente komponente : konfigurationSpeicher.getKomponenten()) {
			if(request.getParameter(komponente.getName())==null || request.getParameter(komponente.getName()).equals("unsi")) {
				komponente.setVorhandensein(Vorhandensein.UNSICHER);
				konfigurationslisteNeu.add(komponente);
			}else if(request.getParameter(komponente.getName()).equals("nich")) {
				komponente.setVorhandensein(Vorhandensein.NICHT_VORHANDEN);
				konfigurationslisteNeu.add(komponente);
			}else if(request.getParameter(komponente.getName()).equals("vorh")) {
				komponente.setVorhandensein(Vorhandensein.VORHANDEN);
				konfigurationslisteNeu.add(komponente);
			}
		}
		
		konfiguration.setKomponenten(konfigurationslisteNeu);

		RangerManagement.getInstance().saveKonfiguration(konfiguration);
		
		return "start";
	}

	/**
	 * Diese Methode ist fuer die Ausgabe aller moeglichen Komponenten zustaendig
	 * @return das alleMoeglichenKomponenten.html file
	 */
	@GetMapping("/alleMoeglichenKomponenten")
	public String alleMoeglichenKomponentenForm(Model model, HttpServletRequest request) {
		Konfiguration konfiguration = RangerManagement.getInstance().getKonfiguration();
		
		if(konfiguration == null) konfiguration = new Konfiguration();
		
		String zuLoeschen = (String) request.getParameter("loeschen");
		
		if(! (zuLoeschen==null || zuLoeschen.length()==0) ) {
			List<Komponente> komponenten_konfiguration = konfiguration.getKomponenten();
			Komponente zuLoeschenKomp = null;
			for(Komponente k : komponenten_konfiguration)
				if(k.getName().equals(zuLoeschen)) {
					zuLoeschenKomp = k;
				}
			if(zuLoeschenKomp!=null) komponenten_konfiguration.remove(zuLoeschenKomp);
			
			konfiguration.setKomponenten(komponenten_konfiguration);
			
			RangerManagement.getInstance().saveKonfiguration(konfiguration);
		}
		
		System.out.println("/alleMoeglichenKomponenten: Konfiguration: "+konfiguration);
		model.addAttribute("konfiguration", konfiguration);
		return "alleMoeglichenKomponenten";
	}

	/**
	 * Achtung POST Mapping
	 * @param request ein Request Objekt, um einen Zugriff auf die Request Eigenschaften zu bekommen
	 * @return 
	 */
	@PostMapping("/alleMoeglichenKomponenten")
	public ModelAndView alleMoeglichenKomponentenSubmit(HttpServletRequest request) {
		
		String neueKomponenteName = request.getParameter("neueKomponente");
		if(neueKomponenteName==null || neueKomponenteName.length()==0) return new ModelAndView( "redirect:/alleMoeglichenKomponenten");
		
		Komponente neueKomponente = new Komponente(neueKomponenteName.replace(" ", ""),Vorhandensein.UNSICHER);
		
		Konfiguration konfiguration = RangerManagement.getInstance().getKonfiguration();
		
		System.out.println("POST: alleMoeglichenKomponentenSubmit: Neue Komponente wird registriert: " + neueKomponente);
		
		List<Komponente> konfigurationsliste = konfiguration.getKomponenten();
		
		for(Komponente k : konfigurationsliste)
			if(k.getName().equals(neueKomponente.getName())) 
				return new ModelAndView("redirect:/alleMoeglichenKomponenten");
		
		konfigurationsliste.add(neueKomponente);
		konfiguration.setKomponenten(konfigurationsliste);
		
		RangerManagement.getInstance().saveKonfiguration(konfiguration);
		
		return new ModelAndView("redirect:/alleMoeglichenKomponenten");
	}

	/**
	 * Diese Methode ist fuer die Informationsseite des gesamten Projektes zustaendig. 
	 * Es enthaelt wichtige Informationen ueber den Code.
	 * @return das ueber.html file
	 */
	@GetMapping("/ueber")
	String ueber() throws Exception {
		return "ueber";
	}
}
