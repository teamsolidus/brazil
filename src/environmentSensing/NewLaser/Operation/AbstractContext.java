
package environmentSensing.NewLaser.Operation;

/**
 *
 * @author simon.buehlmann
 */
public abstract class AbstractContext
{
    
    private AbstractState activeState;
    
    public AbstractContext()
    {
        
    }
    
    public void initializeActiveStep(AbstractState state)
    {
        this.activeState = state;
        this.activeState.entry();
    }
    
    public void setState(AbstractState state)
    {
        this.activeState.exit();
        this.activeState = state;
        this.activeState.entry();
    }
    
    public AbstractState getActiveState()
    {
        return this.activeState;
    }
    
    //Abstract Methods
    public abstract AbstractState getState(EState state);
}
