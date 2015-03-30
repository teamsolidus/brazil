
package References.view;

import References.AReferencePoint;
import java.awt.Point;

/**
 * Describes, how the GUI-Elements is positioned to the absolute null point and the diameter on the GUI.
 * @author simon.buehlmann
 */
public class GUIReference
{
    private int xPos, yPos;
    private int scale;
    private int guiWidth, guiHeight;
    
    /**
     * 
     * @param xPos Distance in pixels from the left top corner of the gui to the reference
     * @param yPos Distance in pixels from the left top corner of the gui to the reference
     * @param scale scale factor in 1000 / x
     * @param width width from gui in pixels
     * @param height height from gui in pixels
     */
    public GUIReference(int xPos, int yPos, int scale, int width, int height)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.scale = scale;
        this.guiWidth = width;
        this.guiHeight = height;
    }
    
    public Point calculatePointInGUI(AReferencePoint reference, Point point)
    {
        int tempX = this.calculateGuiDistance(reference.getAbsolutX() + point.x);
        int tempY = this.calculateGuiDistance(reference.getAbsolutY() - point.y);
        
        return new Point(this.xPos + tempX, this.yPos + tempY);
    }
    
    public int calculateGuiDistance(int value)
    {
        float realValue = (float)((float)value/(float)this.scale) * 1000;
        
        return (int)realValue;
    }
    
    // getter
    public int getGuiScale()
    {
        return this.scale;
    }
    
    public int getGuiWidth()
    {
        return this.guiHeight;
    }
    
    public int getGuiLength()
    {
        return this.guiWidth;
    }
    
    // setter
    public void setXPos(int xPos)
    {
        this.xPos = xPos;
    }

    public void setYPos(int yPos)
    {
        this.yPos = yPos;
    }

    public void setScale(int scale)
    {
        this.scale = scale;
    }

    public void setGUIWidth(int guiWidth)
    {
        this.guiWidth = guiWidth;
    }

    public void setGUIHeight(int guiHeight)
    {
        this.guiHeight = guiHeight;
    }
}
