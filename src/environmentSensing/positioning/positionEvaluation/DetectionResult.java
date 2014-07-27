
package environmentSensing.positioning.positionEvaluation;

import environmentSensing.positioning.positionEvaluation.wall.Wall;
import References.ReferencePoint;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author simon.buehlmann
 */
public class DetectionResult
{
    private List<Wall> validDetectedWalls;
    private List<Point> detectedCorners;
    private ResultType resultType;
    
    public enum ResultType
    {
        NO_RESULT,
        ONLY_WALL,
        WALLS_AND_CORNERS;
    }
    
    public DetectionResult()
    {
        this.resultType = ResultType.NO_RESULT;
    }
    
    public DetectionResult(List<Wall> validDetectedWalls)
    {
        this.validDetectedWalls = validDetectedWalls;
        
        this.resultType = ResultType.ONLY_WALL;
    }
    
    public DetectionResult(List<Wall> validDetectedWalls, List<Point> detectedCorners)
    {
        this.validDetectedWalls = validDetectedWalls;
        this.detectedCorners = detectedCorners;
        
        this.resultType = ResultType.WALLS_AND_CORNERS;
    }
    
    public void addValidWall(Wall wall)
    {
        if(this.validDetectedWalls == null)
        {
            this.validDetectedWalls = new ArrayList<>();
        }
        
        this.validDetectedWalls.add(wall);
        
        if(this.resultType == resultType.NO_RESULT)
        {
            this.resultType = resultType.ONLY_WALL;
        }
    }
    
    public void addDetectedCorner(Point corner)
    {
        if(this.detectedCorners == null)
        {
            this.detectedCorners = new ArrayList<>();
        }
        
        this.detectedCorners.add(corner);
        
        this.resultType = resultType.WALLS_AND_CORNERS;
    }
    
    // Getter
    public List<Wall> getValidDetectedWalls()
    {
        return this.validDetectedWalls;
    }
    
    public List<Point> getDetectedCorners()
    {
        return this.detectedCorners;
    }
    
    public ResultType getResultType()
    {
        return this.resultType;
    }
}
