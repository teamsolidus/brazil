package environmentSensing.NewLaser.Solidus;

import environmentSensing.positioning.positionEvaluation.IEnvironmentSensor;
import environmentSensing.positioning.positionEvaluation.dummyScenarios.Scenario1;
import java.io.IOException;
import java.util.logging.Level;
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

    //<editor-fold defaultstate="collapsed" desc="SINGLETON">
    private static LaserFactory instance;
    private Logger log;

    private LaserFactory()
    {
        // Initalize Logging
        try
        {
            this.log = org.apache.log4j.Logger.getLogger("Laser_Logger");          //Das Framwork "log4j" wird benutzt zum Loggen
            PatternLayout layout = new PatternLayout("%d{ISO8601} %-5p [%t] %m %n");  //Zeitstempel, Umwandlung, Threadname, Nachricht, Zeilenumbruch
            FileAppender fileAppender = new FileAppender(layout, "logs/laser.log"); // File erzeugen mit entsprechenden Namen
            ConsoleAppender consoleAppender = new ConsoleAppender(layout, "System.out");
            this.log.addAppender(fileAppender);
            this.log.addAppender(consoleAppender);
            this.log.setLevel(org.apache.log4j.Level.ALL);
        }
        catch (IOException ex)
        {
            System.err.println("Not possible to initalize file appender for the laser logger");
        }
        
        // Initalize Laser
        try
        {
            this.laser = new Laser();
        }
        catch (IOException ex)
        {
            Logger.getLogger("Laser_Logger").fatal("Not possible to initalize Laser. Error Message: " + ex.getMessage());
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
//</editor-fold>

    private Laser laser;

    public IEnvironmentSensor getEnvironmentSensor()
    {
        return Scenario1.getInstance();
    }

    public ILaserOperatorSolidus getLaserOperatorSolidus()
    {
        return this.laser;
    }
}
