/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Laser.References;

/**
 *
 * @author simon.buehlmann
 */
public class RelativeReferencePoint extends ReferencePoint
{
    private ReferencePoint reference;
    
    public RelativeReferencePoint(int x, int y, int angle, ReferencePoint reference)
    {
        super(x, y, angle);
        
        this.reference = reference;
    }
    
    public void relativeMove(int x, int y, int angle)
    {
        super.x = super.x + x;
        super.y = super.y + y;
        super. angle = super.angle + angle;
    }
    
    public void absoluteMove(int x, int y, int angle)
    {
        super.x = x;
        super.y = y;
        super.angle = angle;
    }
    
    public ReferencePoint getReferencePoint()
    {
        return this.reference;
    }
   
    @Override
    public int getX(Type refType)
    {
        switch(refType)
        {
            case ABSOLUTE:
                return this.reference.getX(Type.ABSOLUTE) + super.x;
            case RELATIVE:
                return super.x;
            default:
                return -1;
        }
    }

    @Override
    public int getY(Type refType)
    {
        switch(refType)
        {
            case ABSOLUTE:
                return this.reference.getY(Type.ABSOLUTE) + super.y;
            case RELATIVE:
                return super.y;
            default:
                return -1;
        }
    }

    @Override
    public int getAngle(Type refType)
    {
        switch(refType)
        {
            case ABSOLUTE:
                return this.reference.getAngle(Type.ABSOLUTE) + super.angle;
            case RELATIVE:
                return super.angle;
            default:
                return -1;
        }
    }
}
