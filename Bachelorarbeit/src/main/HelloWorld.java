package main;

import javax.xml.bind.JAXBException;

import model.Roboter;
import xmlmaker.RoboterWriter;

public class HelloWorld {

	public static void main(String[] args) {
		System.out.println("Starte main!");
		
		try {
			System.setProperty("javax.xml.bind.JAXBContextFactory", "org.eclipse.persistence.jaxb.JAXBContextFactory");
			RoboterWriter writer = new RoboterWriter();
			
			Roboter toFile = new Roboter(1,"Rover","Makeblock",true);
			writer.serialize(toFile);
			
			Roboter fromFile = writer.deserialize();
			System.out.println(fromFile);
		} catch (JAXBException e) {
			System.err.println("Achtung: Fehler von JaxB!!");
			e.printStackTrace();
		}	
		
		System.out.println("Beende main");
	}
	

}
