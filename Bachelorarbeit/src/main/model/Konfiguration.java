package main.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;

@XmlRootElement
public class Konfiguration implements Serializable{
	private String name;
	private String robotername;
	private List<Komponente> komponenten;
	
	public Konfiguration() {
		this.komponenten = new ArrayList<Komponente>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRobotername() {
		return robotername;
	}
	public void setRobotername(String robotername) {
		this.robotername = robotername;
	}
	
	@XmlElementWrapper
    @XmlElement(name="komponente")
	public List<Komponente> getKomponenten() {
		return komponenten;
	}
	public void setKomponenten(List<Komponente> komponenten) {
		this.komponenten = komponenten;
	}
	@Override
	public String toString() {
		String returnString = "Konfiguration:\n name=" + name + "\n robotername=" + robotername+"\n ";
		for(Komponente k : this.getKomponenten())
			returnString = returnString+k+"\n ";
		return returnString;
	}
	
}
