
package environmentSensing.gui;

import References.AReferencePoint;
import References.ReferencePoint;
import References.view.GUIReference;
import environmentSensing.Environment;
import environmentSensing.positioning.positionEvaluation.StraightLine;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author simon.buehlmann
 */
public class StraightLineView
{
    public StraightLineView()
    {
    }
    
    public void drawStraightLine(StraightLine line, Environment environment, Graphics g, GUIReference guiReference)
    {
        g.setColor(Color.BLACK);
        
        // Case 1
        if(line.getAngle() >= 45 && line.getAngle() <= 135)
        {
            try
            {
                AReferencePoint corner = environment.getCorner(Environment.EnvironmentCorner.CORNER_45);
                int yCorner45 = corner.getAbsolutY();
                int x = line.getXOnDefinedY(yCorner45);
                
                Point tempPoint1 = new Point(x, yCorner45);
                
                Point tempPoint2 = new Point(line.getXOnDefinedY(environment.getCorner(Environment.EnvironmentCorner.CORNER_135).getAbsolutY()),
                        environment.getCorner(Environment.EnvironmentCorner.CORNER_45).getAbsolutY());
            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }
            
            Point tempPoint1 = guiReference.calculateGuiPoint(environment.getCorner(Environment.EnvironmentCorner.CORNER_45));
            Point tempPoint2 = guiReference.calculateGuiPoint(environment.getCorner(Environment.EnvironmentCorner.CORNER_135));
            g.drawLine(tempPoint1.x, tempPoint1.y, tempPoint2.x, tempPoint2.y);
        }
        
        // Case 2
        if(line.getAngle() < 45 || line.getAngle() > 135)
        {
            Point tempPoint1 = guiReference.calculateGuiPoint(environment.getCorner(Environment.EnvironmentCorner.CORNER_45));
            Point tempPoint2 = guiReference.calculateGuiPoint(environment.getCorner(Environment.EnvironmentCorner.CORNER_315));
            g.drawLine(tempPoint1.x, tempPoint1.y, tempPoint2.x, tempPoint2.y);
        }
    }
}
