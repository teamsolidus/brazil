
package Laser.Data;

import Laser.References.ReferencePoint;
import java.awt.Point;

/**
 *
 * @author simon.buehlmann
 */
public class DataMaskCoordinates implements IDataProvider<Point>
{
    private IDataProvider<Integer> data;
    private ReferencePoint ref;
    private ReferencePoint.Type refType;
    
    public DataMaskCoordinates(IDataProvider<Integer> data, ReferencePoint ref, ReferencePoint.Type refType)
    {
        this.data = data;
        this.ref = ref;
        this.refType = refType;
    }
    
    @Override
    public int getNrDistance()
    {
        return this.data.getNrDistance();
    }

    @Override
    public Point getDistance(int idx) throws Exception
    {
        Point tempReturn = new Point(calculateX(idx), calculateY(idx));
        return tempReturn;
    }

    @Override
    public Point[] getDistance(int startIdx, int endIdx) throws Exception
    {
        Point[] tempPoint = new Point[endIdx - startIdx];
        
        for(int countFor = 0; countFor < tempPoint.length; countFor++)
        {
            tempPoint[countFor] = this.getDistance(startIdx + countFor);
        }
        
        return tempPoint;
    }

    @Override
    public Point[] getDistance() throws Exception
    {
        return this.getDistance(0, data.getNrDistance()-1);
    }

    @Override
    public int getStartAngle()
    {
        return data.getStartAngle();
    }

    @Override
    public int getSteps()
    {
        return data.getSteps();
    }
    
    /**
     * Calc the X Coordinate from the handed parameters
     * @param data
     * @param angleDeg
     * @param refX
     * @return 
     */
    private int calculateX(int index) throws Exception
    {
        int tempAngle = this.alphaCalculator(index);
        double tempReturn = (Math.cos(Math.toRadians(tempAngle)) * data.getDistance(index)) + this.ref.getX(refType);
        return (int)tempReturn;
    }
    
    /**
     * Calc the Y Cordinate from the handed parameters
     * @param data
     * @param angleDeg
     * @param refY
     * @return 
     */
    private int calculateY(int index) throws Exception
    {
        int tempAngle = this.alphaCalculator(index);
        double tempReturn = (Math.sin(Math.toRadians(tempAngle)) * data.getDistance(index)) + this.ref.getY(refType);
        return (int)tempReturn;
    }
    
    /**
     * Calc the alpha angle from the handed parameters
     * @param index
     * @param startAngle
     * @param angleSteps
     * @param referenceAngle
     * @return 
     */
    private int alphaCalculator(int index)
    {
        int temp = (data.getStartAngle() + (data.getSteps() * index)) + this.ref.getAngle(this.refType);
        return temp;
    }

    
}
