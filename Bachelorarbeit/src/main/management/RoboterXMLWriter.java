package main.management;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import main.model.Roboter;

public class RoboterXMLWriter {

	private String filename;
	private JAXBContext context;
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;
	

	public RoboterXMLWriter() throws JAXBException {
		this("src/main/resources/templates/rangerGerhard.xml");
	}
	public RoboterXMLWriter(String filename) throws JAXBException {
		this.context = JAXBContext.newInstance(Roboter.class);
		
		this.marshaller = this.context.createMarshaller();
		this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		this.unmarshaller = this.context.createUnmarshaller();
		
		this.filename = filename;
	}
	

	public String roboterToXMLString(Roboter robot) throws JAXBException{
		StringWriter sw = new StringWriter();
		marshaller.marshal(robot, sw);
		return sw.toString();
	}
	
	public String getRoboterXMLString() throws FileNotFoundException, JAXBException {
		Roboter r = this.deserialize();
		return roboterToXMLString(r);
	}
	
	public void serialize(Roboter robot) throws JAXBException{
		marshaller.marshal(robot, new File(this.filename));
	}
	
	public Roboter deserialize() throws FileNotFoundException, JAXBException{
		Path p = Paths.get(this.filename);
		
		boolean exists = Files.exists(p);
		if(!exists) throw new FileNotFoundException();
		
		Object unmarshalled = this.unmarshaller.unmarshal(new File(p.toString()));
		
		if(unmarshalled instanceof Roboter)
			return (Roboter) unmarshalled;
		else 
			return null;
	}
	
	
	public static void main(String[] args) {
		try {
			RoboterXMLWriter writer = new RoboterXMLWriter();
			
			System.out.println(writer.getRoboterXMLString());
		} catch (JAXBException e) {
			System.err.println("Achtung: Fehler von JaxB!!");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}


}
