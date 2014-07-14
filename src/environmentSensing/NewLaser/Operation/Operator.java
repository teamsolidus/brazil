
package Laser.Operation;

import Laser.Interpretation.Command;
import Laser.Communication.IComWriter;
import Laser.Interpretation.ICommandListener;
import java.io.IOException;

/**
 *
 * @author simon.buehlmann
 */
public class Operator extends AbstractContext implements IOperator, ICommandListener
{
    
    private AbstractState singleMeasState, continuousMeasState;
    
    public Operator(IComWriter com) throws IOException
    {
        super();
        
        //Initialize States
        this.singleMeasState = new SingleMeasurementState(this, com);
        this.continuousMeasState = new ContinuousMeasurementState(this, com);
        super.initializeActiveStep(this.singleMeasState);
    }

    //<editor-fold defaultstate="collapsed" desc="IOperator">
    @Override
    public void startContinouousMeasurement() throws Exception
    {
        super.getActiveState().startContinuousMeasurement();
    }
    
    @Override
    public void stopContinuousMeasurement() throws Exception
    {
        this.getActiveState().stopContinuousMeasuremenet();
    }
    
    @Override
    public void runSingleMeasurement() throws Exception
    {
        this.getActiveState().runSingleMeasurement();
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="AbstractContext">
    @Override
    public AbstractState getState(EState state)
    {
        switch(state)
        {
            case CONTINUOUS:
                return this.continuousMeasState;
            case SINGLE:
                return this.singleMeasState;
        }
        return null;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ICommandListener">
    @Override
    public void newData(Command command)
    {
        this.getActiveState().confirmCommand(command);
    }
//</editor-fold>
    
}
