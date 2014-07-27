
package environmentSensing.positioning.positionEvaluation.wall;

import References.AReferencePoint;
import java.awt.Point;

/**
 *
 * @author simon.buehlmann
 */
public class LinearFunction
{
    private AReferencePoint reference;
    
    private double deltaX, deltaY; //for gradient
    private double intersectionYAxis;
    
    public LinearFunction(AReferencePoint reference, Point pointA, Point pointB)
    {
        this.defineFunction(reference, pointA, pointB);
    }
    
    public LinearFunction(AReferencePoint reference, double deltaX, double deltaY, double intersectionYAxis)
    {
        this.reference = reference;
        
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        
        this.intersectionYAxis = intersectionYAxis;
    }
    
    public LinearFunction()
    {
    }
    
    //Public Methods
    public final void defineFunction(AReferencePoint reference, Point pointA, Point pointB)
    {
        this.reference = reference;
        
        this.deltaX = pointB.getX() - pointA.getX();
        this.deltaY = pointB.getY() - pointA.getY();
        
        this.intersectionYAxis = this.evaluateIntersectionYAxis(this.deltaX, this.deltaY, pointA.getX(), pointA.getY());
    }
    
    public void shiftFunctionParallel90Degree(double distance)
    {
        double yShift = distance / Math.cos(this.getAngle());
        
        this.intersectionYAxis = this.intersectionYAxis + distance;
    }
    
    public void shiftFunctionToIntersectionPoint(Point point)
    {
        this.intersectionYAxis = this.evaluateIntersectionYAxis(this.deltaX, this.deltaY, point.getX(), point.getY());
    }
    
    public LinearFunction cloneFunctionParameters(LinearFunction function)
    {
        function.setDeltaX(this.deltaX);
        function.setDeltaY(this.deltaY);
        function.setIntersectionYAxis(this.intersectionYAxis);
        function.setReference(this.reference);
        
        return function;
    }
    
    public double getAngle()
    {
        return Math.toDegrees(Math.atan(this.deltaX/this.deltaY));
    }
    
    //Private Methods
    private double evaluateIntersectionYAxis(double deltaX, double deltaY, double concreteX, double concreteY)
    {
        return (concreteY - ((deltaY * concreteX) / deltaX));
    }
    
    //Getter & Setter
    public AReferencePoint getReference()
    {
        return reference;
    }

    public void setReference(AReferencePoint reference)
    {
        this.reference = reference;
    }

    public double getDeltaX()
    {
        return deltaX;
    }

    public void setDeltaX(double deltaX)
    {
        this.deltaX = deltaX;
    }

    public double getDeltaY()
    {
        return deltaY;
    }

    public void setDeltaY(double deltaY)
    {
        this.deltaY = deltaY;
    }

    public double getIntersectionYAxis()
    {
        return intersectionYAxis;
    }

    public void setIntersectionYAxis(double intersectionYAxis)
    {
        this.intersectionYAxis = intersectionYAxis;
    }
    
}
