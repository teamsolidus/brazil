
package environmentSensing.newGUI.view;

import References.AReferencePoint;
import References.AbsoluteReferencePoint;
import References.LinearDependentReferencePoint;
import References.ReferencePoint;
import References.view.GUIReference;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author simon.buehlmann
 */
public class ReferencePointsLayer extends JPanel
{
    private final int DIAMETER;
    
    private GUIReference guiReference;
    
    private List<AReferencePoint> pointsToDrawing;
    
    AReferencePoint robo, laser;
    
    public ReferencePointsLayer()
    {
        this.DIAMETER = 20;
        this.guiReference = new GUIReference(-5000, 5000, 20, 10000, 6000);
        
        this.setSize(this.guiReference.getGuiLength(), this.guiReference.getGuiWidth());
        this.setLayout(null);
        this.setLocation(0, 0);
        this.setVisible(true);
        
        this.pointsToDrawing = new ArrayList<>();
        
        //robo = new ReferencePoint(-1000, 2500, 241, AbsoluteReferencePoint.getInstance());
        //laser = new LinearDependentReferencePoint(500, 0, 0, robo);
    }
    
    public List<AReferencePoint> getPointsForDrawing()
    {
        return this.pointsToDrawing;
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        this.paintComponents(g);
        this.drawAbsoluteReferencePoint(AbsoluteReferencePoint.getInstance(), g);
        
        for(AReferencePoint point : this.pointsToDrawing)
        {
            this.drawReferencePoint(point, g);
        }
        
        //this.drawReferencePoint(robo, g);
        //this.drawReferencePoint(laser, g);
    }
    
    public void drawReferencePoint(AReferencePoint drawingReference, Graphics g)
    {        
        int x = guiReference.calculateGuiXPosition(drawingReference) - DIAMETER/2;
        int y = guiReference.calculateGuiYPosition(drawingReference) - DIAMETER/2;
        int angle = drawingReference.getAbsolutAngle();

        g.setColor(Color.BLACK);
        g.fillOval(x, y, DIAMETER, DIAMETER);
        
        g.setColor(Color.RED);
        g.fillArc(x+2, y+2, DIAMETER-4, DIAMETER-4, 0+angle, 90);
        g.fillArc(x+2, y+2, DIAMETER-4, DIAMETER-4, 180+angle, 90);
    }
    
    public void drawAbsoluteReferencePoint(AReferencePoint drawingReference, Graphics g)
    {        
        int x = guiReference.calculateGuiXPosition(drawingReference) - DIAMETER/2;
        int y = guiReference.calculateGuiYPosition(drawingReference) - DIAMETER/2;
        
        g.setColor(Color.BLACK);
        g.fillOval(x-2, y-2, DIAMETER+4, DIAMETER+4);
        
        g.setColor(Color.WHITE);
        g.fillOval(x-1, y-1, DIAMETER+2, DIAMETER+2);
        
        g.setColor(Color.BLACK);
        g.fillOval(x, y, DIAMETER, DIAMETER);
        
        g.setColor(Color.WHITE);
        g.fillArc(x+2, y+2, DIAMETER-4, DIAMETER-4, 0, 90);
        g.fillArc(x+2, y+2, DIAMETER-4, DIAMETER-4, 180, 90);
    }
}
