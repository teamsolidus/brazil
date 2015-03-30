package environmentSensing.scandatahandling.scandata;

import environmentSensing.laserScanner.ILaserscannerMeasurementData;
import environmentSensing.scandatahandling.IScanData;
import environmentSensing.scandatahandling.IScanMeasurementData;

/**
 *
 * @author Simon Bühlmann
 */
public class ScandataFactory
{

    public static IScanData create(ILaserscannerMeasurementData data)
    {
        IScanData tempScandata = new ScanData(
                new ScannerData(
                        data.getFirmwareVersion(),
                        data.getDeviceNr(),
                        data.getSickSerialNr(),
                        data.getTelegrammCounter(),
                        data.getDevicePowerONDuration(),
                        data.getDeviceTransmissionDuration(),
                        data.getScanFrequency()
                ));
        int[] allDistanceValues = data.getAllDistanceValues();
        int[] allRSSIValues = data.getAllRSSIValues();
        
        if (allDistanceValues.length == allRSSIValues.length)
        {
            for (int cnt = 0; cnt < allDistanceValues.length; cnt++)
            {
                tempScandata.getScanMeasurementData().add(
                        new ScanMeasurementData(
                                allDistanceValues[cnt],
                                angleCalculator(cnt, data),
                                allRSSIValues[cnt]
                        ));
            }

            return tempScandata;
        } else
        {
            throw new RuntimeException("Corrupted data. Not the same amount of distance and rssi data.");
        }

    }

    public static IScanMeasurementData create(int distance, int angle, int rssiValue)
    {
        return new ScanMeasurementData(distance, angle, rssiValue);
    }

    // private methods
    private static int angleCalculator(int index, ILaserscannerMeasurementData data)
    {
        int startangle = (int) data.getScanStartAngle() / 10000;
        int increment = data.getScanAngleIncrement() / 10000;

        int temp = (int) (startangle + (increment * index));
        return temp;
    }
}
