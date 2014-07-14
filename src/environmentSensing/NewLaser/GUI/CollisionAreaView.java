
package Laser.GUI;

import Laser.References.ReferencePoint;
import Laser.References.RelativeReferencePoint;
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
    public CollisionAreaView(ReferencePoint ref, int posX, int posY, int scaleFactor)
    {
        this.ref = new RelativeReferencePoint(posX, posY, 0, ref);
        
        this.sizeX = 580;
        this.sizeY = 1250;
        
        this.scaledSizeX = sizeX/scaleFactor;
        this.scaledSizeY = sizeY/scaleFactor;
        
        int testerX = this.ref.getX(ReferencePoint.Type.ABSOLUTE);
        int testerY = this.ref.getY(ReferencePoint.Type.ABSOLUTE);
        
        this.xToRef = this.ref.getX(ReferencePoint.Type.ABSOLUTE) - scaledSizeX/2;
        this.yToRef = ((this.ref.getY(ReferencePoint.Type.ABSOLUTE)*-1) - scaledSizeY );
        
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
