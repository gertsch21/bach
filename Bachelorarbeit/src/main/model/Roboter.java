package main.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Roboter {
	private int id;
	private String name;
	private String firma;
	private boolean fahrbar;
	private boolean abstandssensor;
	private boolean linefollower;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public boolean getFahrbar() {
		return fahrbar;
	}

	public void setFahrbar(boolean fahrbar) {
		this.fahrbar = fahrbar;
	}

	public boolean getAbstandssensor() {
		return abstandssensor;
	}

	public void setAbstandssensor(boolean abstandssensor) {
		this.abstandssensor = abstandssensor;
	}

	public boolean getLinefollower() {
		return linefollower;
	}

	public void setLinefollower(boolean linefollower) {
		this.linefollower = linefollower;
	}


	public Roboter(int id, String name, String firma, boolean fahrbar, boolean abstandssensor, boolean linefollower) {
		super();
		this.id = id;
		this.name = name;
		this.firma = firma;
		this.fahrbar = fahrbar;
		this.abstandssensor = abstandssensor;
		this.linefollower = linefollower;
	}

	public Roboter() {
		super();
	}

	@Override
	public String toString() {
		return "Roboter [id=" + id + ", name=" + name + ", firma=" + firma + ", fahrbar=" + fahrbar
				+ ", abstandssensor=" + abstandssensor + ", linefollower=" + linefollower + "]";
	}



}
