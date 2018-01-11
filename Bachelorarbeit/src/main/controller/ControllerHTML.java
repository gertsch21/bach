package main.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
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

import main.management.RangerManagement;
import main.model.Komponente;
import main.model.Komponentenauflistung;
import main.model.Konfiguration;
import main.model.Usereingaberanger;
import main.model.Vorhandensein;

@Controller
@EnableAutoConfiguration
public class ControllerHTML {

	
	@RequestMapping({"/","home","start"})
	String start() {
		return "start";
	}

	
	@GetMapping("/steuern")
	String steuernForm(Model model) throws Exception {
		model.addAttribute("usereingabe", new Usereingaberanger());
		return "steuer";
	}

	
	@GetMapping("/konfigurieren")
	public String konfigurationForm(Model model) {
		Konfiguration konfiguration = null;
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream("selbstkonfiguration.conf"));
			konfiguration = (Konfiguration) in.readObject();
			
			
		} catch (FileNotFoundException ex) {
			System.out.println("Speichersdatei (noch) nicht vorhanden!");
		} catch (Exception ex) {
			System.out.println(ex);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
			}
		}
		if (konfiguration == null)
			konfiguration = new Konfiguration();
		
		
		Komponentenauflistung kompAuflistung = RangerManagement.getInstance().getKomponentenauflistung();
		
		
		
		model.addAttribute("konfiguration", konfiguration);
		model.addAttribute("kompauflistung",kompAuflistung);
		return "konfiguration";
	}

	
	/**
	 * Diese Methode mappt die einzelnen Formulareintraege vom HTML file einem Konfigurationsobjekt und speichert
	 * dieses anschliessend in einer datei 
	 * @param konfiguration das vom html-Formular bekommene und auf die Klasse Konfiguration gemappte Konfigurationsobjekt
	 * @return die startseite
	 */
	@PostMapping("/konfigurieren")
	public String konfigurationSubmit(@ModelAttribute Konfiguration konfiguration,@ModelAttribute(value = "priorities") HashMap<String, String> priorities
			,HttpServletRequest request) {

		System.out.println("POST: Konfiguration: Name: " + konfiguration.getName());
		System.out.println("POST: Konfiguration: RobisName: " + konfiguration.getRobotername());

		
		List<Komponente> aktKomponentenliste = new ArrayList<>();
		
		Komponentenauflistung kompAuflistung = RangerManagement.getInstance().getKomponentenauflistung();
		for(String komponente : kompAuflistung.getAlleKomponenten()) {
			if(request.getParameter(komponente)==null || request.getParameter(komponente).equals("unsi")) 
				aktKomponentenliste.add(new Komponente(komponente, Vorhandensein.UNSICHER));
			else if(request.getParameter(komponente).equals("nich"))
				aktKomponentenliste.add(new Komponente(komponente, Vorhandensein.NICHT_VORHANDEN));
			else if(request.getParameter(komponente).equals("vorh"))
				aktKomponentenliste.add(new Komponente(komponente, Vorhandensein.VORHANDEN));
		}
		
		konfiguration.setListe(aktKomponentenliste);
		
		System.out.println(konfiguration);
		
		
		ObjectOutputStream aus = null;
		try {
			aus = new ObjectOutputStream(new FileOutputStream("selbstkonfiguration.conf"));
			aus.writeObject(konfiguration); //das automatisch von html gemappte
		} catch (IOException ex) {
			System.out.println(ex);
		} finally {
			try {
				if (aus != null) {
					aus.flush();
					aus.close();
				}
			} catch (IOException e) {
			}
		}
	
		
		System.out.println(RangerManagement.getInstance().getKonfiguration());
		
		return "start";
	}

	

	@GetMapping("/alleMoeglichenKomponenten")
	public String alleMoeglichenKomponentenForm(Model model,HttpServletRequest request) {
		Komponentenauflistung kompAuflistung=RangerManagement.getInstance().getKomponentenauflistung();
		if(kompAuflistung == null) kompAuflistung = new Komponentenauflistung();
		List<String> alle_komponenten = kompAuflistung.getAlleKomponenten();
		
		String zuLoeschen = (String) request.getParameter("loeschen");
		
		if(! (zuLoeschen==null || zuLoeschen.length()==0) ) {
			alle_komponenten.remove(zuLoeschen);
			kompAuflistung.setAlleKomponenten(alle_komponenten);
			RangerManagement.getInstance().saveKomponentenauflistung(kompAuflistung);
		}
		
		model.addAttribute("alle_komponenten", alle_komponenten);
		return "alleMoeglichenKomponenten";
	}

	
	@PostMapping("/alleMoeglichenKomponenten")
	public ModelAndView alleMoeglichenKomponentenSubmit(HttpServletRequest request) {
		request.setAttribute("fehler", "");
		String neueKomponente = request.getParameter("neueKomponente");
		
		if(neueKomponente==null || neueKomponente.length()==0) return new ModelAndView( "redirect:/alleMoeglichenKomponenten");
		
		Komponentenauflistung kompAuflistung = RangerManagement.getInstance().getKomponentenauflistung();
		System.out.println("POST: alleMoeglichenKomponentenSubmit: Neue Komponente registriert: " + neueKomponente);
		
		List<String> neueListe = kompAuflistung.getAlleKomponenten();
		
		if(neueListe.contains(neueKomponente)) {
			return new ModelAndView( "redirect:/alleMoeglichenKomponenten");
		}
		
		neueListe.add(neueKomponente.replace(" ", ""));
		java.util.Collections.sort(neueListe);
		kompAuflistung.setAlleKomponenten(neueListe);
		
		RangerManagement.getInstance().saveKomponentenauflistung(kompAuflistung);
		
		return new ModelAndView("redirect:/alleMoeglichenKomponenten");
	}
	
	
	@GetMapping("/ueber")
	String ueber() throws Exception {
		return "ueber";
	}
}
