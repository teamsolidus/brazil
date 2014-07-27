package environmentSensing.gui.views;

import References.AReferencePoint;
import References.view.GUIReference;
import environmentSensing.Environment;
import environmentSensing.positioning.positionEvaluation.wall.StraightLine;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author simon.buehlmann
 */
public class StraightLineView implements IDrawingElement
{
    private GUIReference guiReference;
    
    public StraightLineView(GUIReference guiReference)
    {
        this.guiReference = guiReference;
    }

    public void drawStraightLine(StraightLine line, Graphics g, Color color)
    {
        /*g.setColor(color);
        
        // Case 1
        if (line.getAngle() >= 45 && line.getAngle() <= 135)
        {
        int this.guiReference.getGuiWidth()
        
        
        try
        {
        int x = line.getXOnDefinedY(corner.getAbsolutY());
        
        Point tempPoint1 = new Point(x, corner.getAbsolutY());
        
        corner = environment.getCorner(Environment.EnvironmentCorner.CORNER_135);
        x = line.getXOnDefinedY(corner.getAbsolutY());
        
        Point tempPoint2 = new Point(x, corner.getAbsolutY());
        
        // Transform Points to GUI Points
        tempPoint1 = guiReference.calculateGuiPoint(line.getReference(), tempPoint1);
        tempPoint2 = guiReference.calculateGuiPoint(line.getReference(), tempPoint2);
        
        g.drawLine(tempPoint1.x, tempPoint1.y, tempPoint2.x, tempPoint2.y);
        }
        catch (Exception ex)
        {
        System.out.println(ex.getMessage());
        }
        }
        
        // Case 2
        if (line.getAngle() < 45 || line.getAngle() > 135)
        {
        try
        {
        AReferencePoint corner = environment.getCorner(Environment.EnvironmentCorner.CORNER_45);
        int y = line.getYOnDefinedX(corner.getAbsolutX());
        
        Point tempPoint1 = new Point(corner.getAbsolutX(), y);
        
        corner = environment.getCorner(Environment.EnvironmentCorner.CORNER_315);
        y = line.getYOnDefinedX(corner.getAbsolutX());
        
        Point tempPoint2 = new Point(corner.getAbsolutX(), y);
        
        // Transform Points to GUI Points
        tempPoint1 = guiReference.calculateGuiPoint(line.getReference(), tempPoint1);
        tempPoint2 = guiReference.calculateGuiPoint(line.getReference(), tempPoint2);
        
        g.drawLine(tempPoint1.x, tempPoint1.y, tempPoint2.x, tempPoint2.y);
        }
        catch (Exception ex)
        {
        System.out.println(ex.getMessage());
        }
        }*/
    }

    @Override
    public void draw(Graphics g)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
