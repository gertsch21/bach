package mbot;

import purejavacomm.CommPortIdentifier;
import purejavacomm.NoSuchPortException;
import purejavacomm.PortInUseException;
import purejavacomm.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.Enumeration;

import mbot.client.IMbotEvent;
import mbot.client.MbotClient;
import mbot.client.Port;

/**
 * Simple test program fir mBot client library;
 * prints available com ports, modify @comPortName accordingly.
 * Make sure matching mBlock firmware is running on your device (mbot_firmwae.ino, orion_firmware.ino, ...)
 *
 * @author    Martin Kunz <martin.michael.kunz@gmail.com>
 */
public class Main implements IMbotEvent {
    private static String comPortName="COM7";
    public static void main(String [ ] args)
    {
	System.out.println("Main gestartet");
        if(args.length>0)
            comPortName=args[0];
        new Main();
    }

    Main(){
        printCommPorts();
        System.out.println("Using comport: \""+comPortName+"\"");

        try(MbotClient mc=new MbotClient(CommPortIdentifier.getPortIdentifier(comPortName))) {
            mc.addListener(this);
            mc.reset();
            ledDemo(mc,10);
            // mc.mecanum(0,0,0);
            //soundDemo(mc);
            //gyroDemo(mc);
            //irRemoteDemo(mc);
            //drawBotDemo(mc);
        } catch (PortInUseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        } catch (NoSuchPortException e) {
            e.printStackTrace();
        }
    }

    private void gyroDemo(MbotClient mc){
        while(true) {
            float x=mc.readGyro(1);
            float y=mc.readGyro(2);
            float z=mc.readGyro(3);
            System.out.println(String.format("%.2f %.2f %.2f", x, y, z));
        }
    }
    private void ledDemo(MbotClient mc, int seconds) throws IOException {
        System.out.println("led demo");
    	for(int i=0;i<seconds*2;i++) {
            mc.rgbLEDOnboard(1, 255, 0, 0);
            mc.rgbLEDOnboard(2, 0, 0, 255);
            trySleep(250);
            mc.rgbLEDOnboard(2, 255, 0, 0);
            mc.rgbLEDOnboard(1, 0, 0, 255);
            trySleep(250);
        }
    }

    private void trySleep(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void soundDemo(MbotClient mc) throws IOException {
        while(true) {
            float lightIntens=mc.readLightSensorOnboard();
            mc.tone((int) (lightIntens),15);
            System.out.println(String.format("Light intensity: %f", lightIntens));
        }
    }

    private void irRemoteDemo(MbotClient mc) throws IOException {
        boolean led0On=false;
        boolean led1On=false;

        while(true) {
            byte a = mc.readIRCmd();
            switch (a) {
                case MbotClient.REMOTE_D:
                    mc.motorLeft(100);
                    break;
                case MbotClient.REMOTE_LEFT:
                    mc.motorLeft(0);
                    break;
                case MbotClient.REMOTE_0:
                    mc.motorLeft(-100);
                    break;
                case MbotClient.REMOTE_E:
                    mc.motorRight(100);
                    break;
                case MbotClient.REMOTE_RIGHT:
                    mc.motorRight(0);
                    break;
                case MbotClient.REMOTE_F:
                    mc.motorRight(-100);
                    break;
                case MbotClient.REMOTE_OK:
                    mc.motorLeft(0);
                    mc.motorRight(0);
                    break;
                case MbotClient.REMOTE_A:
                    led0On = !led0On;
                    if (led0On)
                        mc.rgbLEDOnboard(2, 255, 255, 255);
                    else
                        mc.rgbLEDOnboard(2, 0, 0, 0);
                    break;
                case MbotClient.REMOTE_C:
                    led1On = !led1On;
                    if (led1On)
                        mc.rgbLEDOnboard(1, 255, 255, 255);
                    else
                        mc.rgbLEDOnboard(1, 0, 0, 0);
                    break;
                case MbotClient.REMOTE_1:
                    mc.tone(200, 1);
                    break;
                case MbotClient.REMOTE_2:
                    mc.ledMatrixString(4, -1, 8, "ABC");
                    break;
                case MbotClient.REMOTE_3:
                    byte[] bitmap = new byte[7];
                    bitmap[0] = (byte) (0b11111111 & 0xFF);
                    bitmap[1] = (byte) (0b10000001 & 0xFF);
                    bitmap[2] = (byte) (0b10000001 & 0xFF);
                    bitmap[3] = (byte) (0b10000001 & 0xFF);
                    bitmap[4] = (byte) (0b10010001 & 0xFF);
                    bitmap[5] = (byte) (0b10000001 & 0xFF);
                    bitmap[6] = (byte) (0b11111111 & 0xFF);
                    mc.ledMatrixBitmap(4, -1, 0, bitmap);

                    bitmap[2] = (byte) (0b10010001 & 0xFF);
                    bitmap[4] = (byte) (0b10000001 & 0xFF);
                    mc.ledMatrixBitmap(4, 8, 0, bitmap);
                    break;
                case MbotClient.REMOTE_4:
                    mc.irSend("asdasd");
                    break;
                case MbotClient.REMOTE_5:
                    float val = mc.readPirMotion(1);
                    System.out.println(val);
                    break;
                case MbotClient.REMOTE_6:
                    float lfVal = mc.readLineFollower(2);
                    System.out.println(lfVal);
                    break;
                case MbotClient.REMOTE_7:
                    float pVal = mc.readPotentiometer(3);
                    System.out.println(pVal);
                    break;
                case MbotClient.REMOTE_8:
                    float xVal = mc.readJoystick(3, 1);
                    float yVal = mc.readJoystick(3, 2);
                    System.out.println("x:" + xVal + ", y: " + yVal);
                    break;
                case MbotClient.REMOTE_9:
                    mc.rgbLED(3, 1, 25, 25, 25);
                    mc.rgbLED(3, 2, 255, 0, 0);
                    mc.rgbLED(3, 3, 0, 255, 0);
                    mc.rgbLED(3, 4, 0, 0, 255);
                    break;
                case MbotClient.REMOTE_B:
                    float lVal = mc.readHumiture(1, 6);
                    System.out.println(lVal);
                default:
            }
        }
    }

    private void drawBotDemo(MbotClient mc) throws IOException {
        mc.stepper(Port.PORT_1,90,100);
        mc.stepper(Port.PORT_2,90,100);
        mc.servo(Port.PORT_3, Port.SLOT1,0);
        trySleep(1000);
        mc.servo(Port.PORT_3, Port.SLOT1,90);
        trySleep(1000);
        mc.servo(Port.PORT_3, Port.SLOT1,180);
        trySleep(1000);
    }

    private void printCommPorts(){
        Enumeration<CommPortIdentifier> identifiers = CommPortIdentifier.getPortIdentifiers();
        System.out.println("Detected com ports:");
        while(identifiers.hasMoreElements()){
            CommPortIdentifier id = (CommPortIdentifier) identifiers.nextElement();
            System.out.println(String.format("Name: \"%s\", Type: %s, Owner: %s",id.getName(), id.getPortType(), id.isCurrentlyOwned()?id.getCurrentOwner():"-"));
        }
        System.out.println();
    }

    @Override
    public void onButton(boolean pressed) {
        System.out.println("button" +pressed);

    }
}
