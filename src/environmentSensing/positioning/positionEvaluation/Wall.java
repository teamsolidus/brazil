package environmentSensing.positioning.positionEvaluation;

import References.AReferencePoint;
import References.ReferencePoint;
import environmentSensing.NewLaser.ReflectionPoint;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author simon.buehlmann
 */
public class Wall
{

    private AReferencePoint reference;

    //Lines
    private StraightLine mainLine;
    private StraightLine outerTolLine;
    private StraightLine innerTolLine;
    private StraightLine controllLine;

    //Tolerances
    private int symetricTol;
    private int rssiTol;

    private int minSuccessivePoints;

    //ReflectionPoints
    private List<ReflectionPoint> associatedPoints;

    public Wall(AReferencePoint reference, ReflectionPoint pointA, ReflectionPoint pointB)
    {
        //Define tolerances
        this.symetricTol = 50;
        this.rssiTol = 5;
        this.minSuccessivePoints = 3;
        
        this.reference = reference;

        //Generate lines
        this.mainLine = new StraightLine(reference, pointA.getPosition(), pointB.getPosition());
        this.controllLine = new StraightLine();
        this.calculateTolerances();

        //ReflectionPoints Buffer
        this.associatedPoints = new ArrayList();
    }

    public boolean reflectionPointBelongsToWall(ReflectionPoint reflectionPoint)
    {
        //In Future: Check RSSI Tolerance

        //Check the position
        return this.pointBelongsToFunction(reflectionPoint.getPosition());
    }

    public void addReflectionPoint(ReflectionPoint reflectionPoint)
    {
        this.associatedPoints.add(reflectionPoint);
        this.correctDifferations(reflectionPoint.getPosition());
    }

    public boolean enoughDefinitionPoints()
    {
        return (this.associatedPoints.size() >= this.minSuccessivePoints);
    }

    //private Methods
    private boolean pointBelongsToFunction(Point point)
    {
        // Shift ControllLine
        this.controllLine = this.mainLine.cloneParameters(this.controllLine);
        this.controllLine.setBasePoint(point);

        // Check
        // Step1: If the main line is horizontal, only the Y must be checked
        if (this.mainLine.getAngle() == 0)
        {
            return (point.getY() < this.outerTolLine.getBasePoint().getY() && point.getY() > this.innerTolLine.getBasePoint().getY());
        }

        // Step 2: If the main line is vertical, only the X must be checked
        if (this.mainLine.getAngle() == 90)
        {
            return (point.getX() > this.outerTolLine.getBasePoint().getX() && point.getX() > this.innerTolLine.getBasePoint().getX());
        }

        // Step 3: If the Angle is >0 and <=45 degrees, calculate with Y-Axis
        if (this.mainLine.getAngle() <= 45 && this.mainLine.getAngle() > 0)
        {
            try
            {
                int intersectionPointYAxisOuterTol = (int) this.outerTolLine.getYOnDefinedX(0);
                int intersectionPointYAxisInnerTol = (int) this.innerTolLine.getYOnDefinedX(0);
                
                int intersectionPointYAxisControllLine = this.controllLine.getYOnDefinedX(0);
                
                // Where intersect the main line the axis?
                if(intersectionPointYAxisControllLine < 0)
                {
                    // switch: inner tol must be bigger, outer tol must be smaller
                    return (intersectionPointYAxisControllLine > intersectionPointYAxisOuterTol &&
                            intersectionPointYAxisControllLine < intersectionPointYAxisInnerTol);
                }
                
                return (intersectionPointYAxisControllLine < intersectionPointYAxisOuterTol &&
                        intersectionPointYAxisControllLine > intersectionPointYAxisInnerTol);
            }
            catch (Exception ex)
            {
                Logger.getLogger(Wall.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // Step 4: If the Angle is >45 and <180 degrees, calculate with X-Axis
        if (this.mainLine.getAngle() > 45 && this.mainLine.getAngle() < 180)
        {
            try
            {
                int intersectionPointXAxisOuterTol = (int) this.outerTolLine.getXOnDefinedY(0);
                int intersectionPointXAxisInnerTol = (int) this.innerTolLine.getXOnDefinedY(0);
                
                int intersectionPointXAxisControllLine = this.controllLine.getXOnDefinedY(0);
                
                // Where intersect the main line the axis?
                if(intersectionPointXAxisControllLine < 0)
                {
                    // switch: inner tol must be bigger, outer tol must be smaller
                    return (intersectionPointXAxisControllLine > intersectionPointXAxisOuterTol && 
                            intersectionPointXAxisControllLine < intersectionPointXAxisInnerTol);
                }
                
                return (intersectionPointXAxisControllLine < intersectionPointXAxisOuterTol && 
                        intersectionPointXAxisControllLine > intersectionPointXAxisInnerTol);
            }
            catch (Exception ex)
            {
                Logger.getLogger(Wall.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // Step 5: Default Return
        return false;
    }

    private void correctDifferations(Point p)
    {
        this.controllLine.defineLineWithTwoPoints(reference, this.mainLine.getBasePoint(), p);
        double angleDifferences = this.controllLine.getAngle() - this.mainLine.getAngle();
        this.mainLine.setAngle(this.mainLine.getAngle() + angleDifferences/2);
        this.calculateTolerances();
    }
    
    private void calculateTolerances()
    {
        this.outerTolLine = this.mainLine.cloneParameters(new StraightLine());
        //this.outerTolLine.shiftLineParallel90Degree(this.symetricTol);
        this.innerTolLine = this.mainLine.cloneParameters(new StraightLine());
        //this.innerTolLine.shiftLineParallel90Degree(this.symetricTol * -1);
        
        // Constant X
        if(this.mainLine.getAngle() == 90)
        {
            if(this.mainLine.getBasePoint().x > 0)
            {
                this.outerTolLine.shiftLineParallel90Degree(this.symetricTol);
                this.innerTolLine.shiftLineParallel90Degree(this.symetricTol * -1);
            }
            else
            {
                this.outerTolLine.shiftLineParallel90Degree(this.symetricTol * -1);
                this.innerTolLine.shiftLineParallel90Degree(this.symetricTol);
            }
            return;
        }
        
        // Constant Y
        if(this.mainLine.getAngle() == 0)
        {
            if(this.mainLine.getBasePoint().y > 0)
            {
                this.outerTolLine.shiftLineParallel90Degree(this.symetricTol);
                this.innerTolLine.shiftLineParallel90Degree(this.symetricTol * -1);
            }
            else
            {
                this.outerTolLine.shiftLineParallel90Degree(this.symetricTol * -1);
                this.innerTolLine.shiftLineParallel90Degree(this.symetricTol);
            }
            return;
        }
        
        // Rising & Sinking
        try
        {
            if(this.mainLine.getXOnDefinedY(0) > 0)
            {
                this.outerTolLine.shiftLineParallel90Degree(this.symetricTol);
                this.innerTolLine.shiftLineParallel90Degree(this.symetricTol * -1);
            }
            else
            {
                this.outerTolLine.shiftLineParallel90Degree(this.symetricTol * -1);
                this.innerTolLine.shiftLineParallel90Degree(this.symetricTol);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(Wall.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Overrides
    @Override
    public String toString()
    {
        return "Base Point X: " + this.mainLine.getBasePoint().getX() + " Base Point Y: " + this.mainLine.getBasePoint().getY() + " Angle: " + this.mainLine.getAngle();
    }
    
    // Public Methods
    public ReferencePoint checkIntersectionWithOtherWall(Wall other) throws Exception
    {
        Point tempPoint = this.mainLine.intersection(other.getMainLine());
        return new ReferencePoint(tempPoint.x, tempPoint.y, 0, this.reference);
    }
    
    /**
     * No Point should be further away as the outer tolerance. 
     * @param reflectionPoints
     * @return 
     */
    public boolean validateWall(List<ReflectionPoint> reflectionPoints)
    {
        // Check
        // Step1: If the main line is horizontal, only the Y must be checked
        if (this.mainLine.getAngle() == 0)
        {
            for(ReflectionPoint point : reflectionPoints)
            {
                if(point.getPosition().y > this.innerTolLine.getBasePoint().y)
                {
                    return false;
                }
            }
            return true;
        }
        
        // Step 2: If the main line is vertical, only the X must be checked
        if (this.mainLine.getAngle() == 90)
        {
            for(ReflectionPoint point : reflectionPoints)
            {
                if(point.getPosition().x > this.innerTolLine.getBasePoint().x)
                {
                    return false;
                }
            }
            return true;
        }

        // Step 3: No static value. Calculate with common reference        
        // Shift ControllLine
        this.controllLine = this.mainLine.cloneParameters(this.controllLine);// Same angle
        
        //If the Angle is >0 & <=45 or >= 135 & < 180 degrees, calculate with Y-Axis
        if (this.mainLine.getAngle() <= 45 && this.mainLine.getAngle() > 0 || this.mainLine.getAngle() >= 135 && this.mainLine.getAngle() < 180)
        {
            for(ReflectionPoint point : reflectionPoints)
            {
                //Shift controllfunction to point
                this.controllLine.setBasePoint(point.getPosition());
                
                try
                {
                    int toleranceComparablePoint = (int)this.outerTolLine.getXOnDefinedY(0);
                    int controllLineComparablePoint = this.controllLine.getXOnDefinedY(0);
                    
                    if(controllLineComparablePoint > toleranceComparablePoint)
                    {
                        return false;
                    }
                }
                catch (Exception ex)
                {
                    Logger.getLogger(Wall.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return true; // Valid!
        }
        
        //If the Angle is >45 and <135 degrees, calculate with X-Axis
        if (this.mainLine.getAngle() > 45 && this.mainLine.getAngle() < 180)
        {
           for(ReflectionPoint point : reflectionPoints)
            {
                //Shift controllfunction to point
                this.controllLine.setBasePoint(point.getPosition());
                
                try
                {
                    int toleranceComparablePoint = (int)this.outerTolLine.getYOnDefinedX(0);
                    int controllLineComparablePoint = this.controllLine.getYOnDefinedX(0);
                    
                    if(controllLineComparablePoint > toleranceComparablePoint)
                    {
                        return false;
                    }
                }
                catch (Exception ex)
                {
                    Logger.getLogger(Wall.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
           return true; // Valid!
        }
        
        // Step 5: Default Return
        return false;
    }

    // Getter & Setter
    public StraightLine getMainLine()
    {
        return this.mainLine;
    }
    
    public StraightLine getOuterTolLine()
    {
        return this.outerTolLine;
    }
    
    public StraightLine getInnerTolLine()
    {
        return this.innerTolLine;
    }
}
