
package environmentSensing.scandatahandling.filters;

import environmentSensing.scandatahandling.IScanData;
import environmentSensing.scandatahandling.IScanMeasurementData;
import environmentSensing.scandatahandling.scandata.ScandataFactory;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Simon Bühlmann
 */
public class AverageFilter
{
    private IScanData[] dataBuffer;
    
    private void addDataToBuffer(IScanData data)
    {
        for (int countFor = this.dataBuffer.length - 1; countFor > 0; countFor--)
        {
            this.dataBuffer[countFor] = this.dataBuffer[countFor - 1];
        }
        this.dataBuffer[0] = data;
    }
    
    private List<IScanMeasurementData> calculateAverage()
    {
        List<IScanMeasurementData> tempSet = new ArrayList<>();
        
        for(IScanData dataSet : this.dataBuffer)
        {
            if(dataSet != null)
            {
                int highest = 0;
                int lowest = 0;
                int sum = 0;
                int cnt = 0;
                
                for(IScanMeasurementData measurementData : dataSet.getScanMeasurementData())
                {
                    // add each measurement point
                    sum = sum + measurementData.getDistance();
                    cnt++;
                    
                    if(measurementData.getDistance() > highest)
                    {
                        highest = measurementData.getDistance();
                    }
                    
                    if(measurementData.getDistance() < lowest ||
                            lowest == 0)
                    {
                        lowest = measurementData.getDistance();
                    }
                }
                
                sum = sum - highest - lowest;
                cnt = cnt - 2;
                
                ScandataFactory.create(
                        sum/cnt, 
                        cnt, 
                        sum)
            }
        }
    }
}
