package roboter;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import mbot.client.MbotClient;
import model.Roboter;
import purejavacomm.CommPortIdentifier;
import purejavacomm.NoSuchPortException;
import purejavacomm.PortInUseException;
import purejavacomm.UnsupportedCommOperationException;
import xmlmaker.RoboterWriter;

public class RangerGerhard {

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
	
	public static boolean istFahrbar() {
		return true;
	}
	public static  boolean hatAbstandssensor() {
		return false;
	}
	public static  boolean hatLineFollower() {
		return false;
	}

}
