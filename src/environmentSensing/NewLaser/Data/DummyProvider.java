
package environmentSensing.NewLaser.Data;

/**
 *
 * @author simon.buehlmann
 */
public class DummyProvider implements IDataProvider<Integer> 
{
    private static DummyProvider instance;
    public static DummyProvider getInstance()
    {
        if(instance == null)
            instance = new DummyProvider();
        return instance;
    }
    public static boolean deletInstance()
    {
        if(instance != null)
        {
            instance = null;
            return true;
        }
        return false;
    }
    

    @Override
    public int getNrDistance()
    {
        return 0;
    }

    @Override
    public Integer getDistance(int idx) throws Exception
    {
        return 0;
    }

    @Override
    public Integer[] getDistance(int startIdx, int endIdx) throws Exception
    {
        Integer[] temp = new Integer[0];
        return temp;
    }

    @Override
    public Integer[] getDistance() throws Exception
    {
        Integer[] temp = new Integer[0];
        return temp;
    }

    @Override
    public int getStartAngle()
    {
        return 0;
    }

    @Override
    public int getSteps()
    {
        return 0;
    }

}
