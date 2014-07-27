
package environmentSensing.gui;

import References.view.GUIReference;
import environmentSensing.NewLaser.Interpretation.Measurement;
import environmentSensing.gui.views.LaserView;
import environmentSensing.gui.views.MeasurementPointsView;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Point;

/**
 *
 * @author simon.buehlmann
 */
public class DrawingLayer extends Panel
{
    private GUIReference guiReference;
    
    // Drawing Elements
    private MeasurementPointsView measurementPointsView;
    
    public DrawingLayer()
    {
        this.guiReference = new GUIReference(-5000, 5000, 20, 10000, 5000);
        
        this.setSize(this.guiReference.getGuiLength(), this.guiReference.getGuiWidth());
        this.setLayout(null);
        this.setBackground(Color.GRAY);
        this.setLocation(0, 0);
        this.setVisible(true);
        
        // Drawing Elements
        this.measurementPointsView = new MeasurementPointsView(this.guiReference);
    }

    @Override
    public void paint(Graphics g)
    {
        // Drawing Elements
        this.measurementPointsView.draw(g);
    }
    
    public void setDrawingReflectionPoints(Point[] points)
    {
        this.measurementPointsView.setDrawingData(points);
    }
}
