package environmentSensing.NewLaser.Data;

/**
 *
 * @author simon.buehlmann
 */
public interface IDataProvider <T>
{
    public int getNrDistance();
    
    public T getDistance(int idx);
    
    public T[] getDistance(int startIdx, int endIdx);
    
    public T[] getDistance();
    
    public int getStartAngle();
    
    public int getSteps();
}
