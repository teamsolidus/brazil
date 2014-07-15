
package environmentSensing.NewLaser.Interpretation;

import Laser.Interpretation.ECommand;

/**
 * Antwort single scan
 * @author simon.buehlmann
 */
public class Measurement extends Command
{
    public Measurement()
    {
        super(2000, 800);
    }
    
    @Override
    public ECommand getCommand()
    {
        return ECommand.MEASUREMENT;
    }
    
}
