
package environmentSensing.NewLaser.Operation;

import environmentSensing.NewLaser.Communication.IComWriter;
import environmentSensing.NewLaser.Interpretation.Command;

/**
 *
 * @author simon.buehlmann
 */
public abstract class AbstractState
{
    private IComWriter com;
    private AbstractContext context;
    
    public AbstractState(AbstractContext context, IComWriter com)
    {
        this.com = com;
        this.context = context;
    }
    
    public AbstractContext getContext()
    {
        return this.context;
    }
    
    public IComWriter getCommunication()
    {
        return this.com;
    }
    
    public abstract void entry();
    
    public abstract void exit();
    
    public abstract void startContinuousMeasurement() throws Exception;
    
    public abstract void stopContinuousMeasuremenet() throws Exception;
    
    public abstract void confirmCommand(Command command);
    
    public abstract void runSingleMeasurement() throws Exception;
    
    public abstract EState getState();
}
