
package environmentSensing;

import environmentSensing.collisionDetection.CollisionDetector;
import environmentSensing.gui.LaserFrame;

/**
 *
 * @author simon.buehlmann
 */
public class EnvironmentSensingFactory
{
    private CollisionDetector collisionDetector;
    private LaserFrame laserFrame;
    
    //<editor-fold defaultstate="collapsed" desc="SINGLETON">
    private static EnvironmentSensingFactory instance;
    public static EnvironmentSensingFactory getInstance()
    {
        if(instance == null)
        {
            instance = new EnvironmentSensingFactory();
        }
        return instance;
    }
//</editor-fold>
    
    private EnvironmentSensingFactory()
    {
        this.collisionDetector = CollisionDetector.getInstance();
    }
    
    public EnvironmentSensingFactory startGui()
    {
        if(this.laserFrame == null)
        {
            laserFrame = new LaserFrame();
        }
        return this;
    }
    
    public CollisionDetector getCollisionDetector()
    {
        return this.collisionDetector;
    }
    
}
