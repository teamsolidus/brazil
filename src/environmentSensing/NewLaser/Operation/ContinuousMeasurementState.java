
package Laser.Operation;

import Laser.Communication.IComWriter;
import Laser.Interpretation.Command;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author simon.buehlmann
 */
public class ContinuousMeasurementState extends AbstractState
{
    public ContinuousMeasurementState(AbstractContext context, IComWriter com)
    {
        super(context, com);
    }
    
    @Override
    public void startContinuousMeasurement() throws Exception
    {
        throw new Exception("Continuous measurement state is allready active");
    }

    @Override
    public void stopContinuousMeasuremenet()
    {
        AbstractState temp = super.getContext().getState(EState.SINGLE);
        super.getContext().setState(temp);
    }

    @Override
    public void runSingleMeasurement() throws Exception
    {
        throw new Exception("Continuous Measurement state is active");
    }

    @Override
    public EState getState()
    {
        return EState.CONTINUOUS;
    }

    @Override
    public void entry()
    {
        try
        {
            super.getCommunication().writeCommand("sEN LMDscandata 1");
        }
        catch (IOException ex)
        {
            Logger.getLogger(ContinuousMeasurementState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void exit()
    {
        try
        {
            super.getCommunication().writeCommand("sEN LMDscandata 0");
        }
        catch (IOException ex)
        {
            Logger.getLogger(ContinuousMeasurementState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void confirmCommand(Command command)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
