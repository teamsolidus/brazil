package environmentSensing.NewLaser.Solidus;

import environmentSensing.collisionDetection.ICollisionSensor;
import environmentSensing.positioning.positionEvaluation.IEnvironmentSensor;
import environmentSensing.positioning.positionEvaluation.dummyScenarios.Scenario1;
import java.io.IOException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.FileAppender;
import org.apache.log4j.PatternLayout;

/**
 *
 * @author simon.buehlmann
 */
public class LaserFactory
{
    private static LaserFactory instance;
    private static Logger log;

    private LaserFactory()
    {
        // Initalize Logging
        try
        {
            log = org.apache.log4j.Logger.getLogger("Laser_Logger");          //Das Framwork "log4j" wird benutzt zum Loggen
            PatternLayout layout = new PatternLayout("%d{ISO8601} %-5p [%t] %m %n");  //Zeitstempel, Umwandlung, Threadname, Nachricht, Zeilenumbruch
            FileAppender fileAppender = new FileAppender(layout, "logs/laser.log"); // File erzeugen mit entsprechenden Namen
            ConsoleAppender consoleAppender = new ConsoleAppender(layout, "System.out");
            log.addAppender(fileAppender);
            log.addAppender(consoleAppender);
            log.setLevel(org.apache.log4j.Level.ALL);
        }
        catch (IOException ex)
        {
            System.err.println("Not possible to initalize file appender for the laser logger");
        }
        
        log.debug("Laser Factory created");
        
        // Initalize Laser
        try
        {
            this.laser = new Laser();
            this.laser.startContinuousMeasurement();
        }
        catch (IOException ex)
        {
            Logger.getLogger("Laser_Logger").fatal("Not possible to initalize Laser. Error Message: " + ex.getMessage());
        }
        catch (Exception ex)
        {
            Logger.getLogger("Laser_Logger").fatal("Not possible to start Laser. Error Message: " + ex.getMessage());
        }
    }

    public static LaserFactory getInstance()
    {
        if (instance == null)
        {
            instance = new LaserFactory();
        }
        return instance;
    }

    private Laser laser;

    public Laser getLaser()
    {
        return this.laser;
    }
    
    public IEnvironmentSensor getEnvironmentSensor()
    {
        return Scenario1.getInstance();
    }
    
    public ICollisionSensor getCollisionSensor()
    {
        return this.laser;
    }

    public ILaserOperatorSolidus getLaserOperatorSolidus()
    {
        return this.laser;
    }
    
    public static void main(String[] args)
    {
        Laser laser = LaserFactory.getInstance().getLaser();
        // GUIControllerSolidus controller = new GUIControllerSolidus(laser);
    }
}
