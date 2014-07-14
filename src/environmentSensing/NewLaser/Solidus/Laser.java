package Laser.Solidus;

import Laser.Data.DataMaskAverage;
import Laser.Data.DataMaskCoordinates;
import Laser.Data.IDataProvider;
import Laser.CollisionControll.MonitorArea;
import Laser.References.IReferencePointContainer;
import Laser.References.ReferencePoint;
import Laser.TiM.TiM55x;
import Laser.References.MainReferencePoint;
import Laser.References.RelativeReferencePoint;
import java.awt.Point;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Laser implements IReferencePointContainer, Observer, ILaserOperatorSolidus
{

    //Objekte
    private TiM55x tim;
    private ReferencePoint referencePoint;
    private DataMaskAverage average;
    private MonitorArea collAera;
    
    //TEMP SOLIDUS
    private GUIControllerSolidus gui;

    public Laser() throws IOException
    {
        this.tim = new TiM55x();
        this.referencePoint = new RelativeReferencePoint(0, 0, 0, MainReferencePoint.getInstance());
        this.average = new DataMaskAverage(10);
        this.collAera = new MonitorArea(this.getReferencePoint(), ReferencePoint.Type.ABSOLUTE);

        this.gui = new GUIControllerSolidus(this);
        
        this.tim.addObserver(this);
        this.tim.addObserver(gui);
    }

    public TiM55x operateTim()
    {
        return this.tim;
    }

    public IDataProvider<Point> getCoordinatesData() throws Exception
    {
        return new DataMaskCoordinates(this.average, this.referencePoint, ReferencePoint.Type.RELATIVE);
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
            this.average.update(tim.getData());
        }
        catch (Exception ex)
        {
            Logger.getLogger(Laser.class.getName()).log(Level.SEVERE, null, ex);
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
    public MeasDataSolidus getMeasurementData() throws Exception
    {
        int startIdx = 90;
        int endIdx = 180;

        boolean areaFree = collAera.checkArea(this.getCoordinatesData().getDistance(startIdx, endIdx));
        int minDistance = (int) collAera.getClosestCoordToY(this.getCoordinatesData().getDistance(startIdx, endIdx)).getY();
        
        return new MeasDataSolidus(minDistance, areaFree);
    }

    @Override
    public void addObserver(Observer Obs)
    {
        tim.addObserver(Obs);
    }
//</editor-fold>
}
