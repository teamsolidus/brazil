
package environmentSensing.gui.views;

import References.AbsoluteReferencePoint;
import References.view.GUIReference;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * Shows the measured Points from the Laser
 * @author simon.buehlmann
 */
public class MeasurementPointsView implements IDrawingElement
{
    // Constantes
    private final int DIAMETER_REFERENCE_POINT;
    
    // DrawingData
    private Point[] drawingPoints;
    
    // Objects
    private GUIReference guiReference;
    
    // Constructor
    public MeasurementPointsView(GUIReference guiReference)
    {
        this.DIAMETER_REFERENCE_POINT = 3;
        this.guiReference = guiReference;
        this.drawingPoints = new Point[]
        {
            new Point(0, 100)
        };
    }
    
    public void setDrawingData(Point[] points)
    {
        this.drawingPoints = points;
    }
    
    private void drawReflectionPoint(Point reflection, Graphics g, GUIReference guiReference)
    {
        Point newPoint = guiReference.calculateGuiPoint(AbsoluteReferencePoint.getInstance(), reflection);
        
        g.setColor(Color.BLUE);
        g.fillOval(newPoint.x-DIAMETER_REFERENCE_POINT/2, newPoint.y-DIAMETER_REFERENCE_POINT/2, DIAMETER_REFERENCE_POINT, DIAMETER_REFERENCE_POINT);
    }
    
    private void drawMeasuredResult(Point[] result, Graphics g, GUIReference guiReference)
    {
        for(Point refPoint : result)
        {
            this.drawReflectionPoint(refPoint, g, guiReference);
        }
    }

    @Override
    public void draw(Graphics g)
    {
        this.drawMeasuredResult(this.drawingPoints, g, guiReference);
    }
}
