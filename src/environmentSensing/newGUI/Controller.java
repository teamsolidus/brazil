
package environmentSensing.newGUI;

import environmentSensing.newGUI.view.WallLayer;
import environmentSensing.newGUI.view.MeasurementPointsLayer;
import environmentSensing.newGUI.view.ReferencePointsLayer;
import environmentSensing.positioning.positionEvaluation.DetectionResult;
import environmentSensing.positioning.positionEvaluation.PositionEvaluator;
import environmentSensing.positioning.positionEvaluation.StraightLine;
import environmentSensing.positioning.positionEvaluation.Wall;
import environmentSensing.positioning.positionEvaluation.WallDetector;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;

/**
 *
 * @author simon.buehlmann
 */
public class Controller implements Observer
{
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
        
        this.refPoints_Layer = new ReferencePointsLayer();
        this.measPoints_Layer = new MeasurementPointsLayer();
        this.detectedWalls_Layer = new WallLayer();
        
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
}
