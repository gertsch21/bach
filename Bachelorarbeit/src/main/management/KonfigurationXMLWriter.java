package main.management;

import java.io.File;
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
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import main.model.Komponente;
import main.model.Konfiguration;
import main.model.Vorhandensein;

public class KonfigurationXMLWriter {

	private String filename;
	private JAXBContext context;
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;
	

	public KonfigurationXMLWriter() throws JAXBException {
		this("selbstkonfiguration.conf");
	}
	
	public KonfigurationXMLWriter(String filename) throws JAXBException {
		this.context = JAXBContext.newInstance(Konfiguration.class);
		
		this.marshaller = this.context.createMarshaller();
		this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		this.unmarshaller = this.context.createUnmarshaller();
		
		this.filename = filename;
	}
	

	public String getKonfigurationToXMLString(Konfiguration konfiguration) throws JAXBException{
		StringWriter sw = new StringWriter();
		marshaller.marshal(konfiguration, sw);
		return sw.toString();
	}
	
	
	public void serialize(Konfiguration konfiguration) throws FileNotFoundException, IOException {
		ObjectOutputStream aus = null;
		aus = new ObjectOutputStream(new FileOutputStream("selbstkonfiguration.conf"));
		aus.writeObject(konfiguration);
	}
	
	public Konfiguration deserialize() throws JAXBException, IOException, ClassNotFoundException{
		Konfiguration konfiguration = null;
		ObjectInputStream in = null;

		Path p = Paths.get(this.filename);
		boolean exists = Files.exists(p);
		if(!exists) throw new FileNotFoundException();
		
		in = new ObjectInputStream(new FileInputStream("selbstkonfiguration.conf"));
		konfiguration = (Konfiguration) in.readObject();	
		 
		return konfiguration;
	}
	
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		try {
			KonfigurationXMLWriter writer = new KonfigurationXMLWriter();
			
			Konfiguration neu = new Konfiguration();
			neu.setName("Neu");
			neu.setRobotername("NeuRobi");
			List<Komponente> liste = new ArrayList<>();
			liste.add(new Komponente("Sensor1", Vorhandensein.forName("VORHANDEN")));
			liste.add(new Komponente("Sensor2", Vorhandensein.forName("unsicher")));
			liste.add(new Komponente("Sensor3", Vorhandensein.forName("nicht_VORHANDEN")));
			neu.setKomponenten(liste);
			writer.serialize(neu);
			
			
			Konfiguration k = writer.deserialize();
			
			System.out.println(writer.getKonfigurationToXMLString(writer.deserialize()));
		} catch (JAXBException e) {
			System.err.println("Achtung: Fehler von JaxB!!");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}


}
