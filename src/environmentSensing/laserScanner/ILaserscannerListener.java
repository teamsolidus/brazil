
package environmentSensing.laserScanner;

/**
 *
 * @author Simon Bühlmann
 */
public interface ILaserscannerListener
{
    public void newMeasurementData(ILaserscannerMeasurementData data);
}
