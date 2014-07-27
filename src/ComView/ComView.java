/*
 * Projekt:         Robotino Team Solidus
 * Autor:           Steck Manuel
 * Datum:           29.05.2013
 * Geändert:        
 * Änderungsdatum:  
 * Version:         V_1.1.0_Explo
 */
package ComView;

import Tools.SolidusLoggerFactory;
import environmentSensing.EnvironmentSensingFactory;
import environmentSensing.collisionDetection.CollisionDetector;
import java.net.DatagramSocket;
import java.util.Observable;
import java.util.Observer;
import org.apache.log4j.Logger;

public class ComView extends Thread implements Observer
{

    private static Logger MAIN_LOGGER;
    private static Logger SPEEDPERCENT_LOGGER;
    
    private int speedpercentBuffer;
    
    private static ComView instance;
    
    int counter = 0;

    private boolean breakingAllowed;
    private boolean firstTimeZeroPercent, avoidAllowed;

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
    private int breakingFactor = 100;

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
        MAIN_LOGGER = SolidusLoggerFactory.getMain();
        SPEEDPERCENT_LOGGER = SolidusLoggerFactory.getSpeedPercent();
        
        this.speedpercentBuffer = 100;
        
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

        // Listen as observer to collision detection
        EnvironmentSensingFactory.getInstance().getCollisionDetector().addObserver(this);
        
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
                    x, y, phi, station, 0, go, breakingFactor, 0
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
                
            }
            catch (Exception ex)
            {
                MAIN_LOGGER.fatal("Error in comview Thread! Message: " + ex.getMessage());
            }
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException ex)
            {
                MAIN_LOGGER.fatal("Error in comview Thread (sleep)! Message: " + ex.getMessage());
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
        return ready;
    }
    
    public int getEnde()
    {
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
    
    public void setSpeedPercent(int speedPercent)
    {
        if (this.isBreakingAllowed())
        {
            this.breakingFactor = speedPercent;
        }
    }
    
    public int getBreakingFactor()
    {
        return breakingFactor;
    }
    
    public void setBreakingFactor(int breakingFactor)
    {
        this.breakingFactor = breakingFactor;
    }
    
    public boolean isBreakingAllowed()
    {
        return breakingAllowed;
    }
    
    public void setBreakingAllowed(boolean breakingAllowed)
    {
        this.breakingAllowed = breakingAllowed;
        
        if (!breakingAllowed)
        {
            // Breaking not allowed, re-init on 100%
            this.breakingFactor = 100;
            SPEEDPERCENT_LOGGER.debug("DISABLE collision detection");
        }
        else
        {
            this.breakingFactor = speedpercentBuffer;
            SPEEDPERCENT_LOGGER.debug("ENABLE collision detection");
        }
    }

    public boolean isAvoidAllowed()
    {
        return avoidAllowed;
    }

    public void setAvoidAllowed(boolean avoidAllowed)
    {
        this.avoidAllowed = avoidAllowed;
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (((Object) o).getClass() == CollisionDetector.class)
        {
            // Changed speed percent
            SPEEDPERCENT_LOGGER.debug("Observer in comview called. Speed: " + (int) arg + "%");
            this.speedpercentBuffer = (int) arg;
            this.setSpeedPercent(this.speedpercentBuffer);
        } else
        {
            MAIN_LOGGER.error("Unknown observable inform comview!");
        }
    }
}
