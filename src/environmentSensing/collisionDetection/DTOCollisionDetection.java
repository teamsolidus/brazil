
package environmentSensing.collisionDetection;

import environmentSensing.gui.views.percentDiagramm.IPercentDataProvider;

/**
 *
 * @author simon.buehlmann
 */
public class DTOCollisionDetection implements IPercentDataProvider
{
    private int minDistance;
    private boolean monitorAreaFree;
    private byte speedPercent;
    
    public DTOCollisionDetection(int minDistance, boolean monitorAreaFree, byte speedPercent)
    {
        this.minDistance = minDistance;
        this.monitorAreaFree = monitorAreaFree;
        this.speedPercent = speedPercent;
    }
    
    public int getMinDistance()
    {
        return this.minDistance;
    }
    
    public boolean isMonitorAreaFree()
    {
        return this.monitorAreaFree;
    }

    @Override
    public byte getSpeedPercent()
    {
        return speedPercent;
    }
}
