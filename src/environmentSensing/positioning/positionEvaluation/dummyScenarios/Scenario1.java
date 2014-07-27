
package environmentSensing.positioning.positionEvaluation.dummyScenarios;

import References.AbsoluteReferencePoint;
import environmentSensing.positioning.positionEvaluation.IEnvironmentReflections;
import environmentSensing.positioning.positionEvaluation.IEnvironmentSensor;
import java.awt.Point;

/**
 *
 * @author simon.buehlmann
 */
public class Scenario1 implements IEnvironmentSensor
{
    private static Scenario1 instance;
    
    //private MeasuredResult result;
    
    public Scenario1()
    {
        /*result = new MeasuredResult(AbsoluteReferencePoint.getInstance());
        
        result.addReflectionPoint(new Point(0, 470), 0);
        result.addReflectionPoint(new Point(45, 470), 0);
        result.addReflectionPoint(new Point(90, 470), 0);
        result.addReflectionPoint(new Point(215, 780), 0);
        result.addReflectionPoint(new Point(300, 795), 0);
        result.addReflectionPoint(new Point(390, 810), 0);
        result.addReflectionPoint(new Point(480, 820), 0);
        result.addReflectionPoint(new Point(600, 840), 0);
        result.addReflectionPoint(new Point(735, 870), 0);
        result.addReflectionPoint(new Point(905, 890), 0);
        
        result.addReflectionPoint(new Point(480, 390), 0);
        result.addReflectionPoint(new Point(480, 320), 0);
        result.addReflectionPoint(new Point(480, 260), 0);
        result.addReflectionPoint(new Point(1190, 545), 0);
        result.addReflectionPoint(new Point(1210, 435), 0);
        result.addReflectionPoint(new Point(1225, 320), 0);
        result.addReflectionPoint(new Point(1245, 220), 0);
        result.addReflectionPoint(new Point(1260, 100), 0);
        result.addReflectionPoint(new Point(1275, 0), 0);*/
    }
    
    public static Scenario1 getInstance()
    {
        if(instance == null)
        {
            instance = new Scenario1();
        }
        return instance;
    }

    @Override
    public IEnvironmentReflections getEnvironmentReflections()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
