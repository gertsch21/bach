package main.controller;

import java.io.IOException;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import main.management.RangerManagement;
import main.model.Usereingaberanger;


/**
 * Diese Klasse enthaelt alle Rest-Endpoints, welche fuer die direkte Steuerung des CPS zustaendig sind.
 * Ein Beispiel waere etwa das Geradeaus-Fahren. 
 *  
 * @author Gerhard
 *
 */
@RestController
@EnableAutoConfiguration
public class ControllerSteuerungen {

		/**
		 * Mit dieser Methode soll sich das CPS nach vor oder zurueck bewegen. 
		 * @param ue die Werte, welche der User in das Formular eingetragen hat. 
		 * @return Der neue ModelAndView(zurueck zur Ausgangsseite)
		 */
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

			return new ModelAndView("redirect:/steuern");
		}
		
		/**
		 * Mit diesem Aufruf soll sich das CPS nach rechts drehen
		 * @return Der neue ModelAndView(zurueck zur Ausgangsseite)
		 * @throws Exception
		 */
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