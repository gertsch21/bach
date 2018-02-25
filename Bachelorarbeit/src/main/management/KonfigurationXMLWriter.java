package main.management;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import main.model.Konfiguration;

/**
 * Diese Klasse ist fuer die Erstellung eines XMLs zustaendig. 
 * Es koennen so verschiedene Objekte vordefinierter Klassen in ein XML-Format ausgegeben werden.
 * Zudem handelt diese Klasse auch die Fileerstellung der Konfiguration, sowie dessen Holen.
 * @author Gerhard
 *
 */
public class KonfigurationXMLWriter {

	private String filename;
	private JAXBContext context;
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;
	
	/**
	 * Falls kein Filename angegeben wird, wird automatisch das File in diesem Ordner mit dem Namen 'selbstkonfiguration.conf' gewaehlt.
	 * @throws JAXBException
	 */
	public KonfigurationXMLWriter() throws JAXBException {
		this("selbstkonfiguration.conf");
	}
	/**
	 * Der Marshaller und der Unmarshaller werden angelegt, sowie der Filename zugewiesen
	 * @param filename Der Filename, in welcher die Konfiguration persistent gespeichert ist. 
	 * @throws JAXBException Falls eine Exception bei XML Konversation auftaucht
	 */
	public KonfigurationXMLWriter(String filename) throws JAXBException {
		this.context = JAXBContext.newInstance(Konfiguration.class);
		
		this.marshaller = this.context.createMarshaller();
		this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		this.unmarshaller = this.context.createUnmarshaller();
		
		this.filename = filename;
	}
	

	/**
	 * Diese Methode wandelt ein gegebenes Konfigurations-objekt in ein XML-Dokument(Stringobjekt) um. 
	 * @param konfiguration Das zu umwandelnde Konfigurations-objekt
	 * @return Die Konfiguration als XML-formatiert
	 * @throws JAXBException Falls bei der Umwandlung Fehler auftauchen
	 */
	public String getKonfigurationToXMLString(Konfiguration konfiguration) throws JAXBException{
		StringWriter sw = new StringWriter();
		marshaller.marshal(konfiguration, sw);
		return sw.toString();
	}
	
	/**
	 * Diese Methode speichert die Konfiguration, welche uebergeben wird, in das vorgegebene File um diese persistent vorhanden zu haben.
	 * @param konfiguration das zu serialisierende Konfigurations-objekt
	 * @throws FileNotFoundException Falls das File nicht existiert, in welches das Objekt gespeichert werden soll
	 * @throws IOException Falls ein fehler beim Speichervorgang geschehen ist
	 */
	public void serialize(Konfiguration konfiguration) throws FileNotFoundException, IOException {
		ObjectOutputStream aus = null;
		aus = new ObjectOutputStream(new FileOutputStream("selbstkonfiguration.conf"));
		aus.writeObject(konfiguration);
	}
	
	/**
	 * Mit dieser Methode wird aus dem Konfigurations-File das aktuelle Konfigurationsobjekt geholt und zurueckgegeben. 
	 * 
	 * @return Das persistent gespeicherte Konfigurationsobjekt
	 * @throws FileNotFoundException Falls das File nicht existiert, in welches das Objekt gespeichert werden soll
	 * @throws IOException Falls ein Fehler beim Holen des Objektes von der Datei aufgetreten ist
	 * @throws ClassNotFoundException Falls es sich bei den Daten in der Datei um kein JavaObjekt handelt
	 */
	public Konfiguration deserialize() throws FileNotFoundException, IOException, ClassNotFoundException{
		Konfiguration konfiguration = null;
		ObjectInputStream in = null;

		Path p = Paths.get(this.filename);
		boolean exists = Files.exists(p);
		if(!exists) throw new FileNotFoundException();
		
		in = new ObjectInputStream(new FileInputStream("selbstkonfiguration.conf"));
		konfiguration = (Konfiguration) in.readObject();	
		 
		return konfiguration;
	}

}
