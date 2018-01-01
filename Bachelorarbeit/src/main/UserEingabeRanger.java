package main;

import java.io.Serializable;

/**
 * Wird zum fahren verwendet, ist eigentlich nur zum testen ob fahrbar
 * serializable, damit automatisch gebindet wird(bei post request auf steuern
 * 
 * @author Gerhard
 *
 */
public class UserEingabeRanger implements Serializable{
	private String geschwindigkeit;
	private String dauer;
	
	
	public UserEingabeRanger(String geschwindigkeit, String dauer) {
		super();
		this.geschwindigkeit = geschwindigkeit;
		this.dauer = dauer;
	}
	public UserEingabeRanger() {
		super();
	}
	public String getGeschwindigkeit() {
		return geschwindigkeit;
	}
	public void setGeschwindigkeit(String geschwindigkeit) {
		this.geschwindigkeit = geschwindigkeit;
	}
	public String getDauer() {
		return dauer;
	}
	public void setDauer(String dauer) {
		this.dauer = dauer;
	}
	
}
