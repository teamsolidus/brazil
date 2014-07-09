
package environmentSensing.newGUI.view;

import References.view.GUIReference;
import environmentSensing.NewLaser.LaserFactory;
import environmentSensing.NewLaser.MeasuredResult;
import environmentSensing.NewLaser.ReflectionPoint;
import environmentSensing.positioning.positionEvaluation.IEnvironmentSensor;
import environmentSensing.positioning.positionEvaluation.dummyScenarios.Scenario1;
import environmentSensing.positioning.positionEvaluation.dummyScenarios.Scenario2;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JPanel;

/**
 *
 * @author simon.buehlmann
 */
public class MeasurementPointsLayer extends JPanel
{
    // Constantes
    private final int DIAMETER_REFERENCE_POINT;
    
    // Objects
    private GUIReference guiReference;
    private IEnvironmentSensor environmentSensor;
    
    // Constructor
    public MeasurementPointsLayer()
    {
        this.DIAMETER_REFERENCE_POINT = 2;
        this.guiReference = new GUIReference(-5000, 5000, 20, 10000, 5000);
        
        this.setSize(this.guiReference.getGuiLength(), this.guiReference.getGuiWidth());
        this.setLayout(null);
        this.setLocation(0, 0);
        this.setVisible(true);
        
        this.environmentSensor = LaserFactory.getInstance().getEnvironmentSensor();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponents(g);
        this.drawMeasuredResult(this.environmentSensor.getEnvironmentReflections(), g, guiReference);
    }
    
    
    
    private void drawReflectionPoint(ReflectionPoint reflection, Graphics g, GUIReference guiReference)
    {
        Point newPoint = guiReference.calculateGuiPoint(reflection.getReference(), reflection.getPosition());
        
        g.setColor(Color.BLUE);
        g.fillOval(newPoint.x-DIAMETER_REFERENCE_POINT/2, newPoint.y-DIAMETER_REFERENCE_POINT/2, DIAMETER_REFERENCE_POINT, DIAMETER_REFERENCE_POINT);
    }
    
    private void drawMeasuredResult(MeasuredResult result, Graphics g, GUIReference guiReference)
    {
        for(ReflectionPoint refPoint : result.getReflectionPoints())
        {
            this.drawReflectionPoint(refPoint, g, guiReference);
        }
    }
}
