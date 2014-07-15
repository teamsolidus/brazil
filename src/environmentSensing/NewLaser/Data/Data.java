package environmentSensing.NewLaser.Data;

import java.util.ArrayList;

/**
 *
 * @author simon.buehlmann
 */
public class Data implements IDataTaker
{
    private byte[] dataList;
    private int[] segmentList;
    private int internalPointer;
    private int segmentCounter;
    private boolean release;

    public Data(int lenghtData, int lenghtSegment)
    {
        this.dataList = new byte[2000];
        this.segmentList = new int[800];

        //Pointer & Counter
        this.internalPointer = 0;
        this.segmentCounter = 0;

        //Release
        this.release = false;

        this.newSegment();
    }

    public byte getDataByte(int pointer)
    {
        return dataList[pointer];
    }
    
    public int getPointer(int segmentId)
    {
        return this.segmentList[segmentId];
    }

    public int getSegmentLenght(int segmentId)
    {
        int temp = this.segmentList[segmentId + 1] - this.segmentList[segmentId];//Differenz des Inhalts von einem Pointer zum nächsten
        return temp;
    }
    
    public boolean getReleaseState()
    {
        return this.release;
    }

    //<editor-fold defaultstate="collapsed" desc="IDataTaker">
    @Override
    public void addByte(byte data)
    {
        if (!release)
        {
            this.dataList[internalPointer] = data;
            this.internalPointer++;//Für Segment List. Zeigt auf nächstes zu befüllendes Feld.
        }
    }

    @Override
    public void newSegment()
    {
        if (!release)
        {
            this.segmentList[segmentCounter] = internalPointer;//Eintrag zeigt auf Start Byte in Data List
            segmentCounter++;
        }
    }

    @Override
    public void release()
    {
        this.release = true;
    }
//</editor-fold>
}
