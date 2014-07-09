
package environmentSensing.positioning;

/**
 *
 * @author simon.buehlmann
 */
public class NoReferenceException extends Exception
{
    public NoReferenceException()
    {
        super("No reference found by laser. Take odometrie value.");
    }
}
