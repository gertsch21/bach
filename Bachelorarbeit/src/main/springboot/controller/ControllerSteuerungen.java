package main.springboot.controller;

import java.io.IOException;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import main.management.RangerManagement;
import main.model.Roboter;
import main.model.Usereingaberanger;


@RestController
@EnableAutoConfiguration
public class ControllerSteuerungen {

		
		@PostMapping("/gerade")
		public ModelAndView gerade(@ModelAttribute Usereingaberanger ue) {
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

			return new ModelAndView( "redirect:/steuern");
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

		
		@RequestMapping("/xml")
	    public Roboter greeting() {
			System.out.println("/xml");
	        return RangerManagement.getInstance().getCurrentRoboterData();
	    }
		

	}