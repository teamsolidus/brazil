package environmentSensing.gui.layers;

import References.view.GUIReference;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Simon Bühlmann
 */
public class ColorBar implements ILayer
{
    private int max;
    private int min;
    
    private int lenght;
    private int height;
    
    private Point position;

    public ColorBar()
    {
        
    }
    
    @Override
    public void draw(Graphics g)
    {
        for(int cnt = 0; cnt < lenght; cnt++)
        {
            try
            {
                g.setColor(this.calculateColor(cnt));
                g.drawLine(position.x + cnt, position.y, position.x + cnt, position.y + height);
            }
            catch (Exception ex)
            {
                Logger.getLogger(ColorBar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private Color calculateColor(int value) throws Exception
    {
        int sectorSize = (max - min) / 4;

        int redLimit = min;
        int yellowLimit = sectorSize;
        int greenLimit = yellowLimit + sectorSize;
        int turqouisLimit = greenLimit + sectorSize;
        int blueLimit = max;
        
        float gradient = 255/sectorSize;
        
        if(value >= redLimit && value < yellowLimit)
        {
            int g = (int)((value - redLimit) * gradient);        
            return new Color(255, g, 0);
        }
        else if(value >= yellowLimit && value < greenLimit)
        {
            int r = (int)((value - yellowLimit) * (gradient * -1));        
            return new Color(r, 255, 0);
        }
        else if(value >= greenLimit && value < turqouisLimit)
        {
            int b = (int)((value - greenLimit) * gradient);        
            return new Color(0, 255, b);
        }
        else if(value >= turqouisLimit && value <= blueLimit)
        {
            int g = (int)((value - turqouisLimit) * (gradient * -1));        
            return new Color(0, g, 255);
        }
        else
        {
            throw new Exception();
        }
    }
}
