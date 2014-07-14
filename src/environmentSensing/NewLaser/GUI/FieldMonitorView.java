package Laser.GUI;

import Laser.References.MainReferencePoint;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Point;

/**
 *
 * @author simon.buehlmann
 */
public class FieldMonitorView extends Panel
{

    private int[] data;
    private Point[] pointData;
    private int startAngle, angleSteps;
    private Color area;
    private int scaleFactor;

    //Draw Parameters
    private int diameter;
    
    private CollisionAreaView collisionArea;
    private MeasCoordView coord;
    
    private int closestToYValue;

    public FieldMonitorView()
    {
        //Panel
        this.setSize(1000, 1000);
        this.setBackground(Color.WHITE);
        
        this.diameter = 3;
        this.scaleFactor = 2;
        
        this.collisionArea = new CollisionAreaView(MainReferencePoint.getInstance(), 500, -1000, 10);
        this.coord = new MeasCoordView(MainReferencePoint.getInstance(), 500, -1000, 10);
        
        this.closestToYValue = 10000;
    }
    
    public void setAreaColor(Color color)
    {
        this.collisionArea.setColor(color);
    }
    
    public void setData(Point[] data)
    {
        this.coord.setCoord(data);
    }
    
    public void setValueClosestCoord(int value)
    {
        this.closestToYValue = value;
    }

    @Override
    public void paint(Graphics g)
    {
        g.clearRect(0, 0, 1000, 1000);
        
        //Colision Area
        this.collisionArea.paintArea(g);
        
        this.coord.paintArea(g);
        
        g.setColor(Color.BLACK);
        g.fillOval(490, 990, 20, 20);

        g.drawString("Min. Distance in Y: " + this.closestToYValue + " mm", 10, 950);
    }
}
