package environmentSensing.NewLaser.Data;


/**
 *
 * @author simon.buehlmann
 */
public class DataMaskAverage implements IDataProvider<Integer>
{

    //For Avarage
    private IDataProvider[] dataBuffer;

    public DataMaskAverage(int bufferSize)
    {
        dataBuffer = new IDataProvider[bufferSize];
        
        // Default fill the buffer (no null pointer)
        for(int countFor = 0; countFor < this.dataBuffer.length; countFor++)
        {
            this.dataBuffer[countFor] = DummyProvider.getInstance();
        }
        DummyProvider.deletInstance();
    }

    private void addDataToBuffer(IDataProvider data)
    {
        for (int countFor = this.dataBuffer.length - 1; countFor > 0; countFor--)
        {
            this.dataBuffer[countFor] = this.dataBuffer[countFor - 1];
        }
        this.dataBuffer[0] = data;
    }

    @Override
    public int getNrDistance()
    {
        int tempReturn = this.dataBuffer[this.dataBuffer.length - 1].getNrDistance();

        return tempReturn;
    }

    @Override
    public Integer getDistance(int idx) throws Exception
    {
        Integer tempFilteredData = new Integer(0);

        for (IDataProvider<Integer> bufferedData : this.dataBuffer)
        {
            //Catch the 0 reflection
            if(bufferedData.getDistance(idx) > 20)
            {
                tempFilteredData = tempFilteredData + bufferedData.getDistance(idx);
            }
            else
            {
                tempFilteredData = tempFilteredData + 10000;
            }
        }

        return tempFilteredData / this.dataBuffer.length;
    }

    @Override
    public Integer[] getDistance(int startIdx, int endIdx) throws Exception
    {
        Integer[] temp = new Integer[endIdx - startIdx];
        for (int countFor = 0; countFor < temp.length; countFor++)
        {
            temp[countFor] = this.getDistance(startIdx + countFor);
        }
        return temp;
    }

    @Override
    public Integer[] getDistance() throws Exception
    {
        Integer[] temp = new Integer[this.getNrDistance()];
        for (int countFor = 0; countFor < temp.length; countFor++)
        {
            temp[countFor] = this.getDistance(countFor);
        }
        return temp;
    }

    @Override
    public int getStartAngle()
    {
        int tempReturn = this.dataBuffer[this.dataBuffer.length - 1].getStartAngle();
        return tempReturn;
    }

    @Override
    public int getSteps()
    {
        int tempReturn = this.dataBuffer[this.dataBuffer.length - 1].getSteps();
        return tempReturn;
    }

    public void update(IDataProvider data)
    {
        this.addDataToBuffer(data);

    }
}
