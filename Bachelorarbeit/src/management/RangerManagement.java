package management;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import mbot.client.IMbotEvent;
import mbot.client.MbotClient;
import model.Roboter;
import purejavacomm.CommPortIdentifier;
import purejavacomm.NoSuchPortException;
import purejavacomm.PortInUseException;
import purejavacomm.UnsupportedCommOperationException;
import xmlmaker.RoboterWriter;

/**
 * Singleton Diese Klasse ist fuer die Businesslogic des Rangers vorhanden,
 * jegliche Informationen ueber den Ranger werden in dieser Klasse agglomeriert
 * und weitergegeben, zudem koennen auch die befehle, welche der Ranger kann
 * ueber diese klasse abgegeben werden
 * 
 * @author Gerhard
 *
 */
public class RangerManagement implements IMbotEvent {
	private int comPort;
	private static MbotClient mc;
	private RoboterWriter rw;
	private static RangerManagement instance; // singleton

	private RangerManagement() {
		System.out.println("Rangermanagement:Defaultkonstruktor");
		this.comPort = 6;
		System.out.println("COM Port benutzt: " + this.comPort);

		try {
			// mc = new MbotClient(CommPortIdentifier.getPortIdentifier("COM6"));

			//this.mc = new MbotClient(CommPortIdentifier.getPortIdentifier("COM" + comPort)); 
			this.mc = new MbotClient(CommPortIdentifier.getPortIdentifier("ttyUSB0"));
			this.mc.addListener(this);
			this.mc.reset();
			for (int j = 0; j < 15; j++) {
				System.out.println("Sound sensor: " + mc.readSoundSensorAuriga(14));
				System.out.println("onboard temp: " + mc.readTempSensorOnboardAuriga());
				mc.rbgLEDAuriga(j, 0, 100, 0);
				Thread.sleep(250);
			}

			System.out.println("MC: " + mc);

		} catch (PortInUseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
		} catch (NoSuchPortException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("OHOH Exception unknown");
		}

		try {
			this.rw = new RoboterWriter();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		System.out.println("Ranger initialisiert");
	}

	public static RangerManagement getInstance() {
		if (instance == null)
			instance = new RangerManagement();
		return instance;
	}

	private void gyroDemo(MbotClient mc) {
		float x = mc.readGyro(1);
		float y = mc.readGyro(2);
		float z = mc.readGyro(3);
		System.out.println(String.format("%.2f %.2f %.2f", x, y, z));
	}

	/**
	 * Diese Methode soll die aktuellen Daten vom Roboter in das XML File lokal
	 * zwischen speichern NOCH NICHT FERTIG; NUR ZUM TESTEN!!!!!!!!!!!!!!!!!!!!
	 * 
	 * @throws JAXBException
	 */
	public void saveCurrentRoboterData() throws JAXBException {
		System.out.println("Starte:RangerManagement:saveCurrentRoboterData");
		Roboter myRanger = new Roboter();
		myRanger.setId(1);
		myRanger.setName("myRanger");
		myRanger.setfirma("Makeblock");
		myRanger.setFahrbar(istFahrbar());
		myRanger.setAbstandssensor(hatAbstandssensor());
		myRanger.setLinefollower(hatLineFollower());
		rw.serialize(myRanger);
	}

	/**
	 * soll nicht aus dem zwischengespeicherten file die Daten holen, sondern frisch
	 * vom Arduino
	 * 
	 * @return
	 */
	public Roboter getCurrentRoboterData() {
		System.out.println("Starte:RangerManagement:getCurrentRoboterData");
		try {
			System.out.println("RangerManagement:getCurrentRoboterData:Neue Daten von Roboter holen");
			saveCurrentRoboterData();
			return new RoboterWriter().deserialize();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getCurrentRoboterDataAsXML() throws JAXBException {
		System.out.println("Starte:RangerManagement:getCurrentRoboterDataAsXML");
		return rw.objectToXMLString(getCurrentRoboterData());
	}

	public Roboter getRoboterData() {
		System.out.println("Starte:RangerManagement:getRoboterData");
		Roboter vonXML_file = null;
		try {
			vonXML_file = rw.deserialize();
		} catch (JAXBException e) {
			System.err.println("RangerManagement:getRoboterData: Fehler beim Deserialisieren mit JAXB");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("RangerManagement:getRoboterData: XML File nicht gefunden.");
			return getCurrentRoboterData();// da noch keins vorhanden
		}

		return vonXML_file;
	}

	public String getRoboterDataAsXML() throws JAXBException {
		System.out.println("Starte:RangerManagement:getRoboterDataAsXML");
		return rw.objectToXMLString(getRoboterData());
	}

	private static boolean istFahrbar() {
		return true;
	}

	private static boolean hatAbstandssensor() {
		return false;
	}

	private static boolean hatLineFollower() {
		return false;
	}

	private static String getFirma() {
		return "Makeblock";
	}

	private static String getName() {
		return "Makeblock Ranger";
	}

	@Override
	public void onButton(boolean pressed) {
		// TODO Auto-generated method stub

	}

	public static void moveForward(int sec) throws Exception {
		mc.encoderMotorLeft(200);
		mc.encoderMotorRight(-200);
		Thread.sleep(sec*1000);
		mc.encoderMotorLeft(0);
		mc.encoderMotorRight(0);
	}
	
	public static void moveBackward(int sec) throws Exception {
		mc.encoderMotorLeft(-200);
		mc.encoderMotorRight(200);
		Thread.sleep(sec*1000);
		mc.encoderMotorLeft(0);
		mc.encoderMotorRight(0);
	}
	

	public static void stop() throws Exception {
		mc.encoderMotorLeft(0);
		mc.encoderMotorRight(0);
	}

}
