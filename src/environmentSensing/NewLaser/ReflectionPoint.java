
package environmentSensing.NewLaser;

import References.AReferencePoint;
import java.awt.Point;

/**
 *
 * @author simon.buehlmann
 */
public class ReflectionPoint
{
    private Point position;
    private int rssiValue;
    
    private AReferencePoint reference;
    
    public ReflectionPoint(Point position, int rssiValue, AReferencePoint reference)
    {
        this.position = position;
        this.rssiValue = rssiValue;
        
        this.reference = reference;
    }
    
    //getter
    public Point getPosition()
    {
        return position;
    }

    public int getRssiValue()
    {
        return rssiValue;
    }

    public AReferencePoint getReference()
    {
        return reference;
    }

    @Override
    public String toString()
    {
        return "X: " + this.position.x + " Y: " + this.position.y + " RSSI: " + this.rssiValue;
    }
    
    
}
