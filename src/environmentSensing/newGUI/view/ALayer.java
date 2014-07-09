
package environmentSensing.newGUI.view;

import References.view.GUIReference;
import javax.swing.JPanel;

/**
 *
 * @author simon.buehlmann
 */
public abstract class ALayer extends JPanel
{
    // Objects
    private GUIReference guiReference;
    
    // Constructor
    public ALayer(GUIReference guiReference)
    {
        this.guiReference = guiReference;
        
        this.setSize(this.guiReference.getGuiLength(), this.guiReference.getGuiWidth());
        this.setLayout(null);
        this.setLocation(0, 0);
        this.setVisible(true);
    }
    
    protected GUIReference getGUIReference()
    {
        return this.guiReference;
    }
}
