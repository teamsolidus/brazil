
package environmentSensing.NewLaser;

import References.AReferencePoint;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author simon.buehlmann
 */
public class MeasuredResult
{
    private List<ReflectionPoint> reflectionPoints;
    private AReferencePoint reference;
    
    public MeasuredResult(AReferencePoint reference)
    {
        this.reference = reference;
        this.reflectionPoints = new ArrayList();
    }
    
    public void addReflectionPoint(Point point, int rssiValue)
    {
        this.reflectionPoints.add(new ReflectionPoint(point, rssiValue, this.reference));
    }
    
    //Getter & Setter
    public List<ReflectionPoint> getReflectionPoints()
    {
        return reflectionPoints;
    }

    public AReferencePoint getReference()
    {
        return reference;
    }
}
