
package Laser.Interpretation;

/**
 * Bestätigung der Anforderung continuous scan
 * @author simon.buehlmann
 */
public class Confirm extends Command
{
    public Confirm()
    {
        super(12, 2);//Data 12, Segment 2
        System.out.println("New CONFIRM-COMMAND");
    }
    
    @Override
    public ECommand getCommand()
    {
        return ECommand.CONFIRM;
    }
    
}
