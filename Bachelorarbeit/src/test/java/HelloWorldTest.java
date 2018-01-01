package test.java;

import org.junit.Test;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import main.management.RangerManagement;

public class HelloWorldTest {

	@Test
	public void testXMLOutput()  {
		RangerManagement rm = RangerManagement.getInstance();
		boolean fehler = false;
		String roboterdata =null;
		try {
			roboterdata = rm.getRoboterDataAsXML();	
		}catch(JAXBException e) {
			fehler = true;
		}
		
		if(roboterdata==null || roboterdata.isEmpty())
			fehler = true;
		Assert.assertEquals(fehler, false);
	}
	
}
