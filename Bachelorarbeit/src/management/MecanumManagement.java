package management;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import mbot.client.MbotClient;
import model.Roboter;
import purejavacomm.CommPortIdentifier;
import purejavacomm.NoSuchPortException;
import purejavacomm.PortInUseException;
import purejavacomm.UnsupportedCommOperationException;
import xmlmaker.RoboterWriter;

public class RangerManagement {
	MbotClient mc;
	private static RangerManagement instance;
	
	private RangerManagement() {
	        try {
	            // mc = new MbotClient(CommPortIdentifier.getPortIdentifier("COM6"));
	            this.mc = new MbotClient(CommPortIdentifier.getPortIdentifier("COM4"));

	        } catch (PortInUseException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (UnsupportedCommOperationException e) {
	            e.printStackTrace();
	        } catch (NoSuchPortException e) {
	            e.printStackTrace();
	        }

	        System.out.println("Ranger initialisiert");
	}
	
	public static RangerManagement getInstance() {
		if(instance == null) 
			instance = new RangerManagement();
		return instance;
	}
	
	
	public static void main(String[] args) throws InterruptedException, JAXBException {
		System.out.println("Start Rover Test");
		//String comPort = "COM4";//Bluetooth
		String comPort = "COM6";//USB
		String xmlFilename = "Roboter.xml"; 
		RoboterWriter rw = new RoboterWriter(xmlFilename);
		MbotClient mc= null;
		
		try {
			Roboter myRanger = new Roboter();
			myRanger.setId(1);
			myRanger.setName("myRanger");
			myRanger.setfirma("Makeblock");
			myRanger.setFahrbar(istFahrbar());
			myRanger.setAbstandssensor(hatAbstandssensor());
			myRanger.setLinefollower(hatLineFollower());
			rw.serialize(myRanger);
			Thread.sleep(2000);
		    mc = new MbotClient(CommPortIdentifier.getPortIdentifier(comPort));
		} catch (PortInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPortException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Thread.sleep(3000);
		if(mc!=null)
			mc.close();
		
		System.out.println("Zuletzt gespeicherter Roboter: "+rw.deserialize());
        System.out.println("program finished");
	}
	
	
	
	
	public Roboter getRoboterData() {
		Roboter x = new Roboter();
		x.setAbstandssensor(hatAbstandssensor());
		x.setFahrbar(istFahrbar());
		x.setfirma(getFirma());
		x.setId(1);
		x.setLinefollower(hatLineFollower());
		x.setName(getName());
		return x;
	}
	
	
	private static boolean istFahrbar() {
		return true;
	}
	private static  boolean hatAbstandssensor() {
		return false;
	}
	private static  boolean hatLineFollower() {
		return false;
	}
	private static String getFirma() {
		return "Makeblock";
	}
	private static String getName() {
		return "Makeblock Ranger";
	}

	
}
