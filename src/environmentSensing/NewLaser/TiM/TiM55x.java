
package Laser.TiM;

import Laser.Communication.Communication;
import Laser.Communication.IComWriter;
import Laser.Data.DataContainer;
import Laser.Data.DataMaskBasic;
import Laser.Data.IDataContainer;
import Laser.Data.IDataProvider;
import Laser.Interpretation.BasicInterpreter;
import Laser.Interpretation.ICommandListener;
import Laser.Operation.IOperator;
import Laser.Operation.Operator;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * 
 * @author simon.buehlmann
 */
public class TiM55x implements Observer
{
    private boolean wait;
    
    private IOperator operator;
    private IDataContainer container;
    private IComWriter com;
    private BasicInterpreter interpreter;
    
    public TiM55x() throws IOException
    {
        container = new DataContainer();
        interpreter = new BasicInterpreter((ICommandListener) container);
        com = new Communication("192.168.2.2", 2112, interpreter);
        operator = new Operator(com);
        
        this.wait = false;
        
        this.addObserver(this);
    }
    
    public IDataProvider<Integer> synchRunSingleMeas() throws Exception
    {
        operator.runSingleMeasurement();
        synchronized(this)
        {
            this.wait = true;
            do
            {
                this.wait();
            }
            while(this.wait);
        }
        return this.getData();
    }
    
    public void asynchRundSingleMeas() throws Exception
    {
        operator.runSingleMeasurement();
    }
    
    public void startContinuousMeas() throws Exception
    {
        operator.startContinouousMeasurement();
    }
    
    public void stopContinuousMeas() throws Exception
    {
        operator.stopContinuousMeasurement();
    }
    
    public IDataProvider<Integer> getData() throws Exception
    {
        return new DataMaskBasic(container.getMeasurementData());
    }
    
    public void addObserver(Observer obs)
    {
        ((Observable)container).addObserver(obs);
    }

    //<editor-fold defaultstate="collapsed" desc="Observer">
    @Override
    public void update(Observable o, Object arg)
    {
        synchronized(this)
        {
            this.wait = false;
            this.notify();
        }
    }
//</editor-fold>
}
