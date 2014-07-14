package environmentSensing.positioning.positionEvaluation;

import References.AReferencePoint;
import References.ReferencePoint;
import environmentSensing.Environment;
import environmentSensing.positioning.DTOPosition;
import environmentSensing.positioning.ILaserPositionEvaluator;
import environmentSensing.positioning.NoReferenceException;
import java.util.List;
import java.util.Observer;

/**
 *
 * @author simon.buehlmann
 */
public class PositionEvaluator implements ILaserPositionEvaluator
{

    private Environment environment;
    private WallDetector wallDetector;

    // Angle Limits
    private final int NORTH_START = 45;
    private final int WEST_START = 135;
    private final int SOUTH_START = 325;
    private final int EAST_START = 315;

    private Orientation orientation;

    private enum Orientation
    {
        NORTH,
        WEST,
        SOUTH,
        EAST
    }

    // Constructors
    public PositionEvaluator()
    {
        this.wallDetector = new WallDetector(0, 0);
        this.environment = Environment.getInstance();
    }

    // Methods
    public void addWallDetectorObserver(Observer o)
    {
        this.wallDetector.addObserver(o);
    }

    @Override
    public DTOPosition evaluatePosition(ReferencePoint vaguePosition) throws NoReferenceException
    {
        int angle = vaguePosition.getAbsolutAngle();

        
        if (angle > this.NORTH_START && angle < this.WEST_START)
        {
            // Orientation North
            this.orientation = Orientation.NORTH;
        }
        else if (angle > this.WEST_START && angle < this.SOUTH_START)
        {
            // Orientation West
            this.orientation = Orientation.WEST;
        }
        else if (angle > this.SOUTH_START && angle < this.EAST_START)
        {
            // Orientation South
            this.orientation = Orientation.SOUTH;
        }
        else
        {
            // Orientation East
            this.orientation = Orientation.EAST;
        }

        DetectionResult result = this.wallDetector.detectWalls();

        // Corner detected?
        List<ReferencePoint> corners = result.getDetectedCorners();

        switch (corners.size())
        {
            case 0:
                // No Corner Detected
                // Check: Wall detected?
                if (result.getValidDetectedWalls().size() == 1)
                {
                    // correct angle
                    switch (this.orientation)
                    {
                        case EAST:
                            break;
                        case NORTH:
                            break;
                        case SOUTH:
                            break;
                        case WEST:
                            break;
                    }
                }
                break;
            case 1:
                // One Corner Detected
                AReferencePoint detectedCorner = null;
                
                switch (this.orientation)
                {
                    case EAST:
                        // Check: Corner in positive range in X-Axis?
                        if (corners.get(0).getX() > 0)
                        {
                            detectedCorner = this.environment.getCorner(Environment.EnvironmentCorner.CORNER_315);
                        }
                        else
                        {
                            detectedCorner = this.environment.getCorner(Environment.EnvironmentCorner.CORNER_45);
                        }
                        break;
                    case NORTH:
                        // Check: Corner in positive range in X-Axis?
                        if (corners.get(0).getX() > 0)
                        {
                            detectedCorner = this.environment.getCorner(Environment.EnvironmentCorner.CORNER_45);
                        }
                        else
                        {
                            detectedCorner = this.environment.getCorner(Environment.EnvironmentCorner.CORNER_135);
                        }
                        break;
                    case SOUTH:
                        // Check: Corner in positive range in X-Axis?
                        if (corners.get(0).getX() > 0)
                        {
                            detectedCorner = this.environment.getCorner(Environment.EnvironmentCorner.CORNER_225);
                        }
                        else
                        {
                            detectedCorner = this.environment.getCorner(Environment.EnvironmentCorner.CORNER_315);
                        }
                        break;
                    case WEST:
                        // Check: Corner in positive range in X-Axis?
                        if (corners.get(0).getX() > 0)
                        {
                            detectedCorner = this.environment.getCorner(Environment.EnvironmentCorner.CORNER_135);
                        }
                        else
                        {
                            detectedCorner = this.environment.getCorner(Environment.EnvironmentCorner.CORNER_225);
                        }
                        break;
                }
                        AReferencePoint tempReferenceForCalculation = new ReferencePoint((corners.get(0).getX() * -1), (corners.get(0).getY() * -1), corners.get(0).getAngle(), detectedCorner);
                        
                        DTOPosition tempPosition = new DTOPosition(DTOPosition.ReferenceCase.ALL_REFERENCES,
                                tempReferenceForCalculation.getAbsolutX(),
                                tempReferenceForCalculation.getAbsolutY(),
                                tempReferenceForCalculation.getAbsolutAngle(),
                                null);
                        
                        return tempPosition;
            case 2:
                // Specialcase: 2 Corners detected (not very probable)
                break;
            default:

        }

        return null;
    }
}
