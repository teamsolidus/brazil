
package Laser.GUI;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author simon.buehlmann
 */
public class VerticalDisplayBarView extends JPanel
{
    private int width, height, max, min, value, position;
    private Color barColor;
    
    public VerticalDisplayBarView(int width, int height, int max, int min, int value, Color barColor)
    {
        this.width = width;
        this.height = height;
        this.max = max;
        this.min = min;
        this.value = value;
        this.position = this.parseValueToPosition(this.value);
        
        this.barColor = barColor;
        
        this.setSize(this.width, this.height);
    }
    
    public void setValue(int value)
    {
        this.position = this.parseValueToPosition(value);
        this.repaint();
    }
    
    private int parseValueToPosition(int value)
    {
        int relValue = (value - min);
        int test = (10000/(max - min));
        int prozent =  test * relValue;
        return (height * prozent)/10000;
    }
    
    @Override
    public void paint(Graphics g)
    {
        g.setColor(this.barColor);
        g.fillRect(0, 0, this.width, this.position);
        
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, this.width, this.height);
    }
    
}
