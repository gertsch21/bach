package mbot.client;

/**
 * Device IDs from mBot firmware files(mbot_firmware.ino, orion_firmware.ino)
 *
 * @author    Martin Kunz <martin.michael.kunz@gmail.com>
 */
public enum DeviceType {
    NONE((byte)0),
    ULTRASONIC_SENSOR((byte)1),
    TEMPERATURE_SENSOR((byte)2),
    LIGHT_SENSOR((byte)3),
    POTENTIONMETER((byte)4),
    JOYSTICK((byte)5),
    GYRO((byte)6),
    SOUND_SENSOR((byte)7),
    RGBLED((byte)8),
    SEVSEG((byte)9),
    MOTOR((byte)10),
    SERVO((byte)11),
    ENCODER((byte)12),
    IR((byte)13),
    IRREMOTE((byte)14),
    PIRMOTION((byte)15),
    INFRARED((byte)16),
    LINEFOLLOWER((byte)17),
    IRREMOTECODE((byte)18),
    SHUTTER((byte)20),
    LIMITSWITCH((byte)21),
    BUTTON((byte)22),
    HUMITURE((byte)23),
    FLAMESENSOR((byte)24),
    GASSENSOR((byte)25),
    COMPASS((byte)26),
    TEMP_SENSOR_ONBOARD_AURIGA((byte)27), //added by Muck
    DIGITAL((byte)30),
    ANALOG((byte)31),
    PWM((byte)32),
    SERVO_PIN((byte)33),
    TONE((byte)34),
    BUTTON_INNER((byte)35),
    ULTRASONIC_ARDUINO((byte)36),
    PULSEIN((byte)37),
    STEPPER((byte)40),
    LEDMATRIX((byte)41),
    TIMER((byte)50),
    TOUCH_SENSOR((byte)51),
    ENCODERMOTOR((byte)61), //added by Muck
    MECANUM((byte)65);

    private final byte id;

    private DeviceType(final byte id) {
        this.id = id;
    }

    public byte getId() {
        return id;
    }

    public static DeviceType getById(int id) {
        for(DeviceType e : values()) { //values() ist builtin bei allen enums und listet alle werte dieses enums auf
            if(e.id==id) return e;
        }
        return null;
    }
}
