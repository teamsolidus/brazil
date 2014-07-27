package environmentSensing.gui;

import environmentSensing.NewLaser.Solidus.Laser;
import environmentSensing.gui.views.LaserView;
import environmentSensing.collisionDetection.CollisionDetector;
import environmentSensing.gui.views.percentDiagramm.IPercentDataProvider;
import java.util.Observable;
import java.util.Observer;
import org.apache.log4j.Logger;

/**
 *
 * @author simon.buehlmann
 */
public class LaserViewController implements IPercentDataProvider, Observer
{
    //Logger
    private static Logger log;
    
    private LaserView view;
    private Laser laser;

    private byte speedPercentBuffer;

    public LaserViewController(CollisionDetector detector, Laser laser, LaserView view)
    {
        if(log == null)
            log = Logger.getLogger("Laser_Logger");
        
        this.speedPercentBuffer = 0;

        detector.addObserver(this);
        this.laser = laser;
        this.laser.addObserver(this);
        
        this.view = view;
    }

    @Override
    public byte getSpeedPercent()
    {
        return this.speedPercentBuffer;
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if (((Object) o).getClass() == CollisionDetector.class)
        {
            synchronized (this)
            {
                // Changed speed percent
                Integer temp = (Integer)arg;
                this.speedPercentBuffer = temp.byteValue();
                this.view.setSpeedPercent(speedPercentBuffer);
                
                //log.debug("Laser view controller new speed percent data");
            }
        }
        else if(((Object) o).getClass() == Laser.class)
        {
            this.view.setReflectionPoints(this.laser.getCoordinatesData().getDistance(90, 180));
            
            //log.debug("Laser view controller new speed reflection points data");
        }
        else
        {
            log.error("Laser view controller informed from unknown observable. Class: " + ((Object) o).getClass());
        }
    }
}
