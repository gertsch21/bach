package main;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import purejavacomm.CommPortIdentifier;

/**
 * Die klasse wird verwendet, um herauszufinden, wie die einzelnen namen der commports sind
 * bei linux sind dies ttyUSB* und bei windows COM*
 * @author Gerhard
 *
 */
public class CommPortTests {

	public static void main(String[] args) {
		CommPortIdentifier cpi = null;

		Enumeration e =  CommPortIdentifier.getPortIdentifiers();

		while (e.hasMoreElements()) {
            try {
                cpi = (CommPortIdentifier) e.nextElement();
            } catch (NoSuchElementException n) {

            }
            System.out.println(cpi.getName());
        }
	}

}
