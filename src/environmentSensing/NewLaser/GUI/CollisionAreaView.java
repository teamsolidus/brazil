
package environmentSensing.NewLaser.GUI;

import References.AReferencePoint;
import References.ReferencePoint;
import java.awt.Color;
import java.awt.Graphics;

public class CollisionAreaView
{
    private ReferencePoint ref;
    
    private int sizeX, sizeY;
    private int scaledSizeX, scaledSizeY;
    private int xToRef, yToRef;
    
    private Color color;
    
    /**
     * 
     * @param ref Bezugspunkt für Positionierung auf internen Nullpunkt
     * @param posX
     * @param scaleFactor
     * @param posY
     */
    public CollisionAreaView(AReferencePoint ref, int posX, int posY, int scaleFactor)
    {
        this.ref = new ReferencePoint(posX, posY, 0, ref);
        
        this.sizeX = 580;
        this.sizeY = 1250;
        
        this.scaledSizeX = sizeX/scaleFactor;
        this.scaledSizeY = sizeY/scaleFactor;
        
        int testerX = this.ref.getX();
        int testerY = this.ref.getY();
        
        this.xToRef = this.ref.getX() - scaledSizeX/2;
        this.yToRef = ((this.ref.getY()*-1) - scaledSizeY );
        
        this.color = Color.BLUE;
    }
    
    public void setColor(Color color)
    {
        this.color = color;
    }
    
    public void paintArea(Graphics g)
    {   
        g.setColor(this.color);
        g.fillRect(xToRef, yToRef, scaledSizeX, scaledSizeY);
    }
    
}
