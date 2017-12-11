package model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Roboter {
	private int id;
	private String name;
	private String company;
	private boolean moveable;

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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public boolean isMoveable() {
		return moveable;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}

	public Roboter(int id, String name, String company, boolean moveable) {
		super();
		this.id = id;
		this.name = name;
		this.company = company;
		this.moveable = moveable;
	}

	public Roboter() {
		super();
	}

	@Override
	public String toString() {
		return "Roboter [id=" + id + ", name=" + name + ", company=" + company + ", moveable=" + moveable + "]";
	}

}
