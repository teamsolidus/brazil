
package References;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.log4j.FileAppender;
import org.apache.log4j.SimpleLayout;


/**
 *
 * @author simon.buehlmann
 */
public class ReferencesTester
{
    public static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("Laser_Logger");
    
    public static void main(String[] args)
    {
        ReferencePoint p1 = new ReferencePoint(100, 100, 45, AbsoluteReferencePoint.getInstance());
        ReferencePoint p2 = new ReferencePoint(-200, 0, 0, p1);
        ReferencePoint p3 = new ReferencePoint(0, 0, 361, p2);
        
        SimpleLayout layout = new SimpleLayout();
        try
        {
            FileAppender x = new FileAppender(layout, "./logs/laser_log.txt");
            log.addAppender(x);
            // ALL | DEBUG | INFO | WARN | ERROR | FATAL | OFF:
            log.setLevel(org.apache.log4j.Level.DEBUG);

            log.debug("Meine Debug-Meldung");
            log.info("Meine Info-Meldung");
            log.warn("Meine Warn-Meldung");
            log.error("Meine Error-Meldung");
            log.fatal("Meine Fatal-Meldung");
        }
        catch (IOException ex)
        {
            Logger.getLogger(ReferencesTester.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("X: " + p3.getAbsolutX());
        System.out.println("Y: " + p3.getAbsolutY());
        System.out.println("Angle: " + p3.getAbsolutAngle());
    }
}
