package main.management;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import main.mbot.client.IMbotEvent;
import main.mbot.client.MbotClient;
import main.model.Komponente;
import main.model.Konfiguration;
import main.model.Roboter;
import main.model.Vorhandensein;
import purejavacomm.CommPortIdentifier;
import purejavacomm.NoSuchPortException;
import purejavacomm.PortInUseException;
import purejavacomm.UnsupportedCommOperationException;

/**
 * Singleton
 * Diese Klasse ist fuer die Businesslogic des Rangers vorhanden,
 * jegliche Informationen ueber den Ranger werden in dieser Klasse agglomeriert
 * und weitergegeben, zudem koennen auch die befehle, welche der Ranger kann
 * ueber diese klasse abgegeben werden
 * 
 * @author Gerhard
 */
public class RangerManagement implements IMbotEvent {
	private int comPort;
	private static MbotClient mc;
	private RoboterXMLWriter rw;
	private static RangerManagement instance; // singleton

	/**
	 * Es werden die wichtigsten Einstellungen vorgenommen. Etwa wird der ComPort festgelegt auf 6(Raspberry) -> Siehe CommPortTest
	 * Zudem wird eine Verbindung mit dem Ranger selbst hergestellt
	 */
	private RangerManagement() {
		System.out.println("Rangermanagement:Defaultkonstruktor");
		
		this.comPort = 6;
		System.out.println("COM Port benutzt: " + this.comPort);

		try {
			// mc = new MbotClient(CommPortIdentifier.getPortIdentifier("COM6"));

			// this.mc = new MbotClient(CommPortIdentifier.getPortIdentifier("COM" + comPort)); //nur fuer windows
			this.mc = new MbotClient(CommPortIdentifier.getPortIdentifier("ttyUSB0")); //nur fuer Raspian
			this.mc.addListener(this);
			this.mc.reset();
			
			//nur damit man sieht(wird gruen) wenn raspberry auf ranger zugreift
			for (int j = 0; j < 15; j++)
				mc.rbgLEDAuriga(j, 0, 100, 0);
			
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
			this.rw = new RoboterXMLWriter(); //Falls schon ein file vorhanden, wird dieses genommen
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		System.out.println("Ranger initialisiert");
	}

	/**
	 * Fuer Singleton
	 * @return Die Instanz der Klasse RangerManagement
	 */
	public static RangerManagement getInstance() {
		if (instance == null)
			instance = new RangerManagement();
		return instance;
	}

	/**
	 * Um eine Instanz der MbotClient-Klasse zu bekommen(Direkte Verbindung zum Roboter)
	 * @return eine Instanz der MbotClient-Klasse
	 */
	public  MbotClient getMc() {
		return mc;
	}
	
	/**
	 * Retourniert  spezielle Komponenten
	 * @return alle Komponenten, bei denen der User manuell das Vorhandensein bestaetigt hat.
	 */
	public List<Komponente> getAllVorhandenKonfiguriert() {
		try {
			KonfigurationXMLWriter kw = new KonfigurationXMLWriter();
			Konfiguration konf = kw.deserialize();
			List<Komponente> alsVorhandenMarkiert = new ArrayList<Komponente>();
			for(Komponente kom : konf.getKomponenten()){
				if(kom.getVorhandensein() == Vorhandensein.VORHANDEN)
					alsVorhandenMarkiert.add(kom);
			}
			return alsVorhandenMarkiert;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * Diese Methode soll die aktuellen Daten vom Roboter in das XML File lokal
	 * zwischen speichern NOCH NICHT FERTIG; NUR ZUM TESTEN!!!!!!!!!!!!!!!!!!!!
	 * 
	 * @throws JAXBException
	 */
	public void saveCurrentRoboterData() throws JAXBException {
		System.out.println("Starte:RangerManagement:saveCurrentRoboterData");		
		//RangerGetComponentsWithCode getcomponents = RangerGetComponentsWithCode.getInstance();
		List<Komponente> vorkonfiguriertVorhanden = getAllVorhandenKonfiguriert();
		Konfiguration konf = getKonfiguration();
		
		Roboter myRanger = new Roboter();
		myRanger.setId(1);
		myRanger.setName(konf.getRobotername());
		myRanger.setfirma("Makeblock");
		myRanger.setKomponenten(vorkonfiguriertVorhanden);
		
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
			saveCurrentRoboterData(); //speichere die aktuellen werte in file
			return new RoboterXMLWriter().deserialize(); //hole file als xml formatiert
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Um die Roboterdaten als XML zu formatieren
	 * @return Die aktuell gespeicherten Roboterdaten als XML-Formatiert 
	 * @throws JAXBException
	 */
	public String getCurrentRoboterDataAsXML() throws JAXBException {
		System.out.println("Starte:RangerManagement:getCurrentRoboterDataAsXML");
		return rw.roboterToXMLString(getCurrentRoboterData());
	}
	
	/**
	 * Hier werden nicht die aktuellen Werte geholt, sondern 
	 * @return
	 */
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
		return rw.roboterToXMLString(getRoboterData());
	}

	
	
	
	
	
	private void gyroDemo(MbotClient mc) {
		float x = mc.readGyro(1);
		float y = mc.readGyro(2);
		float z = mc.readGyro(3);
		System.out.println(String.format("%.2f %.2f %.2f", x, y, z));
	}
	
	
	@Override
	public void onButton(boolean pressed) {
		// TODO Auto-generated method stub

	}

	public void moveForward(int gesch, int sec) throws IOException, InterruptedException  {
		mc.encoderMotorLeft(gesch);
		mc.encoderMotorRight(-gesch);
		Thread.sleep(sec * 1000);
		mc.encoderMotorLeft(0);
		mc.encoderMotorRight(0);
	}

	public void moveBackward(int gesch, int sec) throws IOException, InterruptedException  {
		mc.encoderMotorLeft(-gesch);
		mc.encoderMotorRight(gesch);
		Thread.sleep(sec * 1000);
		mc.encoderMotorLeft(0);
		mc.encoderMotorRight(0);
	}

	
	public void rechtsDrehenWinkel(double winkel)throws IOException, InterruptedException  {
		if(winkel<0||winkel>=360) {
			System.err.println("negativer winkel");
			return;
		}
		double ausgangswinkel =  mc.readGyroSensorOnboard(3);
		if(ausgangswinkel <0) ausgangswinkel = 180 - (ausgangswinkel*(-1)) + 180;
		double ziel = ausgangswinkel + winkel;
		
		int aktuellWinkel = (int) ausgangswinkel;
		System.out.println("Rangermanagement:rechtsDrehenWinkel: Ausgangswinkel: "+ausgangswinkel);
		
		System.out.println("Rangermanagement:rechtsDrehenWinkel: Aktuell/Ziel: "+aktuellWinkel+"/"+ziel);

		
		while(aktuellWinkel<ziel) {
			mc.encoderMotorRight(140);
			mc.encoderMotorLeft(140);
			aktuellWinkel = (int) mc.readGyroSensorOnboard(3);
			if(aktuellWinkel < 0) aktuellWinkel = 180 - (aktuellWinkel*(-1)) + 180;
			if(aktuellWinkel<(int)ausgangswinkel) aktuellWinkel = aktuellWinkel+360;
			//System.out.println("Aktuell/Ziel: "+aktuellWinkel+"/"+ziel);
		}
		mc.encoderMotorRight(0);
		mc.encoderMotorLeft(0);

		System.out.println("Rangermanagement:rechtsDrehenWinkel: Aktuell/Ziel: "+aktuellWinkel+"/"+ziel);

		
	}
	
	public void turnRight() throws IOException, InterruptedException  {
		rechtsDrehenWinkel(90);
	}

	public void stop() throws Exception {
		mc.encoderMotorLeft(0);
		mc.encoderMotorRight(0);
	}


	public Konfiguration getKonfiguration() {
		Konfiguration konfiguration = null;
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream("selbstkonfiguration.conf"));
			konfiguration = (Konfiguration) in.readObject();
		}catch(Exception e){
			
		}
		if(konfiguration == null) konfiguration = new Konfiguration();
		return konfiguration;
	}

	
	public void saveKonfiguration(Konfiguration konfiguration) {
		ObjectOutputStream aus = null;
		try {
			aus = new ObjectOutputStream(new FileOutputStream("selbstkonfiguration.conf"));
			aus.writeObject(konfiguration); //das automatisch von html gemappte
		} catch (IOException ex) {
			System.out.println("Rangermanagement:saveKonfiguration: " + ex);
		} finally {
			try {
				if (aus != null) {
					aus.flush();
					aus.close();
				}
			} catch (IOException e) {
			}
		}
	}
	
}
