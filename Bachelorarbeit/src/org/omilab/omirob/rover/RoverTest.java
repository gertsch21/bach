package org.omilab.omirob.rover;

import org.omilab.omirob.mbot.client.MbotClient;
import purejavacomm.CommPortIdentifier;
import purejavacomm.NoSuchPortException;
import purejavacomm.PortInUseException;
import purejavacomm.UnsupportedCommOperationException;

import java.io.IOException;

/**
 * Created by Christian Muck
 */
public class RoverTest {
    static public void main(String[] args) throws Exception{
	System.out.println("Start Rover Test");
	Thread.sleep(2000);
        MbotClient mc= null;
        try {
            //The port names must be looked up for each computer individually.
            // This can be done under the device manager (by windows), for example.
            //The bluetooth connection must be established before calling the program.
            //COM6 --> for connecting via usb
            //COM4 --> for connectin via bluetooth
            // mc = new MbotClient(CommPortIdentifier.getPortIdentifier("COM6"));
            mc = new MbotClient(CommPortIdentifier.getPortIdentifier("COM4"));
            Thread.sleep(0); //that don't have to remove and add the catch part during testing


            /*-------------------------------------------------------
            test moving
            ---------------------------------------------------------- */

           // System.out.println("encoder move");
            /*mc.encoderTest2(-100);
            Thread.sleep(800);
            mc.encoderTest2(0);*/

           /*mc.encoderMotorLeft(150);
           mc.encoderMotorRight(-150);
           Thread.sleep(1000);
           mc.encoderMotorLeft(0);
           mc.encoderMotorRight(0);*/


          /*-------------------------------------------------------
            test buzzer
            ---------------------------------------------------------- */
           /* for(int i=0;i<15;i++) {
                mc.toneAuriga(i*10, 200);
                //Thread.sleep(250);
            }*/



            /*-------------------------------------------------------
            test rbg LEDs
            ---------------------------------------------------------- */

            //mc.rbgLed(8, 1,255, 0, 0);
           // mc.rbgLEDAuriga(0,0,0,0);
            /*for(int i=1;i<=12;i++){
                mc.rbgLEDAuriga(i,0,100,0);
                Thread.sleep(250);
            }*/


            /*-------------------------------------------------------
            test light sensor
            ---------------------------------------------------------- */
            /*for(int i=0;i<15;i++){
                System.out.println("light sensor 1: "+mc.readLightSensorAuriga(12));
                System.out.println("light sensor 2: "+mc.readLightSensorAuriga(11));
                Thread.sleep(300);
            }*/
           // System.out.println("light sensor: "+mc.readLightSensorAuriga(11));


            /*-------------------------------------------------------
            test sound sensor
            ---------------------------------------------------------- */
            /*for(int i=0;i<15;i++){
                System.out.println("Sound sensor: "+mc.readSoundSensorAuriga(14));
                Thread.sleep(250);
            }
*/

           /*-------------------------------------------------------
            test onboard temp sensor
            ---------------------------------------------------------- */
          /* for(int i=0;i<10;i++){
               System.out.println("onboard temp: "+mc.readTempSensorOnboardAuriga());
               Thread.sleep(250);
           }*/


           /*-------------------------------------------------------
            test onboard gyro sensor
            ---------------------------------------------------------- */
           /*for(int i=0;i<10;i++){
               System.out.println("gyro: "+mc.readGyroSensorOnboard(1));
               Thread.sleep(250);
           }*/

           /*-------------------------------------------------------
            test ultrasonic sensor on port 10
            ---------------------------------------------------------- */

          /*  System.out.println("read ultra sonic ");
            for(int i=0;i<10;i++){
                System.out.println("Ultasonic value "+i+": "+mc.readUltraSonic(10));
                Thread.sleep(500);
            }*/


          /*-------------------------------------------------------
            test line following sensor on port 9
            ---------------------------------------------------------- */

            /*System.out.println("line following");
            for(int i=0;i<10;i++){
                System.out.println("linefollow value "+i+": "+mc.readLineFollower(9));
                Thread.sleep(500);
            }*/




        } catch (PortInUseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        } catch (NoSuchPortException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mc.close();
        System.out.println("program finished");
    }
}
