package main.springboot.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import main.model.Konfiguration;
import main.model.Usereingaberanger;

@org.springframework.stereotype.Controller
@EnableAutoConfiguration
public class ControllerHTML {

	
	@RequestMapping("/")
	String hello() {
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

		model.addAttribute("konfiguration", konfiguration);
		return "konfiguration";
	}

	
	/**
	 * Diese Methode mappt die einzelnen Formulareintraege vom HTML file einem Konfigurationsobjekt und speichert
	 * dieses anschliessend in einer datei 
	 * @param konfiguration das vom html-Formular bekommene und auf die Klasse Konfiguration gemappte Konfigurationsobjekt
	 * @return die startseite
	 */
	@PostMapping("/konfigurieren")
	public String konfigurationSubmit(@ModelAttribute Konfiguration konfiguration) {

		System.out.println("POST: Konfiguration: Name: " + konfiguration.getName());
		System.out.println("POST: Konfiguration: RobisName: " + konfiguration.getRobotername());

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
		return "start";
	}

	
	@GetMapping("/ueber")
	String ueber() throws Exception {
		return "ueber";
	}
}
