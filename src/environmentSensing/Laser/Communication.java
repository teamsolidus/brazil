/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package environmentSensing.Laser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 *
 * @author simon.buehlmann
 */
public class Communication
{

    //Variablen
    private String ipAdress;
    private int port;

    //Objekte
    private Socket socket;
    //private OutputStream request;
    //private InputStream response;
    private OutputStream request;
    private InputStream response;

    //Konstruktor
    public Communication(String ipAdress, int port) throws IOException
    {
        this.ipAdress = ipAdress;
        this.port = port;

        socket = new Socket(this.ipAdress, this.port);
        request = socket.getOutputStream();
        response = socket.getInputStream();
    }

    //Methoden
    public void singleMeasurement(NewMeasurementListener Listener) throws IOException
    {
        if (socket != null)//Check: Parameter vorhanden?
        {
            //Wandeln String in Command-Byte[]
            byte[] command = this.interpretToCommand("sRN LMDscandata");

            //Request
            request.write(command);
            request.flush();

            //Response
            int temporaryData;//temporäre Daten um auf -1 zu testen (Ende der Daten)
            boolean endOfDataFlag;//true = Ende der Daten im Request-Objekt nicht erreicht, false = Ende 
            
            BufferedReader buff = new BufferedReader(new InputStreamReader(response)); 
            
            do
            {
                Listener.newMeasurementValues((byte) buff.read());
            }
            while(buff.ready());
            //System.out.println("DONE!");
            /*do
            {
            temporaryData = response.read();
            
            endOfDataFlag = !(temporaryData == -1);
            System.out.println(temporaryData);
            if (endOfDataFlag)
            {
            Listener.newMeasurementValues((byte) temporaryData);
            }
            } while (endOfDataFlag);*/
        }
    }

    public void startMeasurement(NewMeasurementListener Listener)
    {

    }

    public void stopMeasurement()
    {

    }

    //<editor-fold defaultstate="collapsed" desc="Getter_And_Setter">
    public void setIpAndPort(String ipAdress, int port)
    {
        this.ipAdress = ipAdress;
        this.port = port;
    }
    
    public String getIpAdress()
    {
        return this.ipAdress;
    }
    
    public int getPort()
    {
        return this.port;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private_Methods">
    /**
     * Interpretiert einen normalen String und gibt ihn als Byte-Array im
     * ASCII-Zeichensatz aus. Ergänzt zudem den String mit einem vorangestellten
     * STX und einem ETX am Schluss. Kann verwendet werden um einen Befehl für
     * den Scanner in dies passende Form zu transformieren.
     *
     * @param interpret String der interpretiert werden soll
     * @return ASCII Byte-Array des Strings mit angehängetm & nachfolgenden STX rsp. ETX
     */
    private byte[] interpretToCommand(String interpret)
    {
        final byte ASCII_STX = 2;
        final byte ASCII_ETX = 3;
        final byte ASCII_BR = 32;
        
        byte[] asciiTemp = interpret.getBytes(Charset.forName("US-ASCII"));
        byte[] ascii = new byte[asciiTemp.length + 2];
        
        for (int n = 1; n < (ascii.length - 1); n++)//Alle ausser erstes & letztes Byte
        {
            ascii[n] = asciiTemp[n - 1];
        }
        
        ascii[0] = ASCII_STX;
        ascii[ascii.length - 1] = ASCII_ETX;
        
        return ascii;
    }
//</editor-fold>

}
