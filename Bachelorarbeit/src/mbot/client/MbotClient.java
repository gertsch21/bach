package mbot.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import purejavacomm.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Client library for original mBot firmware
 * Make sure matching mBlock firmware is running on your device (mbot_firmwae.ino, orion_firmware.ino, ...)
 *
 * @author    Martin Kunz <martin.michael.kunz@gmail.com>
 */
public class MbotClient implements Runnable, AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(MbotClient.class);

    private static final byte TYPE_READ = 0x01;
    private static final byte TYPE_WRITE = 0x02;
    private static final byte TYPE_RESET = 0x04;
    private static final byte TYPE_START = 0x05;
    private static final byte MB_MCU_DATATYPE_ZERO = 0x00;
    private static final byte MB_MCU_DATATYPE_BYTE = 0x01;
    private static final byte MB_MCU_DATATYPE_FLOAT = 0x02;
    private static final byte MB_MCU_DATATYPE_SHORT = 0x03;
    private static final byte MB_MCU_DATATYPE_STRING = 0x04; //length(byte)+string
    private static final byte MB_MCU_DATATYPE_DOUBLE = 0x05;

    public final static int PORT_LIGHTSENSOR_INTERNAL = 6;
    public final static int RGB_INTERNAL_IDX = 2;
    public final static int LEDMATRIX_ACTION_STRING = 1;
    public final static int LEDMATRIX_ACTION_BITMAP = 2;
    public final static int LEDMATRIX_ACTION_CLOCK = 3;
    public final static byte REMOTE_LEFT = 0x07;
    public final static byte REMOTE_UP = 0x40;
    public final static byte REMOTE_RIGHT = 0x09;
    public final static byte REMOTE_DOWN = 0x19;
    public final static byte REMOTE_A = 0x45;
    public final static byte REMOTE_B = 0x46;
    public final static byte REMOTE_C = 0x47;
    public final static byte REMOTE_D = 0x44;
    public final static byte REMOTE_E = 0x43;
    public final static byte REMOTE_F = 0x0D;
    public final static byte REMOTE_OK = 0x15;
    public final static byte REMOTE_0 = 0x16;
    public final static byte REMOTE_1 = 0x0C;
    public final static byte REMOTE_2 = 0x18;
    public final static byte REMOTE_3 = 0x5E;
    public final static byte REMOTE_4 = 0x08;
    public final static byte REMOTE_5 = 0x1C;
    public final static byte REMOTE_6 = 0x5A;
    public final static byte REMOTE_7 = 0x42;
    public final static byte REMOTE_8 = 0x52;
    public final static byte REMOTE_9 = 0x4A;


    //die streams, zu de
    private final InputStream portIS;
    private final OutputStream portOS;
    
    private SerialPort serialPort;
    
    //auf der queue sind lauter results von dem was vom robi kommt
    Set<LinkedBlockingQueue<Result>> subscribers = Collections.synchronizedSet(new HashSet(new LinkedBlockingQueue<>()));
    
    
    Set<IMbotEvent> externalSubscribers = Collections.synchronizedSet(new HashSet<>());
    
    private volatile boolean running = true;
    
    private Thread thread;

    // #define GET 1
    // #define RUN 2
    // #define RESET 4
    // #define START 5

    public MbotClient(CommPortIdentifier portid) throws PortInUseException, IOException, UnsupportedCommOperationException {
        serialPort = (SerialPort) portid.open("egalo", 3500);
        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        serialPort.disableReceiveFraming();
        serialPort.disableReceiveThreshold();
        serialPort.disableReceiveTimeout();
        portIS = serialPort.getInputStream();
        portOS = serialPort.getOutputStream();
        thread = new Thread(this);
        thread.setName("Serial port read thread");
        thread.start();
    }

    @Override
    public void close() {
        running = false;
        thread.interrupt();
        serialPort.close();
    }

    public void addListener(IMbotEvent eventif) {
        externalSubscribers.add(eventif);
    }


    public void reset() throws IOException {
        waitForResult(TYPE_RESET, DeviceType.NONE, new byte[]{}, Void.class);
    }

    public float readUltraSonic(int port) throws IOException {
        Result r = waitForResult(TYPE_READ, DeviceType.ULTRASONIC_SENSOR, new byte[]{(byte) port}, Float.class);
        return (float) r.result;
    }

    public float readLineFollower(int port) throws IOException {
        Result r = waitForResult(TYPE_READ, DeviceType.LINEFOLLOWER, new byte[]{(byte) port}, Float.class);
        return (float) r.result;
    }

    /**
     * @param port light sensor port Internal: Nr. 6
     * @return intensity (0..1024)
     * @throws IOException
     */
    public float readLightSensor(int port) throws IOException {
        byte[] payload = new byte[1];
        payload[0] = (byte) (port & 0xFF);
        Result r = waitForResult(TYPE_READ, DeviceType.LIGHT_SENSOR, payload, Float.class);
        return (float) r.result;
    }

    public float readLightSensorOnboard() throws IOException {
        return readLightSensor(6);
    }

    public float readSoundSensor(int port) throws IOException {
        byte[] payload = new byte[1];
        payload[0] = (byte) (port & 0xFF);
        Result r = waitForResult(TYPE_READ, DeviceType.SOUND_SENSOR, payload, Float.class);
        return (float) r.result;
    }

    public float readPotentiometer(int port) throws IOException {
        byte[] payload = new byte[1];
        payload[0] = (byte) (port & 0xFF);
        Result r = waitForResult(TYPE_READ, DeviceType.POTENTIONMETER, payload, Float.class);
        return (float) r.result;
    }

    public void motorLeft(int speed) throws IOException {
        speed = -speed;
        byte[] payload = new byte[3];
        payload[0] = 0x09; //left motor port
        payload[1] = (byte) (speed & 0xFF);
        payload[2] = (byte) (speed >> 8 & 0xFF);
        Result r = waitForResult(TYPE_WRITE, DeviceType.MOTOR, payload, Void.class);
    }


    public void motorRight(int speed) throws IOException {
        byte[] payload = new byte[3];
        payload[0] = 0x0a; //right motor
        payload[1] = (byte) (speed & 0xFF);
        payload[2] = (byte) (speed >> 8 & 0xFF);
        Result r = waitForResult(TYPE_WRITE, DeviceType.MOTOR, payload, Void.class);
    }

    /**
     * @param idx (1: left led, 2: right led, 0: both)
     * @param r   red (0..255)
     * @param g   green (0..255)
     * @param b   blue(0..255)
     * @throws IOException
     */
    public void rgbLEDOnboard(int idx, int r, int g, int b) throws IOException {
        rgbLED(0x07, idx, r, g, b);
    }

    public void rgbLED(int port, int idx, int r, int g, int b) throws IOException {
        byte[] payload = new byte[6];
        payload[0] = (byte) (port & 0xFF); //port
        payload[1] = (byte) (MbotClient.RGB_INTERNAL_IDX & 0xFF); //slot
        payload[2] = (byte) (idx & 0xFF); //idx
        payload[3] = (byte) (r & 0xFF);
        payload[4] = (byte) (g & 0xFF);
        payload[5] = (byte) (b & 0xFF);
        Result rr = waitForResult(TYPE_WRITE, DeviceType.RGBLED, payload, Void.class);
    }

    public void tone(int hz, int tone_time) throws IOException {
        byte[] payload = new byte[3];
        payload[0] = (byte) (hz & 0xFF);
        payload[1] = (byte) (0);
        payload[2] = (byte) (tone_time & 0xFF);
        Result r = waitForResult(TYPE_WRITE, DeviceType.TONE, payload, Void.class);
    }

    public void ledMatrixString(int port, int x, int y, String s) throws IOException {
        byte[] bstring = s.getBytes();
        byte[] payload = new byte[5 + bstring.length];
        payload[0] = (byte) (port & 0xFF);
        payload[1] = (byte) (0x01);
        payload[2] = (byte) (x);
        payload[3] = (byte) (y);
        payload[4] = (byte) (bstring.length & 0xFF);
        for (int i = 0; i < bstring.length; i++)
            payload[5 + i] = bstring[i];
        Result r = waitForResult(TYPE_WRITE, DeviceType.LEDMATRIX, payload, Void.class);
    }

    public void ledMatrixBitmap(int port, int x, int y, byte[] bitmap) throws IOException {
        byte[] payload = new byte[5 + bitmap.length];
        payload[0] = (byte) (port & 0xFF);
        payload[1] = (byte) (0x02);
        payload[2] = (byte) (x);
        payload[3] = (byte) (y);
        for (int i = 0; i < bitmap.length; i++)
            payload[5 + i] = bitmap[i];

        Result r = waitForResult(TYPE_WRITE, DeviceType.LEDMATRIX, payload, Void.class);
    }

    public byte readIRCmd() {
        byte[] payload = new byte[1];
        payload[0] = 0x00;
        Result r = waitForResult(TYPE_READ, DeviceType.IRREMOTECODE, payload, Byte.class);
        return (byte) r.result;
    }

    public void irSend(String s) {
        byte[] bstring = s.getBytes();
        byte[] payload = new byte[1 + bstring.length];
        payload[0] = (byte) (bstring.length + 3 & 0xFF);
        for (int i = 0; i < bstring.length; i++)
            payload[1 + i] = bstring[i];
        Result r = waitForResult(TYPE_WRITE, DeviceType.IR, payload, Void.class);
    }


    public float readPirMotion(int port) {
        byte[] payload = new byte[1];
        payload[0] = (byte) (port & 0xFF);
        Result r = waitForResult(TYPE_READ, DeviceType.PIRMOTION, payload, Float.class);
        return (float) r.result;
    }

    public float readJoystick(int port, int slot) {
        byte[] payload = new byte[2];
        payload[0] = (byte) (port & 0xFF);
        payload[1] = (byte) (slot & 0xFF);
        Result r = waitForResult(TYPE_READ, DeviceType.JOYSTICK, payload, Float.class);
        return (float) r.result;
    }


    public byte readHumiture(int port, int idx) {
        byte[] payload = new byte[2];
        payload[0] = (byte) (port & 0xFF);
        payload[1] = (byte) (idx & 0xFF);
        Result r = waitForResult(TYPE_READ, DeviceType.HUMITURE, payload, Byte.class);
        return (byte) r.result;
    }

    public float readGyro(int axis) {
        byte[] payload = new byte[2];
        payload[0] = (byte) (0 & 0xFF);
        payload[1] = (byte) (axis & 0xFF);
        Result r = waitForResult(TYPE_READ, DeviceType.GYRO, payload, Float.class);
        return (float) r.result;
    }

    public void stepper(Port port, int maxspeed, int distance) throws IOException {
        byte[] payload = new byte[7];
        payload[0] = (byte) port.getId();
        payload[1] = (byte) (maxspeed & 0xFF);
        payload[2] = (byte) (maxspeed >> 8 & 0xFF);
        payload[3] = (byte) (distance & 0xFF);
        payload[4] = (byte) (distance >> 8 & 0xFF);
        payload[5] = (byte) (distance >> 16 & 0xFF);
        payload[6] = (byte) (distance >> 24 & 0xFF);
        Result r = waitForResult(TYPE_WRITE, DeviceType.STEPPER, payload, Void.class);
    }

    public void servo(Port port, Port slot, int degree)  throws IOException {
        byte[] payload = new byte[3];
        payload[0] = port.getId();
        payload[1] = slot.getId();
        payload[2] = (byte) (degree);
        Result r = waitForResult(TYPE_WRITE, DeviceType.SERVO, payload, Void.class);
    }

    /* -------------------------------------------------------------------------
    functions for the mBot Rover
    ---------------------------------------------------------------------------- */

    /**
     * sets the speed of the right encoder motor to the given parameter
     * was testest and developed for the Mbot Rover of the OMiLAB
     * @param speed of the motor
     * @throws IOException
     */
    public void encoderMotorRight(int speed) throws IOException{
        byte[] payload = new byte[4];
        payload[0] = 0x00; //right motor
        payload[1] = 0x01; //right motor
        payload[2] = (byte) (speed & 0xFF);
        payload[3] = (byte) (speed >> 8 & 0xFF);
        Result r = waitForResult(TYPE_WRITE, DeviceType.ENCODERMOTOR , payload, Void.class);
    }

    /**
     * sets the speed of the left encoder motor to the given parameter
     * was testest and developed for the Mbot Rover of the OMiLAB
     * @param speed of the motor
     * @throws IOException
     */
    public void encoderMotorLeft(int speed) throws IOException{
        byte[] payload = new byte[4];
        payload[0] = 0x00; //left motor
        payload[1] = 0x02; //left motor
        payload[2] = (byte) (speed & 0xFF);
        payload[3] = (byte) (speed >> 8 & 0xFF);
        Result r = waitForResult(TYPE_WRITE, DeviceType.ENCODERMOTOR , payload, Void.class);
    }


    /**
     * let the Auriga board on the rover play a tone with the given parameters
     * @param hz height of the tone
     * @param tone_time length of the tone
     * @throws IOException
     */
    public void toneAuriga(int hz, int tone_time) throws IOException {
        byte[] payload = new byte[4];
        payload[0] = 0x2d;
        payload[1] = (byte) (hz & 0xFF);
        payload[2] = (byte) (0);
        payload[3] = (byte) (tone_time & 0xFF);

        Result r = waitForResult(TYPE_WRITE, DeviceType.TONE, payload, Void.class);
    }


    /**
     * sets the onboard leds of the rover mbot
     * led numbers are from 1 to 12
     * led number 0 sets all led at once
     * @param idx id of the led
     * @param r value for red
     * @param g value for green
     * @param b vaue for blue
     * @throws IOException
     */
    public void rbgLEDAuriga(int idx, int r, int g, int b) throws IOException {
        byte[] payload=new byte[6];
        payload[0] = (byte) (0);
        payload[1] = (byte) (MbotClient.RGB_INTERNAL_IDX & 0xFF); //slot
        payload[2] = (byte) (idx & 0xFF); //idx
        payload[3] = (byte) (r & 0xFF);
        payload[4] = (byte) (g & 0xFF);
        payload[5] = (byte) (b & 0xFF);
        Result rr= waitForResult(TYPE_WRITE, DeviceType.RGBLED,payload,Void.class);
    }

    /**
     * reads the light sensor
     * Sensor numbers:
     *      onboard 1: 12
     *      onboard 2: 11
     *      port 6 - 10: 6 - 10
     * @param port number of the sensor
     * @return float
     * @throws IOException
     */
    public float readLightSensorAuriga(int port) throws IOException {
        byte[] payload = new byte[1];
        payload[0] = (byte) (port & 0xFF);
        Result r = waitForResult(TYPE_READ, DeviceType.LIGHT_SENSOR, payload, Float.class);
        return (float) r.result;
    }


    /**
     * reads the onboard sound sensor and also the sound sensors that can be added to the boards
     *      onboard: 14
     *      port 6 - 10: 6 - 10
     * @param port port number of sensor
     * @return float
     * @throws IOException
     */
    public float readSoundSensorAuriga(int port) throws IOException {
        byte[] payload = new byte[1];
        payload[0] = (byte) (port & 0xFF);
        Result r = waitForResult(TYPE_READ, DeviceType.SOUND_SENSOR, payload, Float.class);
        return (float) r.result;
    }


    /**
     * reads the onboard temperatur sensor of the Auriga board of the rover
     * @return float
     * @throws IOException
     */
    public float readTempSensorOnboardAuriga() throws IOException{
        byte[] payload = new byte[1];
        payload[0] = (byte) (0x0d);
        Result r = waitForResult(TYPE_READ, DeviceType.TEMP_SENSOR_ONBOARD_AURIGA, payload, Float.class);
        return (float) r.result;
    }


    /**
     * reads the onboard gyro sensor of the Auriga board of the rover
     * x-axis: 1 (angle forward and backward)
     * y-axis: 2 (angle sidewards)
     * z-axis: 3 (roatation of the rover)
     * @param axis id auf axis, that should be read
     * @return float
     */
    public float readGyroSensorOnboard(int axis){
        byte[] payload = new byte[2];
        payload[0] = (byte) (0x01); //on board gyro
        payload[1]= (byte) (axis & 0xFF);
        Result r = waitForResult(TYPE_READ, DeviceType.GYRO, payload, Float.class);
        return (float) r.result;
    }

    /**
     * let the Auriga board on the rover play a tone with the given parameters
     * @param linX
     * @param linY
     * @param ang
     * @throws IOException
     */
    public void mecanum(int linX, int linY, int ang) throws IOException {
        byte[] payload = new byte[3];
        payload[0] = (byte) linX;
        payload[1] = (byte) linY;
        payload[2] = (byte) ang;
        Result r = waitForResult(TYPE_WRITE, DeviceType.MECANUM, payload, Void.class);
    }




    /* -------------------------------------------------------------------------
    functions sending and getting information from the mBot, starting the Thread for the communication,...
    ---------------------------------------------------------------------------- */

    private void send(byte type, DeviceType deviceType, byte[] payload) throws IOException {
        byte[] data = new byte[6 + payload.length];
	//6Bytes werden fuer die erkennung gebraucht
        data[0] = (byte) (0xFF);
        data[1] = (byte) (0x55);
        data[2] = (byte) (payload.length + 3);
        data[3] = (byte) (0); //index
        data[4] = (byte) (type); //action
        data[5] = (byte) deviceType.getId(); //device
        System.arraycopy(payload, 0, data, 6, payload.length); //hier wird noch das payload an data drangehaengt
        portOS.write(data);
        portOS.flush();
    }


    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 3];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = hexArray[v >>> 4];
            hexChars[j * 3 + 1] = hexArray[v & 0x0F];
            hexChars[j * 3 + 2] = ',';
        }
        return new String(hexChars);
    }

    private Result waitForResult(byte typeRW, DeviceType deviceType, byte[] payload, Class type) {
        LinkedBlockingQueue<Result> q = new LinkedBlockingQueue<>();
        subscribers.add(q);
        try {
            while (true) {
                send(typeRW, deviceType, payload);
                while (true) {
                    Result r = q.poll(200, TimeUnit.MILLISECONDS);
                    if (r == null)
                        break; //send again ich komm nur aus der einen schleife heraus

                    if (r.result == null) {
                        if (type == Void.class) //falls also nix vom roboter zurueckkommen sollte, aber eine Meldung das er fertig is
                            return r;
                    } else {
                        if (type.isAssignableFrom(r.result.getClass())) {  //pruefen ob eh richtiger typ zurueckkommt, wie abgefragt
                            return r;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            subscribers.remove(q);
        }
    }

    private Result readResponse() throws IOException{
        byte[] sync = new byte[2];
        while (true) {
            sync[0] = sync[1];
            sync[1] = (byte) portIS.read();
            if (Arrays.equals(sync, new byte[]{(byte) 0xFF, (byte) 0x55}))
                break;
        }

        byte[] first = new byte[2];
        first[0] = (byte) portIS.read();
        first[1] = (byte) portIS.read();
        if (Arrays.equals(first, new byte[]{(byte) 0x0d, (byte) 0x0a})) { //OK reply (Motors, ..?)
            Result r = new Result<Void>();
            return r;
        } else if (Arrays.equals(first, new byte[]{(byte) 0x80, (byte) 0x01})) { //device button press
            Result r = new Result<Boolean>();
            r.deviceType = DeviceType.BUTTON_INNER;
            r.result = portIS.read() == 1 ? true : false;
            readSuffix();
            return r;
        } else {
            byte index = first[0];
            byte type = first[1];
            switch (type) {
                case MB_MCU_DATATYPE_FLOAT: {//float
                    byte[] data = new byte[4];
                    portIS.read(data);
                    Result r = new Result<Float>();
                    r.result = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                    r.deviceType = DeviceType.getById(type);
                    readSuffix();
                    return r;
                }
                case MB_MCU_DATATYPE_BYTE: {//byte
                    byte res = (byte) portIS.read();
                    Result r = new Result<Byte>();
                    r.result = res;
                    r.deviceType = DeviceType.getById(type);
                    readSuffix();
                    return r;
                }
                default:
                    log.warn("Unknows Type: " + type);
                    Result r = new Result<Void>();
                    return r;
            }
        }
    }

    private void readSuffix() throws IOException {
        byte[] suffix = new byte[2]; //od 0a
        portIS.read(suffix);
    }

    @Override
    public void run() {
        try {
            while (running) {
                Result r = readResponse();
                for (LinkedBlockingQueue<Result> q : subscribers) {
                    q.put(r);
                }

                for (IMbotEvent s : externalSubscribers) {
                    if (r.deviceType == DeviceType.BUTTON_INNER) {
                        s.onButton((Boolean) r.result);
                    }
                }
            }
        } catch (InterruptedException|IOException e) {
            running = false;
            log.info("Serialport Thread terminating");
        }
    }
}
