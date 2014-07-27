
package Tools;

import java.io.IOException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * Contains all Logger, so it is easy to get the right logger and set the logger level
 * @author simon.buehlmann
 */
public class SolidusLoggerFactory
{
    // Singleton
    private static SolidusLoggerFactory instance;
    
    // Loggers
    public Logger MAIN_LOGGER;
    public Logger SPEED_PERCENT_LOGGER;
    public Logger MACHINE_LOGGER;
    
    private SolidusLoggerFactory()
    {
        // Layout
         PatternLayout detailedLayout = new PatternLayout("%d{ISO8601} %-5p [%t] %m %n");  //Zeitstempel, Umwandlung, Threadname, Nachricht, Zeilenumbruch
        
         // Appender
         ConsoleAppender consoleAppender = new ConsoleAppender(detailedLayout, "System.out");
         
        //<editor-fold defaultstate="collapsed" desc="MAIN LOGGER">
         this.MAIN_LOGGER  = org.apache.log4j.Logger.getLogger("MAIN_LOGGER");
         this.MAIN_LOGGER.addAppender(consoleAppender);
         this.MAIN_LOGGER.setLevel(org.apache.log4j.Level.ALL);
         
         try
         {
             this.MAIN_LOGGER.addAppender(new FileAppender(detailedLayout, "logs/main.log"));
         }
         catch (IOException ex)
         {
             this.MAIN_LOGGER.error("Not possible to add file appender to MAIN_LOGGER. Message: " + ex.getMessage());
         }
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="SPEED PERCENT">
        this.SPEED_PERCENT_LOGGER  = org.apache.log4j.Logger.getLogger("SPEED_PERCENT_LOGGER");
        //this.SPEED_PERCENT_LOGGER.addAppender(consoleAppender);
        this.SPEED_PERCENT_LOGGER.setLevel(org.apache.log4j.Level.ALL);
        try
        {
            this.SPEED_PERCENT_LOGGER.addAppender(new FileAppender(detailedLayout, "logs/speedpercent.log"));
        }
        catch (IOException ex)
        {
            this.MAIN_LOGGER.error("Not possible to add fileappender to SPEED_PERCENT_LOGGER. Message: " + ex.getMessage());
        }
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="MACHINE LOGGER">
        this.MACHINE_LOGGER  = org.apache.log4j.Logger.getLogger("MACHINE_LOGGER");
        this.MACHINE_LOGGER.addAppender(consoleAppender);
        this.MACHINE_LOGGER.setLevel(org.apache.log4j.Level.ALL);
        try
        {
            this.MACHINE_LOGGER.addAppender(new FileAppender(detailedLayout, "logs/machine.log"));
        }
        catch (IOException ex)
        {
            this.MAIN_LOGGER.error("Not possible to add fileappender to MACHINE_LOGGER. Message: " + ex.getMessage());
        }
//</editor-fold>
    }
    
    public static Logger getMain()
    {
        return getInstance().MAIN_LOGGER;
    }
    
    public static Logger getSpeedPercent()
    {
        return getInstance().SPEED_PERCENT_LOGGER;
    }
    
    public static Logger getMachine()
    {
        return getInstance().MACHINE_LOGGER;
    }
    
    private static SolidusLoggerFactory getInstance()
    {
        if(instance == null)
        {
            instance = new SolidusLoggerFactory();
        }
        return instance;
    }
}
