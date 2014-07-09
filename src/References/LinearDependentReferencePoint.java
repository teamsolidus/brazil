
package References;

/**
 *
 * @author simon.buehlmann
 */
public class LinearDependentReferencePoint extends AReferencePoint
{
    private final AReferencePoint REFERENCE;
    
    private int distance, angleThis, angleReference;
    
    public LinearDependentReferencePoint(int distance, int angleThis, int angleReference, AReferencePoint reference)
    {
        super();
        
        this.REFERENCE = reference;
        this.distance = distance;
        this.angleThis = angleThis;
        this.angleReference = angleReference;
    }

    @Override
    public int getX()
    {
        int tempX = (int)(Math.cos(Math.toRadians(this.REFERENCE.getAbsolutAngle() + this.angleReference)) * this.distance);
        return tempX;
    }

    @Override
    public int getY()
    {
        int tempY = (int)(Math.sin(Math.toRadians(this.REFERENCE.getAbsolutAngle() + this.angleReference)) * this.distance);
        return tempY;
    }

    @Override
    public int getAngle()
    {
        return this.angleReference + this.angleThis;
    }

    @Override
    public int getAbsolutX()
    {
        return this.REFERENCE.getAbsolutX() + this.getX();
    }

    @Override
    public int getAbsolutY()
    {
        return this.REFERENCE.getAbsolutY() + this.getY();
    }

    @Override
    public int getAbsolutAngle()
    {
        return AReferencePoint.calculateAngle(this.REFERENCE.getAbsolutAngle() + this.getAngle());
    }
}
