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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import main.management.RangerManagement;
import main.model.Komponente;
import main.model.Komponentenauflistung;
import main.model.Konfiguration;
import main.model.Usereingaberanger;
import main.model.Vorhandensein;

@org.springframework.stereotype.Controller
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
		
		
		List<Komponente> liste = new ArrayList<Komponente>();
		
		liste.add(new Komponente("Ultrasonic", Vorhandensein.forName("VORHANDEN")));
		liste.add(new Komponente("LineFollower", Vorhandensein.forName("unsicher")));
		liste.add(new Komponente("Temp", Vorhandensein.forName("nicht_VORHANDEN")));
		
		konfiguration.setListe(liste);
		
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

		System.out.println(request.getAttribute("priorities[Ultrasonic]a"));
		for (String s : priorities.keySet()) {
	        System.out.println(s);
	    }
		
		
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
		
		String ultrasonic = request.getParameter("ultrasonic");
		if (ultrasonic.equals("vorh")) {
			System.out.println("Ultrasonic vorhanden");
		}else if(ultrasonic.equals("nich")) {
			System.out.println("Ultrasonic nicht vorhanden");
		}else if(ultrasonic.equals("unsi")) {
			System.out.println("Ultrasonic unsicher");
		}
		
		return "start";
	}

	

	@GetMapping("/alleMoeglichenKomponenten")
	public String alleMoeglichenKomponentenForm(Model model) {
		Komponentenauflistung kompAuflistung=RangerManagement.getInstance().getKomponentenauflistung();
		if(kompAuflistung == null) kompAuflistung = new Komponentenauflistung();
		List<String> alle_komponenten = kompAuflistung.getAlleKomponenten();
		
		model.addAttribute("alle_komponenten", alle_komponenten);
		return "alleMoeglichenKomponenten";
	}

	
	@PostMapping("/alleMoeglichenKomponenten")
	public String alleMoeglichenKomponentenSubmit(HttpServletRequest request) {

		String neueKomponente = request.getParameter("neueKomponente");
		
		if(neueKomponente==null || neueKomponente.length()==0) return "start";
		
		Komponentenauflistung kompAuflistung = RangerManagement.getInstance().getKomponentenauflistung();
		
		System.out.println("POST: alleMoeglichenKomponentenSubmit: Name: " + neueKomponente);
		
		List<String> neueListe = kompAuflistung.getAlleKomponenten();
		neueListe.add(neueKomponente);
		kompAuflistung.setAlleKomponenten(neueListe);
		
		RangerManagement.getInstance().saveKomponentenauflistung(kompAuflistung);
		
		return "start";
	}
	
	
	@GetMapping("/ueber")
	String ueber() throws Exception {
		return "ueber";
	}
}
