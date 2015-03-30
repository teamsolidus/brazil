
package environmentSensing.scandatahandling.scandata;

import environmentSensing.scandatahandling.IScanMeasurementData;


/**
 *
 * @author Simon Bühlmann
 */
public class ScanMeasurementData implements IScanMeasurementData
{
    private final int distance;
    private final int angle;
    private final int rssi;
    
    public ScanMeasurementData(int distance, int angle, int rssi)
    {
        this.distance = distance;
        this.angle = angle;
        this.rssi = rssi;
    }
    
    // getter
    @Override
    public int getDistance()
    {
        return distance;
    }

    @Override
    public int getAngle()
    {
        return angle;
    }

    @Override
    public int getRSSIValue()
    {
        return rssi;
    }
}
