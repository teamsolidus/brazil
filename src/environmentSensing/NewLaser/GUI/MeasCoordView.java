
package environmentSensing.NewLaser.GUI;

import References.AReferencePoint;
import References.ReferencePoint;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author simon.buehlmann
 */
public class MeasCoordView
{
    private ReferencePoint ref;
    
    private int sizeX, sizeY;
    private int scaleFactor;
    private int diameter;
    
    private Point[] coord;
    private Color color;
    
    /**
     * 
     * @param ref Bezugspunkt für Positionierung auf internen Nullpunkt
     * @param posX
     * @param scaleFactor
     * @param posY
     */
    public MeasCoordView(AReferencePoint ref, int posX, int posY, int scaleFactor)
    {
        this.ref = new ReferencePoint(posX, posY, 0, ref);
        
        this.sizeX = posX * 2;
        this.sizeY = 1000;
        this.diameter = 3;
        
        this.scaleFactor = scaleFactor;
        
        this.color = Color.BLUE;
    }
    
    public void setCoord(Point[] coord)
    {
        this.coord = coord;
    }
    
    public void paintArea(Graphics g)
    {   
        g.setColor(this.color);

        if (coord != null)
        {
            for (int countFor = 0; countFor < coord.length; countFor++)
            {
                int refX = this.ref.getX();
                int refY = this.ref.getY() * -1;//minus one for GUI
                int tempX = (int)coord[countFor].getX()/ this.scaleFactor;
                int tempY = ((int)coord[countFor].getY() * -1)/ this.scaleFactor;

                g.fillOval(tempX + refX, tempY + refY, diameter, diameter);
            }
        }
    }
    
}
