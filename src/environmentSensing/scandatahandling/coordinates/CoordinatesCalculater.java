package environmentSensing.scandatahandling.coordinates;

import environmentSensing.scandatahandling.IScanMeasurementData;

/**
 *
 * @author simon.buehlmann
 */
public class CoordinatesCalculater
{
    public static CoordinatesScandata calculateCoordinates(IScanMeasurementData scanMeasurementData)
    {
        return new CoordinatesScandata(calculateX(scanMeasurementData), calculateY(scanMeasurementData), scanMeasurementData.getRSSIValue());
    }

    /**
     * Calc the X Coordinate from the handed parameters
     *
     * @param data
     * @param angleDeg
     * @param refX
     * @return
     */
    private static int calculateX(IScanMeasurementData scanMeasurementData)
    {
        double cos = Math.cos(Math.toRadians(scanMeasurementData.getAngle()));
        
        double tempReturn = (cos * scanMeasurementData.getDistance());
        return (int) tempReturn;
    }

    /**
     * Calc the Y Cordinate from the handed parameters
     *
     * @param data
     * @param angleDeg
     * @param refY
     * @return
     */
    private static int calculateY(IScanMeasurementData scanMeasurementData)
    {
        double sin = Math.sin(Math.toRadians(scanMeasurementData.getAngle()));
        
        double tempReturn = (sin * scanMeasurementData.getDistance());
        return (int) tempReturn;
    }
}
