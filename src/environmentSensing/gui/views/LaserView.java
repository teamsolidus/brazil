
package environmentSensing.gui.views;

import environmentSensing.gui.DrawingLayer;
import environmentSensing.gui.views.percentDiagramm.IPercentDataProvider;
import environmentSensing.gui.views.percentDiagramm.PercentDiagrammView;
import java.awt.Button;
import java.awt.Panel;
import java.awt.Point;

/**
 *
 * @author simon.buehlmann
 */
public class LaserView extends Panel implements IPercentDataProvider
{
    // Views
    private PercentDiagrammView speedPercentDiagramm;
    private MeasurementPointsView measurementPointsView;
    private DrawingLayer drawingLayer;
    
    // Input Elements
    private Button b_init_laser;
    private Button b_force_positioning;
    
    // Buffer
    private byte speedPercent;
    
    public LaserView()
    {
        this.setSize(1000, 500);
        this.setLayout(null);
        
        this.speedPercent = 0;
        
        // Drawing Layer
        this.drawingLayer = new DrawingLayer();
        this.drawingLayer.setLocation(220, 50);
        this.add(drawingLayer);
        
        // Diagramm
        this.speedPercentDiagramm = new PercentDiagrammView(100, 200, (byte)0, this, 200, 100);
        this.speedPercentDiagramm.setLocation(10, 50);
        this.add(this.speedPercentDiagramm);
        
        // Buttons
        this.b_init_laser = new Button("Initalize Laser");
        this.b_init_laser.setSize(200, 30);
        this.b_init_laser.setLocation(10, 160);
        this.b_init_laser.setVisible(true);
        this.add(this.b_init_laser);
        
        this.b_force_positioning = new Button("Force Positioning");
        this.b_force_positioning.setSize(200, 30);
        this.b_force_positioning.setLocation(10, 200);
        this.b_force_positioning.setVisible(true);
        this.add(this.b_force_positioning);
        
        this.setLayout(null);
        this.setVisible(true);
    }
    
    public void setSpeedPercent(byte percent)
    {
        this.speedPercent = percent;
    }
    
    public void setReflectionPoints(Point[] points)
    {
        this.drawingLayer.setDrawingReflectionPoints(points);
        this.drawingLayer.repaint();
    }

    @Override
    public byte getSpeedPercent()
    {
        return this.speedPercent;
    }
}
