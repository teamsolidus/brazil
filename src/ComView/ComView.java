/*
 * Projekt:         Robotino Team Solidus
 * Autor:           Steck Manuel
 * Datum:           29.05.2013
 * Geändert:        
 * Änderungsdatum:  
 * Version:         V_1.1.0_Explo
 */
package ComView;

import MainPack.Main;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;

public class ComView extends Thread
{

    private static ComView instance;

    int counter = 0;

    // UDP - Socket    
    UDPServer com;
    DatagramSocket serverSocket;

    // Speicher für lesende Nachricht
    // int[] msg;
    // Senden
    private int x;                      // X - Koordinate
    private int y;                      // Y - Koordinate
    private int phi;                    // Winkel
    private int check;                  // Kontrollbit
    private int station;                // Stationsanfahren
    private int go;                     // Empfangsbestätigung 
    private int phase;                  // Spielphase
    public int breakingFactor =100;

    // Empfangen
    int ready;                          // Step ausgeführt
    int red;                            // Zustand rote Lampe
    int orange;                         // Zustand orange Lampe
    int green;                          // Zustand güne Lampe
    int ende;                           // Empfangsbestätigung
    int xAktuell;                       // Aktuelle x Koordinate der Odometrie(inkremental)
    int yAktuell;                       // Aktuelle y Koordinate der Odometrie(inkremental)

    // Schleifenbedingung
    public boolean run = true;

    public static ComView getInstance()
    {
        if (instance == null)
        {
            instance = new ComView();
        }
        return instance;
    }

    public ComView()
    {
        // this.laser = laser;
        // Reverentieren
        com = new UDPServer();
     

        // msg = new int[10];
        x = 0;
        y = 0;
        phi = 0;
        check = 0;
        station = 0;
        go = 0;
        phase = 0;

        ready = 0;
        red = 0;
        orange = 0;
        green = 0;
        ende = 0;
        xAktuell = 0;
        yAktuell = 0;
    }

    @Override
    public void run()
    {
        while (run == true)
        {
            try
            {
                serverSocket = new DatagramSocket(5000);
                int[] sendKoor =
                {
                    x, y, phi, station, 0, go,breakingFactor , 0
                };

                com.sendViewMessage(sendKoor, "127.0.0.1", 5001);

                int[] msg = com.getViewMessagr(serverSocket);
                ready = msg[0];
                red = msg[1];
                orange = msg[2];
                green = msg[3];
                ende = msg[4];
                xAktuell = msg[5];
                yAktuell = msg[6];
                serverSocket.close();

            } catch (Exception ex)
            {
                Main.log.error(ex);
            }
            try
            {
                Thread.sleep(100);
            } catch (InterruptedException ex)
            {
                Logger.getLogger(ComView.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        }
    }

    public void setGo(int go)
    {
        this.go = go;
    }

    /**
     * Gibt die zuletzt gelesenen Lampenfarben zurück: rot,orange,grün 0 = aus,
     * 1 = leuchtet, 2 = blinkt
     *
     * @return
     */
    public int[] getLamp()
    {
        int[] lamp =
        {
            red, orange, green
        };
        return lamp;
    }

    public int getReady()
    {
        // System.out.println(">>>>>>>>>>>>>>> GET NOW READY <<<<<<<<<<<<<<");
        return ready;
    }

    public int getEnde()
    {

        //  System.out.println("Ende wurde empfagnen: " + ende);
        return ende;
    }

    /**
     * Gibt befehl die Station anzufahren 1/0
     *
     * @return
     */
    public void setStation(int go)
    {

        station = go;
        System.out.println(station);

    }

    public void setKoords(int x, int y, int phi)
    {

        this.x = x;

        this.y = y;

        this.phi = phi;

    }

    public int getPhase()
    {
        return phase;
    }

    public void setPhase(int phase)
    {
        this.phase = phase;
    }

    public int getxAktuell()
    {
        return xAktuell;
    }

    public int getyAktuell()
    {
        return yAktuell;
    }

    public static void main(String[] args) throws IOException
    {
        // TiM55x_Solidus laser = new TiM55x_Solidus();
        ComView com = new ComView();
        com.start();
    }

}
