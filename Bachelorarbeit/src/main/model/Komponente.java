package main.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

public class Komponente implements Serializable{

	private String name;
	private Vorhandensein vorhandensein; //0 fuer unsicher, 1 fuer vorhanden, 2 fuer nichtVorhanden
	
	public Komponente() {
	}
	
	
	public Komponente(String name, Vorhandensein vorhandensein) {
		super();
		this.name = name;
		this.vorhandensein = vorhandensein;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name="ist_vorhanden")
	public Vorhandensein getVorhandensein() {
		return vorhandensein;
	}
	public void setVorhandensein(Vorhandensein vorhandensein) {
		this.vorhandensein = vorhandensein;
	}
	
	@Override
	public String toString() {
		return "Komponente: name=" + name + ", vorhandensein=" + vorhandensein;
	}
}
