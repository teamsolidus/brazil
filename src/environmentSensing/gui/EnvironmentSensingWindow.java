
package environmentSensing.gui;

import java.awt.Frame;
import javax.swing.JFrame;


/**
 *
 * @author simon.buehlmann
 */
public class EnvironmentSensingWindow extends JFrame
{
    public EnvironmentSensingWindow()
    {
        
    }
    
    public static void main(String[] args)
    {
        EnvironmentSensingWindow window = new EnvironmentSensingWindow();
        
        window.add(EnvironmentSensingImagingLayer.getInstance());        
        window.setLayout(null);
        
        window.validate();
        window.setVisible(true);
    }
}
