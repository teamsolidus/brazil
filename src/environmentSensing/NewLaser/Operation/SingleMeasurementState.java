
package environmentSensing.NewLaser.Operation;

import environmentSensing.NewLaser.Communication.IComWriter;
import environmentSensing.NewLaser.Interpretation.Command;

/**
 *
 * @author simon.buehlmann
 */
public class SingleMeasurementState extends AbstractState
{
    public SingleMeasurementState(AbstractContext context, IComWriter com)
    {
        super(context, com);
    }
    
    @Override
    public void startContinuousMeasurement()
    {
        AbstractState temp = super.getContext().getState(EState.CONTINUOUS);
        super.getContext().setState(temp);
    }

    @Override
    public void stopContinuousMeasuremenet() throws Exception
    {
        throw new Exception("State flow exception: You are in the single measurement state and have tried to stop the continious measurement");
    }

    @Override
    public void runSingleMeasurement() throws Exception
    {
        super.getCommunication().writeCommand("sRN LMDscandata");
    }

    @Override
    public EState getState()
    {
        return EState.SINGLE;
    }

    @Override
    public void entry()
    {
    }

    @Override
    public void exit()
    {
    }

    @Override
    public void confirmCommand(Command command)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
