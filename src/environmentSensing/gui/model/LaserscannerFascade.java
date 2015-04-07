
package environmentSensing.gui.model;

import environmentSensing.laserScanner.ILaserscanner;
import environmentSensing.laserScanner.ILaserscannerListener;
import environmentSensing.laserScanner.ILaserscannerMeasurementData;
import environmentSensing.laserScanner.ILaserscannerOperator;
import environmentSensing.laserScanner.tim55x.TiM55x;
import environmentSensing.scandatahandling.IScanData;
import environmentSensing.scandatahandling.IScanMeasurementData;
import environmentSensing.scandatahandling.coordinates.CoordinatesCalculater;
import environmentSensing.scandatahandling.coordinates.CoordinatesScandata;
import environmentSensing.scandatahandling.filters.AverageFilter;
import environmentSensing.scandatahandling.scandata.ScandataFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Simon Bühlmann
 */
public class LaserscannerFascade implements ILaserscannerOperator, ILaserscannerListener
{
    private ILaserscanner laserscanner;
    private CoordinatesCalculater coordinatesCalculater;
    private AverageFilter averageFilter;
    private boolean connected;
    
    private List<ILaserscannerFascadeListener> listeners;
    
    public LaserscannerFascade()
    {
        this.connected = false;
        this.coordinatesCalculater = new CoordinatesCalculater();
        this.listeners = new ArrayList<>();
    }
    
    public void connect(String ipAddress, int port) throws LaserscannerException
    {
        try
         {
             this.laserscanner = new TiM55x(this, this, ipAddress, port);
             this.connected = true;
             this.informListenersLaserConnectionChanged(this.connected);
         }
         catch (IOException ex)
         {
             throw new LaserscannerException();
         }
    }
    
    public void disconnect()
    {
        if(this.laserscanner != null)
        {
            this.laserscanner.stopLaser();
            this.laserscanner = null;
            this.connected = false;
            this.informListenersLaserConnectionChanged(this.connected);
        }
    }
    
    public boolean isConnected()
    {
        return this.connected;
    }

    @Override
    public void newStateActice(State state)
    {
        
    }

    @Override
    public void errorOccured()
    {
       
    }

    @Override
    public void newMeasurementData(ILaserscannerMeasurementData data)
    {
        List<CoordinatesScandata> coordinates = new ArrayList<>();
        
        IScanData scanData = ScandataFactory.create(data);
        
        // filter data
        List<IScanMeasurementData> calculatedAverage;
        if(this.averageFilter != null)
        {
            calculatedAverage = this.averageFilter.calculateAverage(scanData);
        }
        else
        {
            calculatedAverage = scanData.getScanMeasurementData();
        }
        
        // calculate coordinates
        for(IScanMeasurementData measurementData : calculatedAverage)
        {
            coordinates.add(CoordinatesCalculater.calculateCoordinates(measurementData));
        }
        this.informListenersNewLaserData(coordinates);
    }
    
    public void runSingle() throws LaserscannerException
    {
         try
         {
             this.laserscanner.runSingleMeas();
         }
         catch (Exception ex)
         {
             throw new LaserscannerException();
         }
    }
    
    public void startCont()
    {
        try
        {
            this.laserscanner.startContinuousMeas();
        }
        catch (Exception ex)
        {
            Logger.getLogger(LaserscannerFascade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stopCont()
    {
        try
        {
            this.laserscanner.stopContinuousMeas();
        }
        catch (Exception ex)
        {
            Logger.getLogger(LaserscannerFascade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addListener(ILaserscannerFascadeListener listener)
    {
        this.listeners.add(listener);
    }
    
    public void removeListener(ILaserscannerFascadeListener listener)
    {
        this.listeners.remove(listener);
    }
    
    public synchronized void setAverageFilter(int bufferSize, boolean removeHighestAndLowest)
    {
        this.averageFilter = new AverageFilter(bufferSize, removeHighestAndLowest);
    }
    
    public synchronized void removeAverageFilter()
    {
        this.averageFilter = null;
    }
    
    // private methods
    private synchronized void informListenersNewLaserData(List<CoordinatesScandata> dataSet)
    {
        for(ILaserscannerFascadeListener listener : this.listeners)
        {
            listener.newMeasurementData(dataSet);
        }
    }
    
    private synchronized void informListenersLaserConnectionChanged(boolean isConnected)
    {
        for(ILaserscannerFascadeListener listener : this.listeners)
        {
            listener.laserConnectionChanged(isConnected);
        }
    }
}
