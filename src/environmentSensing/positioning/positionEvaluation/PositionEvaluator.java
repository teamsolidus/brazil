
package environmentSensing.positioning.positionEvaluation;

import References.ReferencePoint;
import environmentSensing.Environment;
import environmentSensing.positioning.DTOPosition;
import environmentSensing.positioning.ILaserPositionEvaluator;
import environmentSensing.positioning.NoReferenceException;
import java.util.Observer;

/**
 *
 * @author simon.buehlmann
 */
public class PositionEvaluator implements ILaserPositionEvaluator
{
    private Environment environment;
    private WallDetector wallDetector;
    
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
