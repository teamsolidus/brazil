


package environmentSensing.positioning;

import environmentSensing.positioning.*;
import static MainPack.Main.log;
import References.ReferencePoint;
import Traveling.IPositioning;
import java.io.IOException;
import org.apache.log4j.*;

/**
 *
 * @author alwin.mani
 */

public class OptimizationPosition implements IPositioning     //Odometrie und laserposition werden in dieser Klasse verglichen, um über weite Strecken Fahrfehler zu vermeiden.
{

    ILaserPositionEvaluator laser;
    ReferencePoint ref;

    private int tolX;
    private int tolY;
    private int tolAngle;

    //Logging
    private Logger log;

    public OptimizationPosition()
    {

        try
        {
            this.log = org.apache.log4j.Logger.getLogger("Position_Logger");          //Das Framwork "log4j" wird benutzt zum Loggen
            PatternLayout layout = new PatternLayout("%d{ISO8601} %-5p [%t] %m %n");  //Zeitstempel, Umwandlung, Threadname, Nachricht, Zeilenumbruch
            FileAppender fileAppender = new FileAppender(layout, "position_log.txt"); // File erzeugen mit entsprechenden Namen
            this.log.addAppender(fileAppender);
            this.log.setLevel(Level.ALL);         // Hier kann man das Level wählen, z.B DEBUG, ERROR,INFO,FATAL,OFF,TRACE,WARN
        } catch (IOException ex)
        {
            java.util.logging.Logger.getLogger(OptimizationPosition.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    }

    @Override
    public ReferencePoint correctPosition(ReferencePoint vaguePosition)
    {
        this.log.info("Correct Position call");

        this.ref = vaguePosition;

        this.tolX = 200;
        this.tolY = 200;
        this.tolAngle = 10;

        try                //found a Reference?
        {
            DTOPosition currentLaserPos = laser.evaluatePosition(ref);

            switch (currentLaserPos.getReferenceCase())
            {
                // In diesen Schritt fällt man, wenn man vom Laser X, Y und den Winkel erhält.
                case ALL_REFERENCES:    //Die Lasermessungen werden mit den Odometriedaten verglichen.  tolX und tolY sind die gültigen Messdifferenzen 
                    if (currentLaserPos.getX() > ref.getX() + tolX || currentLaserPos.getX() < ref.getX() - tolX //x Achse
                        && currentLaserPos.getY() > ref.getY() + tolY || currentLaserPos.getY() < ref.getY() - tolY //y Achse
                        && currentLaserPos.getAngle() > ref.getAngle() + tolAngle || currentLaserPos.getAngle() < ref.getAngle() - tolAngle)  //Winkel
                    {
                        ReferencePoint temp = new ReferencePoint(currentLaserPos.getX(), currentLaserPos.getY(), currentLaserPos.getAngle(), currentLaserPos.getReference());

                        this.log.info("Position corrected with meas-values. \nOdometrie-Values: X" + vaguePosition.getAbsolutX()
                            + " Y" + vaguePosition.getAbsolutY()
                            + " ANGLE" + vaguePosition.getAbsolutAngle() + "\nLaser-Values: X" + temp.getAbsolutX()
                            + " Y" + temp.getAbsolutY()
                            + " ANGLE" + temp.getAbsolutAngle());

                        return temp;  //Wenn die Differenz grenze nicht überschritten wird, wird an die Drive Class die genauere Laserposition zurückgegeben.
                    }
                    break;

                //Der Laser kann den Winkel, den der Roboter hat, auch ausrechnen wenn er nur eine Bande sieht.
                case X_REFERENCE:   //wenn von Laser nur X Achse und Winkel ermittelt werden konnte.
                    if (currentLaserPos.getX() > ref.getX() + tolX || currentLaserPos.getX() < ref.getX() - tolX //x Achse
                        && currentLaserPos.getAngle() > ref.getAngle() + tolAngle || currentLaserPos.getAngle() < ref.getAngle() - tolAngle) //Winkel
                    {
                        ReferencePoint temp = new ReferencePoint(currentLaserPos.getX(), currentLaserPos.getY(), currentLaserPos.getAngle(), currentLaserPos.getReference());

                           this.log.info("Position corrected with meas-values. \nOdometrie-Values: X" + vaguePosition.getAbsolutX()
                            + " Y" + vaguePosition.getAbsolutY()
                            + " ANGLE" + vaguePosition.getAbsolutAngle() + "\nLaser-Values: X" + temp.getAbsolutX()
                            + " Y" + temp.getAbsolutY()
                            + " ANGLE" + temp.getAbsolutAngle());


                        return temp;   //Wenn die Differenz grenze nicht überschritten wird, wird an die Drive Class die genauere Laserposition zurückgegeben.
                    }
                    break;

                case Y_REFERENCE:   //wenn von Laser nur Y Achse und Winkel ermittelt werden konnte.
                    if (currentLaserPos.getY() > ref.getY() + tolY || currentLaserPos.getY() < ref.getY() - tolY  //y Achse
                        && currentLaserPos.getAngle() > ref.getAngle() + tolAngle || currentLaserPos.getAngle() < ref.getAngle() - tolAngle) //Winkel
                    {
                        ReferencePoint temp = new ReferencePoint(currentLaserPos.getX(), currentLaserPos.getY(), currentLaserPos.getAngle(), currentLaserPos.getReference());

                             this.log.info("Position corrected with meas-values. \nOdometrie-Values: X" + vaguePosition.getAbsolutX()
                            + " Y" + vaguePosition.getAbsolutY()
                            + " ANGLE" + vaguePosition.getAbsolutAngle() + "\nLaser-Values: X" + temp.getAbsolutX()
                            + " Y" + temp.getAbsolutY()
                            + " ANGLE" + temp.getAbsolutAngle());


                        return temp;  //Wenn die Differenz Grenze nicht überschritten wird, wird an die Drive Class die genauere Laserposition zurückgegeben.
                    }
                    break;
            }
            //Falls der Laser keine Wand sieht wird an die Drive Class der Odometriewert unverändert zurückgeschickt
        } catch (NoReferenceException ex)
        {

            return ref;
        }
        return ref;
    }

    public static void main(String[] args)
    {
        OptimizationPosition t = new OptimizationPosition();
    }
}
