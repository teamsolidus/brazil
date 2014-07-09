
package environmentSensing.gui;

import References.view.GUIReference;
import environmentSensing.NewLaser.MeasuredResult;
import environmentSensing.NewLaser.ReflectionPoint;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author simon.buehlmann
 */
public class LaserReflectionPointView
{
    // Constantes
    private final int DIAMETER_REF_POINT;
    
    // Constructor
    public LaserReflectionPointView()
    {
        this.DIAMETER_REF_POINT = 4;
    }
    
    public void drawReflectionPoint(ReflectionPoint reflection, Graphics g, GUIReference guiReference)
    {
        Point newPoint = guiReference.calculateGuiPoint(reflection.getReference(), reflection.getPosition());
        
        g.setColor(Color.BLUE);
        g.fillOval(newPoint.x, newPoint.y, DIAMETER_REF_POINT, DIAMETER_REF_POINT);
    }
    
    public void drawMeasuredResult(MeasuredResult result, Graphics g, GUIReference guiReference)
    {
        for(ReflectionPoint refPoint : result.getReflectionPoints())
        {
            this.drawReflectionPoint(refPoint, g, guiReference);
        }
    }
}
