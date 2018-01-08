package main.model;

import java.io.Serializable;

public class Konfiguration implements Serializable{
	private String name;
	private String robotername;
	
	
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
}
