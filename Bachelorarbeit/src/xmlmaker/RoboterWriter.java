package xmlmaker;

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

import model.Roboter;

public class RoboterWriter {

	private String filename;
	private JAXBContext context;
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;
	

	public RoboterWriter() throws JAXBException {
		this("Roboter.xml");
	}
	public RoboterWriter(String filename) throws JAXBException {
		this.context = JAXBContext.newInstance(Roboter.class);
		
		this.marshaller = this.context.createMarshaller();
		this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		this.unmarshaller = this.context.createUnmarshaller();
		
		this.filename = filename;
	}
	

	public String objectToXMLString(Roboter x) throws JAXBException{
		StringWriter sw = new StringWriter();
		marshaller.marshal(x, sw);
		return sw.toString();
	}
	
	
	public void serialize(Roboter x) throws JAXBException{
		marshaller.marshal(x, new File("files\\"+this.filename));
	}
	
	public Roboter deserialize() throws FileNotFoundException, JAXBException{
		Path p = Paths.get("files\\"+this.filename);
		boolean exists = Files.exists(p);
		if(!exists) throw new FileNotFoundException();
		
		Object unmarshalled = this.unmarshaller.unmarshal(new File("files\\"+this.filename));
		if(unmarshalled instanceof Roboter)
			return (Roboter) unmarshalled;
		else 
			return null;
	}
	
	
	public static void main(String[] args) {
		try {
			RoboterWriter writer = new RoboterWriter();
			
			Roboter toFile = new Roboter();
			writer.serialize(toFile);
			
			Roboter fromFile = writer.deserialize();
			System.out.println(fromFile);
		} catch (JAXBException e) {
			System.err.println("Achtung: Fehler von JaxB!!");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}


}
