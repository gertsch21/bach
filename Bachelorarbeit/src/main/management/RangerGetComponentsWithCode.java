package main.management;

import java.io.IOException;

import main.mbot.client.MbotClient;

/**
 * In dieser Klasse sind alle Methoden, welche sich mit dem Holen der einzelnen Komponenten beschaeftigt. 
 * Es wird mit diversen Algorithmen versucht SELBST ZU ERKENNEN ob die einzelnen Komponenten angeschlossen sind.
 * 
 * @author Gerhard
 */
public class RangerGetComponentsWithCode {

	private static RangerGetComponentsWithCode instance;
	
	private RangerGetComponentsWithCode() {
		System.out.println("RangerGetComponentsWithCode:Defaultkonstruktor");
	}

	/**
	 * Singleton
	 * @return RangerGetComponentsWithCode-Objekt
	 */
	public static RangerGetComponentsWithCode getInstance() {
		if (instance == null)
			instance = new RangerGetComponentsWithCode();
		return instance;
	}


	
	/**
	 * Diese Methode prueft ob der Ranger fahrbar ist. Indem er eine Geschwindigkeit einstellt bei den zwei
	 * Motoren. Falls bei diesem Vorgang ein Fehler auftritt, dann ist er nicht fahrbar.  
	 * @return true, falls fahrbar, sonst false
	 */
	public boolean istFahrbar() {

		try {
			RangerManagement.getInstance().getMc().encoderMotorLeft(0);
			RangerManagement.getInstance().getMc().encoderMotorRight(0);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Diese Methode prueft ob der Ranger einen Abstandsmesser hat und dieser korrekte Werte liefert. 
	 * @return true, falls vorhanden, sonst false
	 */
	public boolean hatAbstandssensor() {
		MbotClient mc =  RangerManagement.getInstance().getMc();
		boolean vorhanden = false;
		for(int i = 1; i<11; i++) {
			try {
				System.out.println("Port: " + i + ": " + mc.readUltraSonic(i));
				if(mc.readUltraSonic(i) < 400) vorhanden = true;
			} catch (IOException e) {
				//System.err.println("im Catch");
				e.printStackTrace();
			}
		}
		return vorhanden;
	}

	/**
	 * Diese Methode prueft ob der Ranger einen LineFollower-Sensor hat und dieser korrekte Werte liefert. 
	 * @return true, falls vorhanden, sonst false
	 */
	public boolean hatLineFollower() {
		MbotClient mc =  RangerManagement.getInstance().getMc();
		boolean vorhanden = false;
		for(int i = 1; i<11; i++) {
			try {
				System.out.println("Line: Port: " + i + ": " + mc.readLineFollower(i));
				if (mc.readLineFollower(i) == 3) vorhanden = true; 
			} catch (IOException e) {
				System.err.println("im Catch");
				e.printStackTrace();
			}
		}
		return vorhanden;
	}

	/**
	 * Diese Methode prueft ob der Ranger einen OnboardTemperatur-Sensor hat. 
	 * @return true, falls vorhanden, sonst false
	 */
	public boolean hatInternalTempSensor() {
		MbotClient mc =  RangerManagement.getInstance().getMc();
		try{
			mc.readTempSensorOnboardAuriga();
		}catch(Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Diese Methode prueft ob der Ranger einen OnboardLicht-Sensor hat. 
	 * @return true, falls vorhanden, sonst false
	 */
	public boolean hatInternalLightSensor() {
		MbotClient mc =  RangerManagement.getInstance().getMc();
		try{
			mc.readLightSensorOnboard();
		}catch(Exception e) {
			return false;
		}
		return true;
	}
	
}
