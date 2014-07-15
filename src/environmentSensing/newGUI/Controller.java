
package environmentSensing.newGUI;

import References.AbsoluteReferencePoint;
import References.ReferencePoint;
import environmentSensing.newGUI.view.WallLayer;
import environmentSensing.newGUI.view.MeasurementPointsLayer;
import environmentSensing.newGUI.view.ReferencePointsLayer;
import environmentSensing.positioning.DTOPosition;
import environmentSensing.positioning.NoReferenceException;
import environmentSensing.positioning.positionEvaluation.DetectionResult;
import environmentSensing.positioning.positionEvaluation.PositionEvaluator;
import environmentSensing.positioning.positionEvaluation.Wall;
import environmentSensing.positioning.positionEvaluation.WallDetector;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author simon.buehlmann
 */
public class Controller implements Observer, ActionListener
{
    // GUI Elements
    private JButton b_force_positioning;
    
    // Views
    private JFrame testwindow;
    private ReferencePointsLayer refPoints_Layer;
    private MeasurementPointsLayer measPoints_Layer;
    private WallLayer detectedWalls_Layer;
    
    // Modell
    private PositionEvaluator posEvaluator;

    public Controller()
    {
        testwindow = new JFrame();
        b_force_positioning = new JButton("Force Positioning");
        b_force_positioning.setLocation(10, 400);
        b_force_positioning.setSize(200, 20);
        b_force_positioning.setVisible(true);
        b_force_positioning.addActionListener(this);
        
        this.refPoints_Layer = new ReferencePointsLayer();
        this.measPoints_Layer = new MeasurementPointsLayer();
        this.detectedWalls_Layer = new WallLayer();
        
        testwindow.add(b_force_positioning);
        testwindow.add(this.refPoints_Layer);
        testwindow.add(this.measPoints_Layer);
        testwindow.add(this.detectedWalls_Layer);
        testwindow.setLayout(null);
        testwindow.setVisible(true);
        
        this.posEvaluator  = new PositionEvaluator();
        this.posEvaluator.addWallDetectorObserver(this);
        
        WallDetector wd = new WallDetector(0, 0);
        wd.addObserver(this);
        wd.detectWalls();
    }
    
    @Override
    public void update(Observable o, Object arg)
    {
        DetectionResult result = (DetectionResult)arg;
        this.detectedWalls_Layer.getWallsForDrawing().clear();
        
        for(Wall wall : result.getValidDetectedWalls())
        {
            this.detectedWalls_Layer.getWallsForDrawing().add(wall);
        }
        
        this.detectedWalls_Layer.repaint();
    }
    
    public static void main(String[] args)
    {
        new Controller();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        try
        {
            DTOPosition dto = this.posEvaluator.evaluatePosition(new ReferencePoint(0, 0, 95, AbsoluteReferencePoint.getInstance()));
            ReferencePoint posRef = new ReferencePoint(dto.getX(), dto.getY(), dto.getAngle(), AbsoluteReferencePoint.getInstance());
            
            this.refPoints_Layer.getPointsForDrawing().clear();
            this.refPoints_Layer.getPointsForDrawing().add(posRef);
            this.refPoints_Layer.repaint();
        }
        catch (NoReferenceException ex)
        {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
