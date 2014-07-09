package environmentSensing.positioning.positionEvaluation;

import References.AReferencePoint;
import References.ReferencePoint;
import environmentSensing.NewLaser.LaserFactory;
import environmentSensing.NewLaser.MeasuredResult;
import environmentSensing.NewLaser.ReflectionPoint;
import environmentSensing.positioning.positionEvaluation.dummyScenarios.Scenario1;
import environmentSensing.positioning.positionEvaluation.dummyScenarios.Scenario2;
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
        
        MeasuredResult measuredResult = LaserFactory.getInstance().getEnvironmentSensor().getEnvironmentReflections();
        
        int x = 0;
        
        while(x < (measuredResult.getReflectionPoints().size()-1))
        {
            ReflectionPoint tempPoint = measuredResult.getReflectionPoints().get(x);
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
                    tempWall = new Wall(measuredResult.getReference(), tempPoint, measuredResult.getReflectionPoints().get(x+1));
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
            System.out.println("Validate wall: " + wall.validateWall(measuredResult.getReflectionPoints()));
        }
        
        // Check Intersections (each valid wall with each other)
        for(int n = 0; n < newResult.getValidDetectedWalls().size()-1; n++)
        {
            for(int m = 1; m < newResult.getValidDetectedWalls().size(); m++)
            {
                try
                {
                    ReferencePoint tempCorner = newResult.getValidDetectedWalls().get(n).checkIntersectionWithOtherWall(newResult.getValidDetectedWalls().get(m));
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
