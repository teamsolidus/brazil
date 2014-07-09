
package References.view;

import References.AReferencePoint;
import References.ReferencePoint;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author simon.buehlmann
 */
public class ReferencePointView
{
    private static int DIAMETER = 20;
    private GUIReference guiReference;
    
    public ReferencePointView(GUIReference guiReference)
    {
        this.guiReference = guiReference;
    }
    
    public void drawReferencePoint(ReferencePoint drawingReference, Graphics g)
    {        
        int x = guiReference.calculateGuiXPosition(drawingReference) - DIAMETER/2;
        int y = guiReference.calculateGuiYPosition(drawingReference) - DIAMETER/2;

        g.setColor(Color.BLACK);
        g.fillOval(x, y, DIAMETER, DIAMETER);
        
        g.setColor(Color.PINK);
        System.out.println(g.getColor());
        g.fillArc(x+2, y+2, DIAMETER-4, DIAMETER-4, 0, 90);
        g.fillArc(x+2, y+2, DIAMETER-4, DIAMETER-4, 180, 90);
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
