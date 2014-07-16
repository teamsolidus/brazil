
package environmentSensing.collisionDetection;

import Traveling.ICollisionDetection;
import environmentSensing.NewLaser.Solidus.ILaserOperatorSolidus;
import environmentSensing.NewLaser.Solidus.LaserFactory;
import environmentSensing.NewLaser.Solidus.MeasDataSolidus;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author simon.buehlmann
 */
public class CollisionDetector implements ICollisionDetection, Observer
{
    //<editor-fold defaultstate="collapsed" desc="SINGLETON">
    private static CollisionDetector instance;
    
    private CollisionDetector()
    {
        this.FULL_SPEED_LIMIT = 1250;
        this.STOP_LIMIT = 300;
        this.FULL_SPEED_VALUE = 100;
        this.STOP_VALUE = 0;
        
        this.laser = LaserFactory.getInstance().getLaserOperatorSolidus();
        this.laser.addObserver(this);
        
        try
        {
            this.laser.startContinuousMeasurement();
        }
        catch (Exception ex)
        {
            Logger.getLogger(CollisionDetector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static CollisionDetector getInstance()
    {
        if(instance == null)
        {
            instance = new CollisionDetector();
        }
        return instance;
    }
//</editor-fold>

    private ILaserOperatorSolidus laser;
    
    // Configuration
    private final int FULL_SPEED_LIMIT;
    private final int STOP_LIMIT;
    private final int FULL_SPEED_VALUE;
    private final int STOP_VALUE;
    
    // Meas Data Buffer
    private MeasDataSolidus lastMeasData;
    
    
    @Override
    public int evaluateSpeedPercent()
    {
        // Bigger as the fullSpeed limit
        if(this.lastMeasData.getMinDistance() > this.FULL_SPEED_LIMIT)
        {
            return this.FULL_SPEED_VALUE;
        }
        
        // Smaller as the stop limit
        if(this.lastMeasData.getMinDistance() < this.STOP_LIMIT)
        {
            return this.STOP_VALUE;
        }
        
        // Ramp function
        int deltaY = this.FULL_SPEED_VALUE - this.STOP_VALUE;
        int deltaX = this.FULL_SPEED_LIMIT - this.STOP_LIMIT;
        double steigung = ((double)deltaY / (double)deltaX);
        
        return (int)(steigung * (this.lastMeasData.getMinDistance() - this.STOP_LIMIT));
    }

    @Override
    public void update(Observable o, Object arg)
    {
        try
        {
            this.lastMeasData = this.laser.getMeasurementData();
        }
        catch (Exception ex)
        {
            org.apache.log4j.Logger.getLogger("Laser_Logger").fatal("Not possible to get measurement Data from Laser. Error-Message: " + ex.getMessage());
        }
        
        // DEBUG!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //System.out.println("Min. Distance: " + this.lastMeasData.getMinDistance());
        //System.out.println("Speed: " + this.evaluateSpeedPercent() + "%");
    }
}
