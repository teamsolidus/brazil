
package References.view;

import References.AReferencePoint;
import java.awt.Point;

/**
 * Describes, how the GUI-Elements is positioned to the absolute null point and the diameter on the GUI.
 * @author simon.buehlmann
 */
public class GUIReference
{
    private final int X_POS, Y_POS;
    private final int GUI_SCALE;
    private final int GUI_WIDTH, GUI_HEIGHT;
    
    /**
     * 
     * @param x Not scaled Distance from ReferencePoint to GUI Reference
     * @param y Not scalled Distance from ReferencePoint to GUI Reference
     * @param scale 
     * @param width Not scaled width from gui
     * @param height Not scaled height from gui
     */
    public GUIReference(int x, int y, int scale, int width, int height)
    {
        this.X_POS = x;
        this.Y_POS = y;
        this.GUI_SCALE = scale;
        this.GUI_WIDTH = width/scale;
        this.GUI_HEIGHT = height/scale;
    }
    
    public int calculateGuiXPosition(AReferencePoint otherReference)
    {
        return this.calculateGuiXPosition(otherReference.getAbsolutX());
    }
    
    public int calculateGuiXPosition(int absoluteX)
    {
        return ((this.X_POS*-1) + absoluteX)/this.GUI_SCALE;
    }
    
    public int calculateGuiYPosition(AReferencePoint otherReference)
    {
        return this.calculateGuiYPosition(otherReference.getAbsolutY());
    }
    
    public int calculateGuiYPosition(int absoluteY)
    {
        return (this.Y_POS - absoluteY)/this.GUI_SCALE;
    }
    
    public Point calculateGuiPoint(AReferencePoint otherReference)
    {
        int tempX = ((this.X_POS*-1) + otherReference.getAbsolutX())/this.GUI_SCALE;
        int tempY = (this.Y_POS - otherReference.getAbsolutY())/this.GUI_SCALE;
        
        return new Point(tempX, tempY);
    }
    
    public Point calculateGuiPoint(AReferencePoint reference, Point point)
    {
        int tempX = ((this.X_POS*-1) + reference.getAbsolutX() + point.x)/this.GUI_SCALE;
        int tempY = (this.Y_POS - reference.getAbsolutY() - point.y)/this.GUI_SCALE;
        
        return new Point(tempX, tempY);
    }
    
    public int calculateGuiDistance(int value)
    {
        return value/this.GUI_SCALE;
    }
    
    // Getter & Setter
    public int getGuiScale()
    {
        return this.GUI_SCALE;
    }
    
    public int getGuiWidth()
    {
        return this.GUI_HEIGHT;
    }
    
    public int getGuiLength()
    {
        return this.GUI_WIDTH;
    }
    
    /**
     * Refer to the zero point, not to the gui reference. Unscaled!
     * @return 
     */
    public int getXRight()
    {
        return 0;
    }
}
