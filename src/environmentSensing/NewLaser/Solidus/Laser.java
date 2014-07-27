package environmentSensing.NewLaser.Solidus;

import References.AbsoluteReferencePoint;
import References.ReferencePoint;
import environmentSensing.collisionDetection.MonitorArea;
import environmentSensing.NewLaser.Data.DataMaskAverage;
import environmentSensing.NewLaser.Data.DataMaskCoordinates;
import environmentSensing.NewLaser.TiM.ILaserscanner;
import environmentSensing.NewLaser.TiM.TiM55x;
import environmentSensing.collisionDetection.ICollisionReflections;
import environmentSensing.collisionDetection.ICollisionSensor;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import org.apache.log4j.Logger;

public class Laser extends Observable implements Observer, ILaserOperatorSolidus, ICollisionSensor
{
    // Logger
    private static Logger log;

    //Objekte
    private ILaserscanner tim;
    private ReferencePoint referencePoint;
    private DataMaskAverage dataMaskAverage;
    private MonitorArea monitorArea;

    public Laser() throws IOException
    {
        log = Logger.getLogger("Laser_Logger");
        log.debug("Laser created");
        
        this.tim = new TiM55x();
        this.referencePoint = new ReferencePoint(0, 0, 0, AbsoluteReferencePoint.getInstance());
        this.dataMaskAverage = new DataMaskAverage(10);

        this.tim.addObserver(this);
    }

    public ILaserscanner operateTim()
    {
        return this.tim;
    }

    public DataMaskCoordinates getCoordinatesData()
    {
        return new DataMaskCoordinates(this.dataMaskAverage, this.referencePoint);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        // Save new Data
         this.dataMaskAverage.update(tim.getData());
         
         // Inform Observers
        this.setChanged();
        this.notifyObservers();
    }

    @Override
    public void startContinuousMeasurement() throws Exception
    {
        tim.startContinuousMeas();
    }

    @Override
    public void stopContinuousMeasurement() throws Exception
    {
        this.tim.stopContinuousMeas();
    }
//</editor-fold>

    @Override
    public ICollisionReflections getCollisionReflections()
    {
        DataMaskCoordinates tempData = this.getCoordinatesData();
        return new DataWrapper(tempData);
    }
}
