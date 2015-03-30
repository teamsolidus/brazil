
package environmentSensing.gui.operator;

import References.view.GUIReference;
import environmentSensing.gui.layers.LayerArea;
import environmentSensing.gui.model.LaserscannerFascade;
import static java.awt.Component.TOP_ALIGNMENT;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Simon Bühlmann
 */
public class ZoomArea extends JPanel implements ActionListener
{
    private LayerArea layerArea;
    private int zoomStep;
    
    // gui elements
    private JButton buttonPlus;
    private JButton buttonMinus;
    
    private static final Dimension BUTTON_DIM = new Dimension(90, 24);
    
    // action commands
    private final static String ACTION_PLUS = "+";
    private final static String ACTION_MINUS = "-";
    
    public ZoomArea(LayerArea layerArea)
    {
        this.layerArea = layerArea;
        this.zoomStep = 200;

        GridBagLayout gridBagLayout = new GridBagLayout();
        this.setLayout(gridBagLayout);
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 2, 2, 2);
        
        this.buttonPlus = new JButton(ACTION_PLUS);
        this.buttonPlus.setPreferredSize(BUTTON_DIM);
        constraints.gridx = 0;
        constraints.gridy = 0;
        gridBagLayout.setConstraints(this.buttonPlus, constraints);
        this.add(this.buttonPlus);
        
        this.buttonMinus = new JButton(ACTION_MINUS);
        this.buttonMinus.setPreferredSize(BUTTON_DIM);
        constraints.gridx = 1;
        constraints.gridy = 0;
        gridBagLayout.setConstraints(this.buttonMinus, constraints);
        this.add(this.buttonMinus);
        
        this.setAlignmentY(TOP_ALIGNMENT);
        
        // add listener
        this.buttonMinus.addActionListener(this);
        this.buttonPlus.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case ACTION_MINUS:
                this.layerArea.getGUIReference().setScale(this.layerArea.getGUIReference().getGuiScale() + this.zoomStep);
                this.layerArea.repaint();
                break;
                
            case ACTION_PLUS:
                this.layerArea.getGUIReference().setScale(this.layerArea.getGUIReference().getGuiScale() - this.zoomStep);
                this.layerArea.repaint();
                break;
        }
    }
}
