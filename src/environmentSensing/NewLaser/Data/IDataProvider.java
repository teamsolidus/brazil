package environmentSensing.NewLaser.Data;

/**
 *
 * @author simon.buehlmann
 */
public interface IDataProvider <T>
{
    public int getNrDistance();
    
    public T getDistance(int idx) throws Exception;
    
    public T[] getDistance(int startIdx, int endIdx) throws Exception;
    
    public T[] getDistance() throws Exception;
    
    public int getStartAngle();
    
    public int getSteps();
}
