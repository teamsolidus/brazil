
package environmentSensing.NewLaser.Solidus;

import java.util.Observer;

/**
 *
 * @author simon.buehlmann
 */
public interface ILaserOperatorSolidus
{
    public void startContinuousMeasurement() throws Exception;
    
    public void stopContinuousMeasurement() throws Exception;
    
    public void addObserver(Observer Obs);
}
