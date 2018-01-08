package main.springboot.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import main.management.RangerManagement;
import main.model.Konfiguration;
import main.model.Usereingaberanger;

@Controller
@EnableAutoConfiguration
public class SecondController {

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
			in = new ObjectInputStream(new FileInputStream("konfiguration.conf"));
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

	@PostMapping("/konfigurieren")
	public String konfigurationSubmit(@ModelAttribute Konfiguration konfiguration) {

		System.out.println("POST: Konfiguration: Name: " + konfiguration.getName());
		System.out.println("POST: Konfiguration: RobisName: " + konfiguration.getRobotername());

		ObjectOutputStream aus = null;
		try {
			aus = new ObjectOutputStream(new FileOutputStream("konfiguration.conf"));
			aus.writeObject(konfiguration);
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

		return "result";
	}

	@GetMapping("/ueber")
	String ueber() throws Exception {
		return "ueber";
	}

	@PostMapping("/gerade")
	public String gerade(@ModelAttribute Usereingaberanger ue) {
		System.out.println("POST Request auf /gerade");
		int gesch = 0;
		int dauer = 0;

		try {
			gesch = Integer.valueOf(ue.getGeschwindigkeit());
			dauer = Integer.valueOf(ue.getDauer());
			System.out.println("Fahre mit Geschwindigkeit " + gesch + " " + dauer + " Sekunden lang.");
			RangerManagement.getInstance().moveForward(gesch, dauer);
		} catch (NumberFormatException e) {
			System.err.println("MyRequestController:roboterDoSomething: Fehler, da keine Zahl uebergeben wurde");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("ERROR, wahrscheinlich Ranger nicht angeschlossen!");
		}

		return "start";
	}

}
