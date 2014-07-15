
package environmentSensing.NewLaser.Solidus;

/**
 *
 * @author simon.buehlmann
 */
public class MeasDataSolidus
{
    private int minDistance;
    private boolean monitorAreaFree;
    
    public MeasDataSolidus(int minDistance, boolean monitorAreaFree)
    {
        this.minDistance = minDistance;
        this.monitorAreaFree = monitorAreaFree;
    }
    
    public int getMinDistance()
    {
        return this.minDistance;
    }
    
    public boolean isMonitorAreaFree()
    {
        return this.monitorAreaFree;
    }
}
