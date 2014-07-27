
package environmentSensing.collisionDetection;

import References.AReferencePoint;
import References.AbsoluteReferencePoint;
import java.awt.Point;

/**
 *
 * @author simon.buehlmann
 */
public class MonitorArea
{
    private int sizeX, sizeY;
    
    //Limits
    private int xLeft, xRight, yTop, yBottom;
    
    private AReferencePoint ref;
    
    public MonitorArea(int width, int lenght)
    {
        //Limit Coordinates Aaera
        this.xRight = width/2;
        this.xLeft = this.xRight * -1;
        this.yTop = lenght;
        this.yBottom = 0;
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
        int tempXLeft = this.xLeft;
        int tempXRight = this.xRight;
        int tempYTop = this.yTop;
        int tempYBottom = this.yBottom;

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
    
    public int getWidth()
    {
        return 0;
    }
    
    public int getLenght()
    {
        return 0;
    }
}
