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
    private boolean removeHighestAndLowest;

    public AverageFilter(int bufferSize, boolean removeHighestAndLowest)
    {
        this.dataBuffer = new IScanData[bufferSize];
        this.removeHighestAndLowest = removeHighestAndLowest;
    }

    public List<IScanMeasurementData> calculateAverage(IScanData data)
    {
        // 1. Step: Add new data to buffer (on first place in Array)
        for (int countFor = this.dataBuffer.length - 1; countFor > 0; countFor--)
        {
            this.dataBuffer[countFor] = this.dataBuffer[countFor - 1];
        }
        this.dataBuffer[0] = data;

        // 2. Step: Calculate average from the values in the buffer
        return this.calculateAverage();
    }

    private List<IScanMeasurementData> calculateAverage()
    {
        List<IScanMeasurementData> averages = new ArrayList<>();

        // requires the same angle sequence!
        if (this.dataBuffer[0] != null)
        {
            for (int cnt = 0; cnt < this.dataBuffer[0].getScanMeasurementData().size(); cnt++)
            {
                // iterate through the distance values from the datas on the first place in the buffer
                
                int angle = this.dataBuffer[0].getScanMeasurementData().get(cnt).getAngle();
                int highestDistance = 0;
                int highestRSSI = 0;
                int lowestDistance = 0;
                int lowestRSSI = 0;
                int sumDistance = 0;
                int sumRSSI = 0;
                int nrValues = 0;

                for (IScanData bufferedScanData : this.dataBuffer)
                {
                    if (bufferedScanData != null)
                    {
                        IScanMeasurementData measDatasOfBufferedScanData = bufferedScanData.getScanMeasurementData().get(cnt);
                        
                        // check angle
                        if(measDatasOfBufferedScanData.getAngle() != angle)
                        {
                            throw new RuntimeException("Corrupted data. Not the same angle sequence");
                        }
                        
                        // add each measurement point
                        sumDistance = sumDistance + measDatasOfBufferedScanData.getDistance();
                        sumRSSI = sumRSSI + measDatasOfBufferedScanData.getRSSIValue();
                        nrValues++;

                        if (measDatasOfBufferedScanData.getDistance() > highestDistance)
                        {
                            highestDistance = measDatasOfBufferedScanData.getDistance();
                        }

                        if (measDatasOfBufferedScanData.getDistance() < lowestDistance
                                || lowestDistance == 0)
                        {
                            lowestDistance = measDatasOfBufferedScanData.getDistance();
                        }
                        
                        if (measDatasOfBufferedScanData.getDistance() > highestRSSI)
                        {
                            highestRSSI = measDatasOfBufferedScanData.getRSSIValue();
                        }

                        if (measDatasOfBufferedScanData.getDistance() < lowestRSSI
                                || lowestRSSI == 0)
                        {
                            lowestRSSI = measDatasOfBufferedScanData.getRSSIValue();
                        }
                    }
                }
                
                // remove highest and lowest value
                if(this.removeHighestAndLowest &&
                        nrValues >= 3)
                {
                    sumDistance = sumDistance - highestDistance - lowestDistance;
                    nrValues = nrValues - 2;
                }
                
                IScanMeasurementData create = ScandataFactory.create(
                        sumDistance / nrValues, // calculate distance average
                        angle,
                        sumRSSI / nrValues);// calculate rssi average

                averages.add(create);
            }

            return averages;
        }

        throw new RuntimeException("Buffer is empty");
    }
}
