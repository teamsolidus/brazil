
package Laser.References;

/**
 *
 * @author simon.buehlmann
 */
public abstract class ReferencePoint
{
    protected int x, y, angle;
    
    public enum Type
    {
        ABSOLUTE,
        RELATIVE;
    }
    
    public ReferencePoint(int x, int y, int angle)
    {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }
    
    public abstract int getX(Type refType);
    
    public abstract int getY(Type refType);
    
    public abstract int getAngle(Type refType);
}
