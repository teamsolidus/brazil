
package environmentSensing.NewLaser.Solidus;

import environmentSensing.NewLaser.Data.DataMaskCoordinates;
import environmentSensing.collisionDetection.ICollisionReflections;
import java.awt.Point;

/**
 * Wrapps the Data from the Laser api for the positioning and the collision detection
 * @author simon.buehlmann
 */
public class DataWrapper implements ICollisionReflections
{
    private DataMaskCoordinates data;
    private final int START_IDX, END_IDX;
    
    public DataWrapper(DataMaskCoordinates data)
    {
        this.START_IDX = 90;
        this.END_IDX = 180;
        
        this.data = data;
    }
    
    @Override
    public Point[] getReflections()
    {
        return this.data.getDistance(START_IDX, END_IDX);
    }
}
