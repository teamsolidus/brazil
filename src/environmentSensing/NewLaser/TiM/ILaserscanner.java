
package environmentSensing.NewLaser.TiM;

import environmentSensing.NewLaser.Data.IDataProvider;
import java.util.Observer;

/**
 *
 * @author simon.buehlmann
 */
public interface ILaserscanner
{
    public IDataProvider<Integer> synchRunSingleMeas() throws Exception;
    
    public void asynchRunSingleMeas() throws Exception;
    
    public void startContinuousMeas() throws Exception;
    
    public void stopContinuousMeas() throws Exception;
    
    public IDataProvider<Integer> getData() throws Exception;
    
    public void addObserver(Observer obs);
}
