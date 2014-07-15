package environmentSensing.NewLaser.Solidus;

import References.AbsoluteReferencePoint;
import environmentSensing.NewLaser.Data.IDataProvider;
import References.IReferencePointContainer;
import References.ReferencePoint;
import environmentSensing.collisionDetection.MonitorArea;
import environmentSensing.NewLaser.Data.DataMaskAverage;
import environmentSensing.NewLaser.Data.DataMaskCoordinates;
import environmentSensing.NewLaser.TiM.ILaserscanner;
import environmentSensing.NewLaser.TiM.TiM55x;
import java.awt.Point;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Laser implements IReferencePointContainer, Observer, ILaserOperatorSolidus
{

    //Objekte
    private ILaserscanner tim;
    private ReferencePoint referencePoint;
    private DataMaskAverage dataMaskAverage;
    private MonitorArea monitorArea;
    
    //TEMP SOLIDUS
    private GUIControllerSolidus gui;

    public Laser() throws IOException
    {
        this.tim = new TiM55x();
        this.referencePoint = new ReferencePoint(0, 0, 0, AbsoluteReferencePoint.getInstance());
        this.dataMaskAverage = new DataMaskAverage(10);
        this.monitorArea = new MonitorArea(this.getReferencePoint());

        this.gui = new GUIControllerSolidus(this);
        
        this.tim.addObserver(this);
        this.tim.addObserver(gui);
    }

    public ILaserscanner operateTim()
    {
        return this.tim;
    }

    public IDataProvider<Point> getCoordinatesData() throws Exception
    {
        return new DataMaskCoordinates(this.dataMaskAverage, this.referencePoint);
    }

    //<editor-fold defaultstate="collapsed" desc="IReferencePointContainer">
    @Override
    public ReferencePoint getReferencePoint()
    {
        return this.referencePoint;
    }

    @Override
    public void setReferencePoint(ReferencePoint reference)
    {
        this.referencePoint = reference;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Observer">
    @Override
    public void update(Observable o, Object arg)
    {
        try
        {
            this.dataMaskAverage.update(tim.getData());
        }
        catch (Exception ex)
        {
            Logger.getLogger("Laser_Logger").fatal("Fault on updating data mask average. Message: " + ex.getMessage());
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ILaserOperatorSolidus">
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

    @Override
    public MeasDataSolidus getMeasurementData()
    {
        int startIdx = 90;
        int endIdx = 180;

        boolean areaFree = false;
        int minDistance = 0;
        try
        {
            areaFree = monitorArea.checkArea(this.getCoordinatesData().getDistance(startIdx, endIdx));
            
            Point temp = monitorArea.getClosestCoordToYInArea(this.getCoordinatesData().getDistance(startIdx, endIdx));
            if(temp != null)
            {
                minDistance = (int)temp.getY();
            }
            else
            {
                minDistance = 0;
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger("Laser_Logger").fatal("Fault when checking measurement results with the monitor area. Message: " + ex.getMessage());
        }

        return new MeasDataSolidus(minDistance, areaFree);
    }

    @Override
    public void addObserver(Observer Obs)
    {
        tim.addObserver(Obs);
    }
//</editor-fold>
}
