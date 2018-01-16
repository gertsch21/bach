package main.model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "roboter")
public class Roboter {
	private int id;
	private String name;
	private String firma;
	private List<Komponente> komponenten;
	private String ipAddress;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    @XmlAttribute(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getfirma() {
		return firma;
	}

	public void setfirma(String firma) {
		this.firma = firma;
	}

	@XmlElementWrapper(name = "units")
    @XmlElement(name="unit")
	public List<Komponente> getKomponenten() {
		return komponenten;
	}

	public void setKomponenten(List<Komponente> komponenten) {
		this.komponenten = komponenten;
	}
	
	
	public String getIpAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return "0";
		}
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}



	public Roboter() {
		super();
	}

	@Override
	public String toString() {
		return "Roboter [id=" + id + ", name=" + name + ", firma=" + firma + ", fahrbar=" +"]";
	}



}
