
package environmentSensing.newGUI.view;

import References.AReferencePoint;
import References.view.GUIReference;
import environmentSensing.Environment;
import environmentSensing.positioning.positionEvaluation.StraightLine;
import environmentSensing.positioning.positionEvaluation.Wall;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author simon.buehlmann
 */
public class WallLayer extends ALayer
{
    private List<Wall> wallsForDrawing;
    private Environment environment;
    
    public WallLayer()
    {
        super(new GUIReference(-5000, 5000, 20, 10000, 5000));
        this.wallsForDrawing = new ArrayList<>();
        this.environment = Environment.getInstance();
    }
    
    public List<Wall> getWallsForDrawing()
    {
        return this.wallsForDrawing;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        for(Wall wall : this.wallsForDrawing)
        {
            this.drawStraightLine(wall.getMainLine(), this.environment, g, super.getGUIReference(), Color.BLACK);
            this.drawStraightLine(wall.getOuterTolLine(), this.environment, g, super.getGUIReference(), Color.RED);
            this.drawStraightLine(wall.getInnerTolLine(), this.environment, g, super.getGUIReference(), Color.GREEN);
        }
    }
    
    private void drawStraightLine(StraightLine line, Environment environment, Graphics g, GUIReference guiReference, Color color)
    {
        g.setColor(color);
        
        // Case 1
        if(line.getAngle() >= 45 && line.getAngle() <= 135)
        {
            try
            {
                AReferencePoint corner = environment.getCorner(Environment.EnvironmentCorner.CORNER_45);
                int x = line.getXOnDefinedY(corner.getAbsolutY());
                
                Point tempPoint1 = new Point(x, corner.getAbsolutY());
                
                corner = environment.getCorner(Environment.EnvironmentCorner.CORNER_135);
                x = line.getXOnDefinedY(corner.getAbsolutY());
                
                Point tempPoint2 = new Point(x, corner.getAbsolutY());
                
                // Transform Points to GUI Points
                tempPoint1 = guiReference.calculateGuiPoint(line.getReference(), tempPoint1);
                tempPoint2 = guiReference.calculateGuiPoint(line.getReference(), tempPoint2);
                
                g.drawLine(tempPoint1.x, tempPoint1.y, tempPoint2.x, tempPoint2.y);
            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }
        
        // Case 2
        if(line.getAngle() < 45 || line.getAngle() > 135)
        {
            try
            {
                AReferencePoint corner = environment.getCorner(Environment.EnvironmentCorner.CORNER_45);
                int y = line.getYOnDefinedX(corner.getAbsolutX());
                
                Point tempPoint1 = new Point(corner.getAbsolutX(), y);
                
                corner = environment.getCorner(Environment.EnvironmentCorner.CORNER_315);
                y = line.getYOnDefinedX(corner.getAbsolutX());
                
                Point tempPoint2 = new Point(corner.getAbsolutX(), y) ;
                
                // Transform Points to GUI Points
                tempPoint1 = guiReference.calculateGuiPoint(line.getReference(), tempPoint1);
                tempPoint2 = guiReference.calculateGuiPoint(line.getReference(), tempPoint2);
                
                g.drawLine(tempPoint1.x, tempPoint1.y, tempPoint2.x, tempPoint2.y);
            }
            catch (Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }
    }
}
