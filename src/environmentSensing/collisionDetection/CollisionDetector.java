package environmentSensing.collisionDetection;

import Tools.SolidusLoggerFactory;
import environmentSensing.NewLaser.Solidus.LaserFactory;
import java.awt.Point;
import java.util.Observable;
import java.util.Observer;
import org.apache.log4j.Logger;

/**
 *
 * @author simon.buehlmann
 */
public class CollisionDetector extends Observable implements Observer
{
    //<editor-fold defaultstate="collapsed" desc="SINGLETON">
    private static CollisionDetector instance;
    private static Logger speedpercentLogger;

    public static CollisionDetector getInstance()
    {
        if (instance == null)
        {
            instance = new CollisionDetector();
        }
        return instance;
    }
//</editor-fold>

    // Objects
    private ICollisionSensor laser;
    private SpeedPercentCalculaterReducedJump SpeedCalc;

    // Configuration
    private final int FULL_SPEED_LIMIT;
    private final int STOP_LIMIT;
    private final int FULL_SPEED_VALUE;
    private final int STOP_VALUE;

    /**
     * For informing the observer only when something changed
     */
    private int lastCalculatetPercent;

    private MonitorArea monitorArea;
    
     private CollisionDetector()
    {
        speedpercentLogger = SolidusLoggerFactory.getSpeedPercent();
        
        this.lastCalculatetPercent = 0;

        this.FULL_SPEED_LIMIT = 800;
        this.STOP_LIMIT = 240;
        this.FULL_SPEED_VALUE = 100;
        this.STOP_VALUE = 0;
        
        this.SpeedCalc = new SpeedPercentCalculaterReducedJump(this.FULL_SPEED_LIMIT - this.STOP_LIMIT);
        this.laser = LaserFactory.getInstance().getCollisionSensor();
        this.laser.addObserver(this);

        this.monitorArea = new MonitorArea(500, this.FULL_SPEED_LIMIT);
    }

    public int evaluateSpeedPercent(Point closestReflection)
    {
        if (closestReflection == null)
        {
            // No Point in Area
            return this.FULL_SPEED_VALUE;
        }
        //System.out.println("Distance: " + closestPointInArea.y);

        // Bigger as the fullSpeed limit
        if (closestReflection.y > this.FULL_SPEED_LIMIT)
        {
            return this.FULL_SPEED_VALUE;
        }

        // Smaller as the stop limit
        if (closestReflection.y < this.STOP_LIMIT)
        {
            return this.STOP_VALUE;
        }

        // Ramp function
        int deltaY = this.FULL_SPEED_VALUE - this.STOP_VALUE;
        int deltaX = this.FULL_SPEED_LIMIT - this.STOP_LIMIT;
        double steigung = ((double) deltaY / (double) deltaX);
        int tempSpeedPercent = (int) (steigung * (closestReflection.y - this.STOP_LIMIT));

        return tempSpeedPercent;
    }
    
    public int temp()
    {
        Point closestPointInArea = this.monitorArea.getClosestCoordToYInArea(this.laser.getCollisionReflections().getReflections());
        
        int tempReturn;
        
        // No Point in Area?
        if(closestPointInArea == null)
        {
            tempReturn = this.SpeedCalc.calculate(this.FULL_SPEED_LIMIT - this.STOP_LIMIT);
        }
        else
        {
            tempReturn = this.SpeedCalc.calculate(closestPointInArea.y - this.STOP_LIMIT);
        }
        
        // Validate
        if(tempReturn > 100)
        {
            tempReturn = 100;
        }
        else if(tempReturn < 0)
        {
            tempReturn = 0;
        }
        
        return tempReturn;
    }
    
    @Override
    public void update(Observable o, Object arg)
    {
        // Check: Change?
        Point closestPointInArea = this.monitorArea.getClosestCoordToYInArea(this.laser.getCollisionReflections().getReflections());
        int tempSpeedpercent = this.evaluateSpeedPercent(closestPointInArea);

        //int tempSpeedpercent = this.temp();
        
        if (this.lastCalculatetPercent != tempSpeedpercent)
        {
            String logMsg = "Collision detector inform observers over new speedpercent. Percent: " + tempSpeedpercent;
            if(closestPointInArea != null)
            {
                logMsg = logMsg + " Distance: " + closestPointInArea.y + "mm";
            }
            else
            {
                logMsg = logMsg + " (No Reflection in monitor area)";
            }
            speedpercentLogger.debug(logMsg);
            
            // inform about changed value
            this.setChanged();
            this.notifyObservers(tempSpeedpercent);
            this.lastCalculatetPercent = tempSpeedpercent;
        }
    }
}
