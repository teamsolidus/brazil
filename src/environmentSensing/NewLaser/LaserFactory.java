
package environmentSensing.NewLaser;

import environmentSensing.positioning.positionEvaluation.IEnvironmentSensor;
import environmentSensing.positioning.positionEvaluation.dummyScenarios.Scenario1;
import environmentSensing.positioning.positionEvaluation.dummyScenarios.Scenario2;

/**
 *
 * @author simon.buehlmann
 */
public class LaserFactory
{
    private static LaserFactory instance;
    public static LaserFactory getInstance()
    {
        if(instance == null)
        {
            instance = new LaserFactory();
        }
        return instance;
    }
    
    public IEnvironmentSensor getEnvironmentSensor()
    {
        return Scenario2.getInstance();
    }
}
