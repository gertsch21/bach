package main.mbot.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import purejavacomm.CommPortIdentifier;
import purejavacomm.PortInUseException;
import purejavacomm.SerialPort;
import purejavacomm.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Client library for original mBot firmware
 * Make sure matching mBlock firmware is running on your device (mbot_firmwae.ino, orion_firmware.ino, ...)
 *
 * @author    Martin Kunz <martin.michael.kunz@gmail.com>
 */
public class MDrawBotClient implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(MDrawBotClient.class);

    private final InputStream portIS;
    private final OutputStream portOS;
    private SerialPort serialPort;

    public MDrawBotClient(CommPortIdentifier portid) throws PortInUseException, IOException, UnsupportedCommOperationException {
        serialPort = (SerialPort) portid.open("asdf", 3500);
        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        portIS = serialPort.getInputStream();
        portOS = serialPort.getOutputStream();
        while (portIS.available() != 0) {
            portIS.read();
        }

    }

    public void msleep(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void init() throws IOException {
        msleep(2500);
        System.out.println("init");
        portOS.write("M10\n".getBytes());
        System.out.println(readline());
        System.out.println("M5");
        portOS.write("M5 MSPIDER H590 W1390 0.00 0.00 A0 B0 S15 U1 D91\n".getBytes());
        portOS.flush();
        System.out.println("before readline()");
        System.out.println(readline());
        System.out.println("after readline()");
        portOS.write("M10\n".getBytes());
        portOS.flush();
        System.out.println(readline());
    }

    String readline()
    {
        String ret="";
        for(;;) {
            int a= 0;
            try {
                a = portIS.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(a=='\n')
                break;
            ret+=(char)a;
        }
        return ret;
    }

    public void penUp() throws IOException {
        portOS.write("M1 1024\n".getBytes());
        portOS.flush();
        System.out.println(readline());
    }

    public void penDown() throws IOException {
        portOS.write("M1 0\n".getBytes());
        portOS.flush();
        System.out.println(readline());
    }

    public void moveto(int x, int y, int f) throws IOException {
        byte[] cmd=("G1 X"+x+" Y"+y+" F"+f+"\n").getBytes();
        portOS.write(cmd);
        System.out.println(readline());
    }

    @Override
    public void close() {
        serialPort.close();
    }

}
