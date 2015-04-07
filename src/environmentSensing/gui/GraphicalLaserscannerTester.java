
package environmentSensing.gui;

import environmentSensing.gui.layers.LayerArea;
import environmentSensing.gui.model.ILaserscannerFascadeListener;
import environmentSensing.gui.model.LaserscannerException;
import environmentSensing.gui.model.LaserscannerFascade;
import environmentSensing.gui.operator.ConnectLaserArea;
import environmentSensing.gui.operator.filter.FilterArea;
import environmentSensing.gui.operator.ZoomArea;
import environmentSensing.gui.operator.OperatorArea;
import environmentSensing.scandatahandling.coordinates.CoordinatesCalculater;
import environmentSensing.scandatahandling.coordinates.CoordinatesScandata;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import javax.swing.Box.Filler;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 *
 * @author Simon Bühlmann
 */
public class GraphicalLaserscannerTester extends JFrame implements ILaserscannerFascadeListener
{
    // modell
    private LaserscannerFascade laserscanner;
    
    // gui elements
    private LayerArea layerArea;
    private ConnectLaserArea connectLaserArea;
    private OperatorArea operatorArea;
    private ZoomArea mapArea;
    private FilterArea filterArea;

    private JSplitPane horizontalSplit;   
    
    // laser elements
    private CoordinatesCalculater coordinatesCalculater;
    
    public GraphicalLaserscannerTester() throws LaserscannerException
    {
        this.laserscanner = new LaserscannerFascade();
        this.laserscanner.addListener(this);
        
        // init gui objects
        this.horizontalSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        this.layerArea = new LayerArea();
        this.connectLaserArea = new ConnectLaserArea(this.laserscanner);
        this.operatorArea = new OperatorArea(this.laserscanner);
        this.mapArea = new ZoomArea(this.layerArea);
        this.filterArea = new FilterArea(this.laserscanner);
        
        JPanel leftSplitPanel = new JPanel(new BorderLayout());
        
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.add(this.connectLaserArea);
        Dimension dimension = new Dimension(200, 23);
        menuPanel.add(new Filler(dimension, dimension, dimension));
        menuPanel.add(this.operatorArea);
        menuPanel.add(new Filler(dimension, dimension, dimension));
        menuPanel.add(this.mapArea);
        menuPanel.add(new Filler(dimension, dimension, dimension));
        menuPanel.add(this.filterArea);
        
        leftSplitPanel.add(menuPanel, BorderLayout.PAGE_START);
        
        this.horizontalSplit.setLeftComponent(leftSplitPanel);
        this.horizontalSplit.setRightComponent(this.layerArea);
        
        this.operatorArea.setEnabled(false);
        
        this.add(horizontalSplit);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        this.setVisible(true);
        
        // init other objects
        this.coordinatesCalculater = new CoordinatesCalculater();
    }
    
    @Override
    public void newMeasurementData(List<CoordinatesScandata> datas)
    {
        this.layerArea.getMeasurementDataLayer().setScandataSet(datas);
        this.layerArea.repaint();
    }

    @Override
    public void laserConnectionChanged(boolean isConnected)
    {
        this.operatorArea.setEnabled(isConnected);
        this.connectLaserArea.setConnectionState(isConnected);
    }
    
    public static void main(String[] args) throws LaserscannerException
    {
        GraphicalLaserscannerTester graphicalLaserscannerTester = new GraphicalLaserscannerTester();
    }
}
