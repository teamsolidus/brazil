
package environmentSensing.NewLaser.TiM;

import environmentSensing.NewLaser.Communication.Communication;
import environmentSensing.NewLaser.Communication.IComWriter;
import environmentSensing.NewLaser.Data.DataContainer;
import environmentSensing.NewLaser.Data.DataMaskBasic;
import environmentSensing.NewLaser.Data.IDataContainer;
import environmentSensing.NewLaser.Data.IDataProvider;
import environmentSensing.NewLaser.Interpretation.BasicInterpreter;
import environmentSensing.NewLaser.Interpretation.ICommandListener;
import environmentSensing.NewLaser.Operation.IOperator;
import environmentSensing.NewLaser.Operation.Operator;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import org.apache.log4j.Logger;

/**
 *
 * @author simon.buehlmann
 */
public class TiM55x implements Observer, ILaserscanner
{
    private boolean wait;
    
    private IOperator operator;
    private IDataContainer container;
    private IComWriter com;
    private BasicInterpreter interpreter;

    //Logging
    private Logger log;
    
    public TiM55x() throws IOException
    {
        Logger.getLogger("Laser_Logger").info("Initialize TiM55x");
        
        container = new DataContainer();
        interpreter = new BasicInterpreter((ICommandListener) container);
        com = new Communication("192.168.2.2", 2112, interpreter);
        operator = new Operator(com);
        
        this.wait = false;
        
        this.addObserver(this);
    }
    
    @Override
    public IDataProvider<Integer> synchRunSingleMeas() throws Exception
    {
        operator.runSingleMeasurement();
        synchronized (this)
        {
            this.wait = true;
            do
            {
                this.wait();
            }
            while (this.wait);
        }
        return this.getData();
    }
    
    @Override
    public void asynchRunSingleMeas() throws Exception
    {
        operator.runSingleMeasurement();
    }
    
    @Override
    public void startContinuousMeas() throws Exception
    {
        operator.startContinouousMeasurement();
    }
    
    @Override
    public void stopContinuousMeas() throws Exception
    {
        operator.stopContinuousMeasurement();
    }
    
    @Override
    public IDataProvider<Integer> getData()
    {
        return new DataMaskBasic(container.getMeasurementData());
    }
    
    public void addObserver(Observer obs)
    {
        ((Observable) container).addObserver(obs);
    }

    //<editor-fold defaultstate="collapsed" desc="Observer">
    @Override
    public void update(Observable o, Object arg)
    {
        synchronized (this)
        {
            this.wait = false;
            this.notify();
        }
    }
//</editor-fold>
}
