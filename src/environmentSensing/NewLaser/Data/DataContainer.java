
package environmentSensing.NewLaser.Data;

import environmentSensing.NewLaser.Interpretation.Command;
import environmentSensing.NewLaser.Interpretation.ICommandListener;
import java.util.Observable;

/**
 *
 * @author simon.buehlmann
 */
public class DataContainer extends Observable implements IDataContainer, ICommandListener
{
    private Data newestData;
    
    public DataContainer()
    {
        this.newestData = null;
    }

    //<editor-fold defaultstate="collapsed" desc="IDataContainer">
    @Override
    public Data getMeasurementData()
    {
        return this.newestData;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ICommandListener">
    @Override
    public void newData(Command command)
    {
        this.newestData = (Data)command.getData();
        
        //Inform Observers
        this.setChanged();
        this.notifyObservers();
    }
//</editor-fold>
    
}
