
package environmentSensing.gui;

import environmentSensing.EnvironmentSensingFactory;
import environmentSensing.NewLaser.Solidus.LaserFactory;
import environmentSensing.collisionDetection.CollisionDetector;
import environmentSensing.gui.views.LaserView;
import java.awt.Frame;

/**
 *
 * @author simon.buehlmann
 */
public class LaserFrame extends Frame
{
    private LaserView collisionView;
    private LaserViewController collisionViewController;
    
    public LaserFrame()
    {
        this.setLayout(null);
        this.setSize(750, 350);
        this.collisionView = new LaserView();

        this.add(this.collisionView);
        this.setVisible(true);
        
        this.collisionViewController = new LaserViewController(EnvironmentSensingFactory.getInstance().getCollisionDetector(), 
                LaserFactory.getInstance().getLaser(), 
                collisionView);
        
        
    }
    
    public static void main(String[] args)
    {
        new LaserFrame();
    }
}
