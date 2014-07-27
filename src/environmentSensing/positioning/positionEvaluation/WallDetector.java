package environmentSensing.positioning.positionEvaluation;

import environmentSensing.positioning.positionEvaluation.wall.Wall;
import environmentSensing.NewLaser.Solidus.LaserFactory;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author simon.buehlmann
 */
public class WallDetector extends Observable
{
    private int minFollowingPointsForVector;
    private int ratioTolerance;

    public WallDetector(int minFollowingPointsForVector, int ratioTolerance)
    {
        super();

        this.minFollowingPointsForVector = minFollowingPointsForVector;
        this.ratioTolerance = ratioTolerance;
    }

    public DetectionResult detectWalls()
    {
        List<Wall> wallBuffer = new ArrayList<>();
        Wall tempWall = null;
        
        IEnvironmentReflections measuredResult = LaserFactory.getInstance().getEnvironmentSensor().getEnvironmentReflections();
        
        int x = 0;
        
        while(x < (measuredResult.getEnvironmentReflections().length-1))
        {
            Point tempPoint = measuredResult.getEnvironmentReflections()[x];
            System.out.println("New tempPoint: " + tempPoint);
            
            boolean matchesPointWithWallFromBuffer = false;
            Wall matchingWall = null;
            for(Wall wall : wallBuffer)
            {
                if(wall.reflectionPointBelongsToWall(tempPoint))
                {
                    matchesPointWithWallFromBuffer = true;
                    matchingWall = wall;
                    System.out.println("Point matching with exist wall from buffer");
                    System.out.println(wall);
                    break;
                }
            }
            if(!matchesPointWithWallFromBuffer)
            {
                if(tempWall != null)
                {
                    if(tempWall.reflectionPointBelongsToWall(tempPoint))
                    {
                        tempWall.addReflectionPoint(tempPoint);
                        System.out.println("Point matching with tempWall");
                        
                        if(tempWall.enoughDefinitionPoints())
                        {
                            wallBuffer.add(tempWall);
                            System.out.println("Wall Detected!");
                            System.out.println(tempWall);
                            tempWall = null;
                        }
                        x++;
                    }
                    else
                    {
                        tempWall = null;
                        System.out.println("Point dont matching with tempWall");
                        x = x-1;
                    }
                }
                else
                {
                    tempWall = new Wall(tempPoint, measuredResult.getEnvironmentReflections()[x+1]);
                    System.out.println("New tempWall created: " + tempWall);
                    x = x + 2;
                }
            }
            else
            {
                matchingWall.addReflectionPoint(tempPoint);
                x++;
            }
        }
        
        // Validate Resulte
        DetectionResult newResult = new DetectionResult();
        
        // Validate detected Walls
        for(Wall wall : wallBuffer)
        {
            newResult.addValidWall(wall);
            System.out.println("Validate wall: " + wall.validateWall(measuredResult.getEnvironmentReflections()));
        }
        
        // Check Intersections (each valid wall with each other)
        for(int n = 0; n < newResult.getValidDetectedWalls().size()-1; n++)
        {
            for(int m = 1; m < newResult.getValidDetectedWalls().size(); m++)
            {
                try
                {
                    Point tempCorner = newResult.getValidDetectedWalls().get(n).checkIntersectionWithOtherWall(newResult.getValidDetectedWalls().get(m));
                    newResult.addDetectedCorner(tempCorner);
                    System.out.println("Corner Detected: " + tempCorner);
                }
                catch (Exception ex)
                {
                    Logger.getLogger(WallDetector.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        // Inform Observers
        super.setChanged();
        super.notifyObservers(newResult); // ruft für alle Beobachter die update-Methode auf
        
        return newResult;
    }

    public static void main(String[] args)
    {
        
        System.out.println("START");
        WallDetector detector = new WallDetector(3, 100);
        detector.detectWalls();
    }
}
