
package environmentSensing.scandatahandling.scandata;

import environmentSensing.scandatahandling.IScanData;
import environmentSensing.scandatahandling.IScanMeasurementData;
import environmentSensing.scandatahandling.IScannerData;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Simon Bühlmann
 */
public class ScanData implements IScanData
{
    private List<IScanMeasurementData> scanMeasurementDatas;
    private IScannerData scannerData;
    
    public ScanData(IScannerData scannerData)
    {
        this.scannerData = scannerData;
    }
    
    @Override
    public IScannerData getScannerData()
    {
        return this.scannerData;
    }

    @Override
    public List<IScanMeasurementData> getScanMeasurementData()
    {
        if(this.scanMeasurementDatas == null)
        {
            this.scanMeasurementDatas = new ArrayList<>();
        }
        
        return this.scanMeasurementDatas;
    }
    
}
