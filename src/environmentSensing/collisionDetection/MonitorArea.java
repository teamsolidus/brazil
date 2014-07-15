
package environmentSensing.collisionDetection;

import References.IReferencePointContainer;
import References.ReferencePoint;
import java.awt.Point;

/**
 *
 * @author simon.buehlmann
 */
public class MonitorArea implements IReferencePointContainer
{
    private int sizeX, sizeY;
    
    //Limits
    private int xLeft, xRight, yTop, yBottom;
    
    private ReferencePoint ref;
    
    public MonitorArea(ReferencePoint ref)
    {
        this.sizeX = 580;
        this.sizeY = 1250;
        
        //Limit Coordinates Aaera
        this.xRight = this.sizeX/2;
        this.xLeft = this.xRight * -1;
        this.yTop = this.sizeY/2;
        this.yBottom = this.yTop * -1;
        
        this.ref = new ReferencePoint(0, this.sizeY/2, 0, ref);
    }
    
    /**
     * is one of the handed points in the area?
     * @param coordinates
     * @return 
     */
    public boolean checkArea(Point[] coordinates)
    {
        for(Point p : coordinates)
        {
            if(this.checkArea(p))
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Is the handed point in the area?
     * @param coord
     * @return 
     */
    public boolean checkArea(Point coord)
    {
        int tempXLeft = this.xLeft + this.ref.getX();
        int tempXRight = this.xRight + this.ref.getX();
        int tempYTop = this.yTop + this.ref.getY();
        int tempYBottom = this.yBottom + this.ref.getY();
        
        if(coord.getX() < tempXRight && coord.getX() > tempXLeft)//Check: X in X limits
        {
            if(coord.getY() > tempYBottom && coord.getY() < tempYTop)//Check: Y in Y limits
            {
                return true;
            }
            return false;
        }
        return false;
    }
    
    /**
     * returns the closest point to the ref point. No whether in the area or not.
     * @param coordinates
     * @return 
     */
    public Point getClosestCoordToY(Point[] coordinates)
    {
        Point tempReturn = new Point(10000, 10000);
        
        for(Point p : coordinates)
        {
            if(p.getY() < tempReturn.getY())
            {
                tempReturn.setLocation(p.getX(), p.getY());
            }
        }
        
        return tempReturn;
    }
    
    public Point getClosestCoordToYInArea(Point[] coordinates)
    {
        Point tempReturn = null;
        
        for(Point p : coordinates)
        {
            if(this.checkArea(p))// is the point in the area?
            {
                if(tempReturn == null)
                {
                    tempReturn = new Point(p.x, p.y);
                }
                if(tempReturn.getY() > p.getY())
                {
                    tempReturn.setLocation(p.getX(), p.getY());
                }
            }
        }
        
        return tempReturn;
    }
    
    

    @Override
    public ReferencePoint getReferencePoint()
    {
        return this.ref;
    }

    @Override
    public void setReferencePoint(ReferencePoint reference)
    {
        this.ref = reference;
    }
}