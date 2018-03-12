package main;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import purejavacomm.CommPortIdentifier;

/**
 * Die Klasse wird verwendet, um herauszufinden, wie die einzelnen Namen der Commports heissen
 * bei Linux sind dies ttyUSB* und bei Windows COM*
 * @author Gerhard
 *
 */
public class CommPortTests {

	public static void main(String[] args) {
		CommPortIdentifier cpi = null;

		Enumeration e =  CommPortIdentifier.getPortIdentifiers();
		
		//ueber alle CommPorts durchiterieren
		while (e.hasMoreElements()) {
            try {
                cpi = (CommPortIdentifier) e.nextElement();
            } catch (NoSuchElementException n) {

            }
            System.out.println(cpi.getName());
        }
	}

}
