package main.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Komponentenauflistung implements Serializable{
	List<String> alleKomponenten;

	public Komponentenauflistung() {
		alleKomponenten = new ArrayList<String>();
	}
	
	public List<String> getAlleKomponenten() {
		return alleKomponenten;
	}

	public void setAlleKomponenten(List<String> alleKomponenten) {
		this.alleKomponenten = alleKomponenten;
	}
	
}
