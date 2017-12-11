package drawbot;

import purejavacomm.CommPortIdentifier;
import purejavacomm.NoSuchPortException;
import purejavacomm.PortInUseException;
import purejavacomm.UnsupportedCommOperationException;

import java.io.IOException;

import mbot.client.MDrawBotClient;

/**
 * This class was created to test the function of the MDrawBotClient and to show how the functions can be called.
 *
 * Created by Christian Muck
 *
 */
public class DrawBotTest {
    static public void main(String[] args){

        //port identifier must be changed to the used port of the computer
        try(MDrawBotClient mc=new MDrawBotClient(CommPortIdentifier.getPortIdentifier("COM3"))) {
            mc.init();



        System.out.println("put the pen down");
          mc.penDown();

        /*  System.out.println("put the pen up");
          mc.penUp();*/

          // f is the speed
            //By drawing longer lines it is possible that the become curved, even if the should be straight.
            //In that case the line should be splited into more shorter ones to draw the line straight.
          System.out.println("move the draw bot to a point");
          mc.moveto(0,0,10);

            try {
                // Thread.sleep(18000);

                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mc.close();

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

}
