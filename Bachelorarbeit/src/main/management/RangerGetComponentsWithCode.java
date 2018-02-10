package main.management;

import java.io.IOException;

import main.mbot.client.MbotClient;

public class RangerGetComponentsWithCode {

	private static RangerGetComponentsWithCode instance;
	
	private RangerGetComponentsWithCode() {
		System.out.println("RangerGetComponentsWithCode:Defaultkonstruktor");
	}

	public static RangerGetComponentsWithCode getInstance() {
		if (instance == null)
			instance = new RangerGetComponentsWithCode();
		return instance;
	}


	
	
	public boolean istFahrbar() {

		try {
			RangerManagement.getInstance().getMc().encoderMotorLeft(0);
			RangerManagement.getInstance().getMc().encoderMotorRight(0);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean hatAbstandssensor() {
		MbotClient mc =  RangerManagement.getInstance().getMc();
		boolean vorhanden = false;
		for(int i = 1; i<11; i++) {
			try {
				//System.out.println("Port: " + i + ": " + mc.readUltraSonic(i));
				if(mc.readUltraSonic(i) <400) vorhanden = true;
			} catch (IOException e) {
				//System.err.println("im Catch");
				e.printStackTrace();
			}
		}
		return vorhanden;
	}

	public boolean hatLineFollower() {
		MbotClient mc =  RangerManagement.getInstance().getMc();
		boolean vorhanden = false;
		for(int i = 1; i<11; i++) {
			try {
				System.out.println("Line: Port: " + i + ": " + mc.readLineFollower(i));
				
			} catch (IOException e) {
				System.err.println("im Catch");
				e.printStackTrace();
			}
		}
		return vorhanden;
	}

	public boolean hatInternalTempSensor() {
		return false;
	}

	public boolean hatExternalTempSensor() {
		return false;
	}

	
	public static void main(String[] args) {
		System.out.println(RangerGetComponentsWithCode.getInstance().istFahrbar());
	}
	
}
