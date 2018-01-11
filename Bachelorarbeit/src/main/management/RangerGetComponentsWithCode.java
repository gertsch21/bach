package main.management;

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