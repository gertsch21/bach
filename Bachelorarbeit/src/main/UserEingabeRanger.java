package main;

import java.io.Serializable;

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
