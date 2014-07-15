
package environmentSensing.NewLaser.Data;

/**
 *
 * @author simon.buehlmann
 */
public class DataMaskBasic implements IDataProvider <Integer>
{
    private Data data;
    
    public DataMaskBasic(Data data)
    {
        this.data = data;
    }

    //<editor-fold defaultstate="collapsed" desc="IDataProvider">
    @Override
    public int getNrDistance()
    {
        return this.interpretAsInt(this.getPointer(Segment.NR_DISTANCE_DATA));
    }

    @Override
    public Integer getDistance(int index) throws Exception
    {
        return this.interpretAsInt(this.getPointer(Segment.DISTANCE_DATA) + index);
    }
    
    @Override
    public Integer[] getDistance(int startIdx, int endIdx) throws Exception
    {
        Integer[] temp = new Integer[endIdx - startIdx];
        for(int countFor = 0; countFor < temp.length; countFor++)
        {
            temp[countFor] = this.getDistance(startIdx + countFor);
        }
        return temp;
    }

    @Override
    public Integer[] getDistance() throws Exception
    {
        Integer[] temp = new Integer[this.getNrDistance()];
        for(int countFor = 0; countFor < temp.length; countFor++)
        {
            temp[countFor] = this.getDistance(countFor);
        }
        return temp;
    }

    public int getNrRssi()
    {
        return this.interpretAsInt(this.getPointer(Segment.NR_RSSI_DATA));
    }

    public int getRssi(int index) throws Exception
    {
        return this.interpretAsInt(this.getPointer(Segment.RSSI_DATA) + index);
    }

    @Override
    public int getStartAngle()
    {
        int startAngle = this.interpretAsInt(this.getPointer(Segment.START_ANGLE));
        if (Integer.numberOfTrailingZeros(startAngle) < 6)//Special Case -45 (laser returns -449999). Check the 0 bits of the angle. 
        {
            return -45;
        }
        return startAngle / 10000;
    }

    @Override
    public int getSteps()
    {
        return this.interpretAsInt(this.getPointer(Segment.ANGLE_STEPS)) / 10000;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private_Methods">

    /**
     * Interpretieren eines Datensegments als int
     *
     * @param segmentId Startpunkt des Datensegments
     * @return
     */
    private int interpretAsInt(int segmentId)
    {
        int tempInt = 0;
        int dezimalValency = 1;//Wertigkeit der Stellen (Hexadez)

        int currentPointer;//Zeigt auf momentan aktuelles Byte in Schlaufe
        char currentChar;//Repräsentiert das ausgelesene Byte an der position des currentDataPointer

        for (int countFor = data.getSegmentLenght(segmentId) - 1; countFor >= 0; countFor--)//Beginnen mit letztem Feld in Segment
        {
            currentPointer = data.getPointer(segmentId) + countFor;
            currentChar = (char) ((byte) data.getDataByte(currentPointer));
            tempInt = tempInt + Character.getNumericValue(currentChar) * dezimalValency;
            dezimalValency = dezimalValency * 16;
        }
        return tempInt;
    }

    /**
     * Gibt die Position für die Rssi-Daten zurück und rechnet dabei die
     * variable Anzahl von Distance-Data ein.
     *
     * @param rssiPointer Position des Rssi-Datenfeldes relativ
     * @return Position des rssi-Datenfeldes absolut
     * @throws Exception
     */
    private int rssiPointerCalculator(int rssiPointer)
    {
        return this.getPointer(Segment.DISTANCE_DATA) + this.getNrDistance() + rssiPointer;
    }

    /**
     * Gibt den Pointer auf die jeweiligen Segmente zurück. Berücksichtigt dabei
     * die Verschiebung durch unterschiedlich viele Distance Daten.
     *
     * @param segment Segment, für welches Pointer ausgegeben werden soll.
     * @return Pointer, der in dataList auf entsprechenden Anfangspunkt zeigt
     */
    private int getPointer(Segment segment)
    {
        int temp = -1;
        switch (segment)
        {
            case NR_DISTANCE_DATA:
                temp = 25 - 1;
                break;
            case DISTANCE_DATA:
                temp = 26 - 1;
                break;
            case NR_RSSI_DATA:
                temp = this.rssiPointerCalculator(6);
                break;
            case RSSI_DATA:
                temp = this.rssiPointerCalculator(7);
                break;
            case START_ANGLE:
                temp = 23 - 1;
                break;
            case ANGLE_STEPS:
                temp = 24 - 1;
                break;
        }
        return temp;
    }
    
    private enum Segment
    {
        NR_DISTANCE_DATA,
        DISTANCE_DATA,
        NR_RSSI_DATA,
        RSSI_DATA,
        START_ANGLE,
        ANGLE_STEPS;
    }
//</editor-fold>
}
