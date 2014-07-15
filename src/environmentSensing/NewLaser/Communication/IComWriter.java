
package environmentSensing.NewLaser.Communication;

import java.io.IOException;

/**
 *
 * @author simon.buehlmann
 */
public interface IComWriter
{
    public void writeCommand(String command) throws IOException;
}
