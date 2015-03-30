
package environmentSensing.gui.model;

import environmentSensing.scandatahandling.coordinates.CoordinatesScandata;
import java.util.List;

/**
 *
 * @author Simon Bühlmann
 */
public interface ILaserscannerFascadeListener
{
    public void newMeasurementData(List<CoordinatesScandata> datas);
    
    public void laserConnectionChanged(boolean isConnected);
}
