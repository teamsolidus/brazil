package Laser.Communication;

import Laser.Interpretation.ICommandListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 *
 * @author simon.buehlmann
 */
public class Communication implements IComReader, IComWriter
{

    //Variablen
    private String ipAdress;
    private int port;

    //Objekte
    private Socket socket;
    private OutputStream request;
    private InputStream response;
    private INewDataByteListener listener;
    private Receiver receiver;
    private Thread thread;

    //Konstruktor
    public Communication(String ipAdress, int port, INewDataByteListener listener) throws IOException
    {
        this.ipAdress = ipAdress;
        this.port = port;

        socket = new Socket(this.ipAdress, this.port);
        request = socket.getOutputStream();
        response = socket.getInputStream();

        this.listener = listener;
        
        this.receiver = new Receiver(response, listener);
        this.thread = new Thread(this.receiver);
        this.thread.start();
    }
    
    //<editor-fold defaultstate="collapsed" desc="IComReader">
    @Override
    public void writeCommand(String command) throws IOException
    {
        //Wandeln String in Command-Byte[]
        byte[] commandReadyToSend = this.interpretToCommand(command);
        
        //Request
        request.write(commandReadyToSend);
        request.flush();
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getter_And_Setter">
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
     * @return ASCII Byte-Array des Strings mit angehängetm & nachfolgenden STX
     * rsp. ETX
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
