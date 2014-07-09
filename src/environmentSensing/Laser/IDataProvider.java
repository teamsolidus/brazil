package environmentSensing.Laser;

/**
 *
 * @author simon.buehlmann
 */
public interface IDataProvider
{
    public int getNrDistance();
    
    public int getDistance(int index) throws Exception;
    
    public int[] getDistance() throws Exception;
    
    public int[] getDistance(int startIndex, int stopIndex) throws Exception;
    
    public int getNrRssi();
    
    public int getRssi(int sickAngle) throws Exception;
    
    public int getStartAngle();
    
    public int getSteps();
}
