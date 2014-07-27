
package environmentSensing.collisionDetection;

import References.AReferencePoint;
import java.awt.Point;

/**
 * Summs related reflections and the reference point, on which te reflections refer
 * @author simon.buehlmann
 */
public interface ICollisionReflections
{
    /**
     * returns all reflections
     * @return 
     */
    public Point[] getReflections();
}
