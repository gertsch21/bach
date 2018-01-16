package main;

import main.management.RangerManagement;

public class Testklasse {

	public static void main(String[] args) {
		System.out.println(RangerManagement.getInstance().getAllVorhandenKonfiguriert().size());
		
	}
}
