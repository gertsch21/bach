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
		for(int i = 0; i<11; i++) {
			try {
				System.out.println(mc.readUltraSonic(i));
			} catch (IOException e) {
				System.err.println("im Catch");
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean hatLineFollower() {
		return false;
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
