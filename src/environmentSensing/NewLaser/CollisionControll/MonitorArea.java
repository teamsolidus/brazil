
package Laser.CollisionControll;

import Laser.References.IReferencePointContainer;
import Laser.References.ReferencePoint;
import Laser.References.RelativeReferencePoint;
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
    private ReferencePoint.Type type;
    
    public MonitorArea(ReferencePoint ref, ReferencePoint.Type type)
    {
        this.sizeX = 580;
        this.sizeY = 1250;
        
        //Limit Coordinates Aaera
        this.xRight = this.sizeX/2;
        this.xLeft = this.xRight * -1;
        this.yTop = this.sizeY/2;
        this.yBottom = this.yTop * -1;
        
        this.ref = new RelativeReferencePoint(0, this.sizeY/2, 0, ref);
        this.type = type;
    }
    
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
    
    public boolean checkArea(Point coord)
    {
        int tempXLeft = this.xLeft + this.ref.getX(type);
        int tempXRight = this.xRight + this.ref.getX(type);
        int tempYTop = this.yTop + this.ref.getY(type);
        int tempYBottom = this.yBottom + this.ref.getY(type);
        
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