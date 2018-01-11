package main.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;


@XmlRootElement(name = "roboter")
public class Konfiguration implements Serializable{
	private String username;
	private String robotername;
	private List<Komponente> komponenten;
	
	public Konfiguration() {
		this.username = "Gib deinen Namen ein!";
		this.robotername = "Wie heißt dein Roboter?";
		this.komponenten = new ArrayList<Komponente>();
        
		BufferedReader in = null; 
		try { 
            in = new BufferedReader(new FileReader("AlleKomponentenStartup")); 
            String zeile = null; 
            while ((zeile = in.readLine()) != null) { 
                this.komponenten.add(new Komponente(zeile, Vorhandensein.UNSICHER)); 
            } 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } finally { 
            if (in != null) 
                try { 
                    in.close(); 
                } catch (IOException e) { 
                } 
        } 
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
    @XmlAttribute(name="name")
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
		String returnString = "Konfiguration:\n username=" + username + "\n robotername=" + robotername+"\n ";
		for(Komponente k : this.getKomponenten())
			returnString = returnString+k+"\n ";
		return returnString;
	}

	public static void main(String[] args) {
		Konfiguration k = new Konfiguration();
	}
	
}
