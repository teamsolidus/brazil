
package environmentSensing.gui.layers;

import References.view.GUIReference;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Simon Bühlmann
 */
public class LayerArea extends JPanel
{
    private GUIReference guiReference;
    
    // layers
    private MeasurementDataLayer measurementDataLayer;
    private MapLayer mapLayer;
    
    public LayerArea()
    {
        this.setSize(500, 500);
        this.setBackground(Color.yellow);
        this.setLayout(null);
        this.setVisible(true);
        
        this.guiReference = new GUIReference(250, 250, 1000, 500, 500);
        this.measurementDataLayer = new MeasurementDataLayer(this.guiReference);
        this.mapLayer = new MapLayer(this.guiReference);
    }
    
    // getter
    public MeasurementDataLayer getMeasurementDataLayer()
    {
        return measurementDataLayer;
    }
    
    public GUIReference getGUIReference()
    {
        return this.guiReference;
    }
    
    @Override
    public void paint(Graphics g)
    {
        this.guiReference.setGUIHeight(this.getSize().height);
        this.guiReference.setGUIWidth(this.getSize().width);
        this.guiReference.setXPos(this.getSize().width/2);
        this.guiReference.setYPos(this.getSize().height/2);
        
        System.out.println("GUI height" + this.getSize().height);
        System.out.println("GUI width" + this.getSize().width);
        System.out.println("GUI x" + this.getSize().width/2);
        System.out.println("GUI y" + this.getSize().height/2);
        System.out.println();
        
        super.paint(g);
        this.measurementDataLayer.drawMeasuredResult(g);
        this.mapLayer.draw(g);
    }
}
