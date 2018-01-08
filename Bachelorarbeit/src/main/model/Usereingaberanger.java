package main.model;

import java.io.Serializable;

/**
 * Wird zum fahren verwendet, ist eigentlich nur zum testen ob fahrbar
 * serializable, damit automatisch gebindet wird(bei post request auf steuern
 * 
 * @author Gerhard
 *
 */
public class Usereingaberanger{
	private String geschwindigkeit;
	private String dauer;
	

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
