package environmentSensing.positioning.positionEvaluation.wall;

import References.AReferencePoint;
import References.AbsoluteReferencePoint;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author simon.buehlmann
 */
public class StraightLine
{
    private double angle;
    private Point basePoint;

    public StraightLine()
    {
        this.angle = 0;
        this.basePoint = new Point(0, 0);
    }

    public StraightLine(Point basePoint, Point definitionPoint)
    {
        this.defineLineWithTwoPoints(basePoint, definitionPoint);
    }

    //Definitions
    public void defineLineWithPointAndAngle(Point basePoint, long angle)
    {
        this.basePoint = basePoint;
        this.angle = angle;
    }

    public final void defineLineWithTwoPoints(Point basePoint, Point definitionPoint)
    {
        // Check 90 Degrees Tan-Exception
        if (basePoint.getX() == definitionPoint.getX())
        {
            this.angle = 90;
        }
        else
        {
            // Calculate with Trigo
            // 1. Step: Calculate Deltas
            double deltaX = basePoint.getX() - definitionPoint.getX();
            double deltaY = basePoint.getY() - definitionPoint.getY();

            // 2. Step: Calculate Angle
            this.angle = Math.toDegrees(Math.atan(deltaY / deltaX));

            // 3. Step: Round Angle (0 to 180 Degrees)
            this.angle = this.angle % 180;
            if (this.angle < 0)
            {
                this.angle = this.angle + 180;
            }
        }
        // Define Base Point
        this.basePoint = basePoint;
    }

    //Calculate
    public int getXOnDefinedY(double y) throws Exception
    {
        // Check angle Exception
        if (this.angle == 0)
        {
            throw new Exception("ERROR: infinitely results");
        }

        // Calculate Delta Y from Base Point
        double deltaY = y - this.basePoint.getY();

        // Calculate Delta X with Trigo
        double deltaX = deltaY / Math.tan(Math.toRadians(this.angle));

        // Calculate X with Base Point and delta X
        return (int) (this.basePoint.getX() + deltaX);
    }

    public int getYOnDefinedX(double x) throws Exception
    {
        // Check angle Exception
        if (this.angle == 90)
        {
            throw new Exception("ERROR: infinitely results");
        }

        // Calculate Delta X from Base Point
        double deltaX = x - this.basePoint.getX();

        // Calculate Delta Y with Trigo
        double deltaY = Math.tan(Math.toRadians(this.angle)) * deltaX;

        // Calculate Y with Base Point and delta Y
        return (int) (this.basePoint.getY() + deltaY);
    }

    public Point intersection(StraightLine other) throws Exception
    {
        // Check: Same orientation?
        if (this.getAngle() == other.getAngle())
        {
            throw new Exception("ERROR: Same Orientation");
        }

        // Check: this = horizontally? (Constant Y, endless X)
        if (this.getAngle() == 0)
        {
            int constantY = (int) this.getBasePoint().getY();
            return new Point(other.getXOnDefinedY(constantY), constantY);
        }

        // Check: this = vertically? (Constant X, endless Y)
        if (this.getAngle() == 90)
        {
            int constantX = (int) this.getBasePoint().getX();
            return new Point(constantX, other.getYOnDefinedX(constantX));
        }

        // Check: other = horizontally? (Constant Y, endless X)
        if (other.getAngle() == 0)
        {
            int constantY = (int) other.getBasePoint().getY();
            return new Point(this.getXOnDefinedY(constantY), constantY);
        }

        // Check: other = vertically? (Constant X, endless Y)
        if (other.getAngle() == 90)
        {
            int constantX = (int) other.getBasePoint().getX();
            return new Point(constantX, this.getYOnDefinedX(constantX));
        }

        // Calculate Intersection
        // Step 1: Define same Y Reference
        double tempX = this.getXOnDefinedY(other.getBasePoint().getY());

        // Step 2: Calculate distance between the two references
        double distance = other.getBasePoint().getX() - tempX;

        // Step 3: Calculate tang 
        double tanAngleOther = Math.tan(Math.toRadians(180 - other.getAngle()));
        double tanAngleThis = Math.tan(Math.toRadians(this.angle));

        // Step 4: Calculate the deltaX1 (Distance between the tempX-Reference and the Intersection-Point)
        double deltaX1 = ((tanAngleOther * distance) / (tanAngleThis + tanAngleOther));

        //Calculate X from Intersection-Point
        int intersectionX = (int) (tempX + deltaX1);

        // Step 5: Return Intersection Point
        return new Point(intersectionX, this.getYOnDefinedX(intersectionX));
    }

    public StraightLine cloneParameters(StraightLine straightLine)
    {
        straightLine.setAngle(this.angle);
        straightLine.setBasePoint(this.basePoint);

        return straightLine;
    }

    // Shift the Line
    public void shiftLineParallel90Degree(int distance)
    {
        //Constante X
        if (this.angle == 90)
        {
            int newX = this.basePoint.x + distance;
            this.setBasePoint(new Point(newX, this.basePoint.y));
        }

        //Constante Y
        if (this.angle == 0)
        {
            int newY = this.basePoint.y + distance;
            this.setBasePoint(new Point(this.basePoint.x, newY));
        }

        //Rising
        if (this.angle > 0 && this.angle < 90)
        {
            int distanceX = (int) (Math.sin(Math.toRadians(this.angle)) * distance);
            int newX = (int) (this.getBasePoint().getX() + distanceX);
            int distanceY = (int) (Math.cos(Math.toRadians(this.angle)) * distance);
            int newY = (int) (this.getBasePoint().getY() - distanceY);
            
            this.setBasePoint(new Point(newX, newY));
        }
        
        //Sinking
        if(this.angle > 90 && this.angle < 180)
        {
            double tempAngle = this.angle - 90;
            
            int distanceX = (int) (Math.cos(Math.toRadians(tempAngle)) * distance);
            int newX = (int) (distanceX + this.getBasePoint().getX());
            int distanceY = (int) (Math.sin(Math.toRadians(tempAngle)) * distance);
            int newY = (int) (distanceY + this.getBasePoint().getY());
            
            this.setBasePoint(new Point(newX, newY));
        }
    }

    // Getter & Setter
    public double getAngle()
    {
        return angle;
    }

    public void setAngle(double angle)
    {
        this.angle = angle;
    }

    public Point getBasePoint()
    {
        return basePoint;
    }

    public void setBasePoint(Point basePoint)
    {
        this.basePoint = basePoint;
    }

    @Override
    public String toString()
    {
        return "STRAIGHT_LINE   X: " + this.basePoint.getX() + " Y: " + this.basePoint.getY() + " Angle: " + this.angle;
    }

    public static void main(String[] args)
    {
        // Test-Case 1
        StraightLine sl1 = new StraightLine();
        StraightLine sl2 = new StraightLine();
        sl1.defineLineWithTwoPoints(new Point(0, 2), new Point(2, 0));
        sl2.defineLineWithTwoPoints(new Point(0, 0), new Point(2, 2));

        System.out.println("Angle: " + sl1.angle);
        System.out.println("Angle: " + sl2.angle);
        try
        {
            System.out.println("Intersection X: " + sl1.intersection(sl2).getX());
            System.out.println("Intersection Y: " + sl1.intersection(sl2).getY());
        }
        catch (Exception ex)
        {
            Logger.getLogger(StraightLine.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Test-Case 2
        StraightLine sl3 = new StraightLine();
        StraightLine sl4 = new StraightLine();
        sl3.defineLineWithTwoPoints(new Point(1, 0), new Point(3, 4));
        sl4.defineLineWithTwoPoints(new Point(0, 0), new Point(4, 4));

        System.out.println("Angle: " + sl3.angle);
        System.out.println("Angle: " + sl4.angle);
        try
        {
            System.out.println("Intersection X: " + sl3.intersection(sl4).getX());
            System.out.println("Intersection Y: " + sl3.intersection(sl4).getY());
        }
        catch (Exception ex)
        {
            Logger.getLogger(StraightLine.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Test-Case 2
        StraightLine sl5 = new StraightLine();
        StraightLine sl6 = new StraightLine();
        sl5.defineLineWithTwoPoints(new Point(2, 0), new Point(0, 2));
        sl6.defineLineWithTwoPoints(new Point(1, 0), new Point(1, 2));

        System.out.println("Angle: " + sl5.angle);
        System.out.println("Angle: " + sl6.angle);
        try
        {
            System.out.println("Intersection X: " + sl5.intersection(sl6).getX());
            System.out.println("Intersection Y: " + sl5.intersection(sl6).getY());
        }
        catch (Exception ex)
        {
            Logger.getLogger(StraightLine.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Shift Line Test rising
        StraightLine sl7 = new StraightLine();
        sl7.defineLineWithTwoPoints(new Point(2, 2), new Point(5, 5));
        System.out.println(sl7);
        sl7.shiftLineParallel90Degree(-3);
        System.out.println(sl7);
        
        //Shift Line test sinking
        StraightLine sl8 = new StraightLine();
        sl8.defineLineWithTwoPoints(new Point(2, 4), new Point(5, 3));
        System.out.println(sl8);
        sl8.shiftLineParallel90Degree(-10);
        System.out.println(sl8);
    }
}
