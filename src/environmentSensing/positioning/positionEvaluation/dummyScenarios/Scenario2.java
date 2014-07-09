
package environmentSensing.positioning.positionEvaluation.dummyScenarios;

import References.AbsoluteReferencePoint;
import environmentSensing.NewLaser.MeasuredResult;
import environmentSensing.positioning.positionEvaluation.IEnvironmentSensor;
import java.awt.Point;

/**
 *
 * @author simon.buehlmann
 */
public class Scenario2 implements IEnvironmentSensor
{
    private static Scenario2 instance;
    
    private MeasuredResult result;
    
    public Scenario2()
    {
        result = new MeasuredResult(AbsoluteReferencePoint.getInstance());
        
        result.addReflectionPoint(new Point(95, 980), 0);
        result.addReflectionPoint(new Point(170, 910), 0);
        result.addReflectionPoint(new Point(235, 850), 0);
        result.addReflectionPoint(new Point(295, 790), 0);
        result.addReflectionPoint(new Point(350, 740), 0);
        result.addReflectionPoint(new Point(400, 680), 0);
        result.addReflectionPoint(new Point(445, 640), 0);
        result.addReflectionPoint(new Point(490, 600), 0);
        result.addReflectionPoint(new Point(540, 550), 0);
        result.addReflectionPoint(new Point(590, 500), 0);
        
        result.addReflectionPoint(new Point(630, 450), 0);
        result.addReflectionPoint(new Point(690, 400), 0);
        result.addReflectionPoint(new Point(700, 320), 0);
        result.addReflectionPoint(new Point(590, 210), 0);
        result.addReflectionPoint(new Point(520, 140), 0);
        result.addReflectionPoint(new Point(460, 90), 0);
        result.addReflectionPoint(new Point(410, 30), 0);
        result.addReflectionPoint(new Point(380, 0), 0);
    }
    
    public static Scenario2 getInstance()
    {
        if(instance == null)
        {
            instance = new Scenario2();
        }
        return instance;
    }

    @Override
    public MeasuredResult getEnvironmentReflections()
    {
        return this.result;
    }
}
