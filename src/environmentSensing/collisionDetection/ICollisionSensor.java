
package environmentSensing.collisionDetection;

import java.util.Observer;

/**
 *
 * @author simon.buehlmann
 */
public interface ICollisionSensor
{
    public ICollisionReflections getCollisionReflections();
    
    public void addObserver(Observer obs);
    
    public void deleteObserver(Observer obs);
}
