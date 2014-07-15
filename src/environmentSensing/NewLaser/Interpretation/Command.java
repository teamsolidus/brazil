
package environmentSensing.NewLaser.Interpretation;

import Laser.Interpretation.ECommand;
import environmentSensing.NewLaser.Data.Data;
import environmentSensing.NewLaser.Data.IDataTaker;

/**
 *
 * @author simon.buehlmann
 */
public abstract class Command
{
    private IDataTaker data;
    private ICommandListener listener;
    
    public Command(int lenghtData, int lenghtSegment)
    {
        this.data = new Data(lenghtData, lenghtSegment);
    }
    
    public IDataTaker getData()
    {
        return this.data;
    }
    
    public void releaseCommand()
    {
        this.listener.newData(this);
    }
    
    public abstract ECommand getCommand();
}
