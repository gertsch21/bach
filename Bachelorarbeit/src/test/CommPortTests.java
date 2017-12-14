package test;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import purejavacomm.CommPortIdentifier;

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
