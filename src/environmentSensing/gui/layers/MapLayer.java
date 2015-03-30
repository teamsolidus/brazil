
package environmentSensing.gui.layers;

import References.AbsoluteReferencePoint;
import References.view.GUIReference;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 *
 * @author Simon Bühlmann
 */
public class MapLayer implements ILayer
{
    private GUIReference guiReference;
    
    private int diameter;
    
    public MapLayer(GUIReference guiReference)
    {
        this.guiReference = guiReference;
        this.diameter = 200;
    }
    
    @Override
    public void draw(Graphics g)
    {
        this.drawCircle(g, 200);
        this.drawCircle(g, 400);
        this.drawCircle(g, 600);
        this.drawCircle(g, 800);
        this.drawCircle(g, 1000);
    }
    
    // private methods
    /**
     * 
     * @param g
     * @param diameter unscaled!
     */
    private void drawCircle(Graphics g, int diameter)
    {
        Point guiReferencePoint = guiReference.calculatePointInGUI(AbsoluteReferencePoint.getInstance(), new Point(0, 0));
        int tempDiameter = guiReference.calculateGuiDistance(diameter);
        
        g.setColor(Color.GRAY);
        g.drawOval(guiReferencePoint.x - tempDiameter/2, 
                guiReferencePoint.y - tempDiameter/2, 
                tempDiameter, 
                tempDiameter);
        
        g.drawString(diameter + "mm", guiReferencePoint.x, guiReferencePoint.y - tempDiameter/2);
    }
}
