
package Laser.GUI;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Point;

/**
 *
 * @author simon.buehlmann
 */
public class View extends Frame
{
    private VerticalDisplayBarView d;
    private FieldMonitorView monitor;
    
    public View()
    {
        this.setSize(1100, 1100);
        monitor = new FieldMonitorView();
        this.add(monitor);
        this.setVisible(true);
    }
    
    public void setLaserMap(Point[] data)
    {
        monitor.setData(data);
        monitor.repaint();
    }
    
    public void setMonitorAreaColor(Color color)
    {
        monitor.setAreaColor(color);
    }
    
    public void setValueClosestCoord(int value)
    {
        monitor.setValueClosestCoord(value);
    }
}
