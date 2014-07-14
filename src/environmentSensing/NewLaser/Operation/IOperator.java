
package Laser.Operation;


/**
 *
 * @author simon.buehlmann
 */
public interface IOperator
{
    public void startContinouousMeasurement() throws Exception;
    
    public void stopContinuousMeasurement() throws Exception;
    
    public void runSingleMeasurement() throws Exception;
}
