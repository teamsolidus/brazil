
package environmentSensing.gui.operator.filter;

import environmentSensing.gui.model.LaserscannerFascade;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 *
 * @author Simon Bühlmann
 */
public class FilterDialog extends JDialog implements ActionListener
{
    private IFilterDialogListener listener;

    // gui elements
    private JLabel labelNrDataForAverage;
    private JSpinner spinnerNrDataForAverage;

    private JLabel labelIgnoreHighestAndLowest;
    private JCheckBox checkboxIgnoreHighestAndLowest;

    private JButton buttonSet;
    private JButton buttonCancel;

    // action commands
    private final static String ACTION_SET = "set";
    private final static String ACTION_CANCEL = "cancel";

    public FilterDialog(IFilterDialogListener listener)
    {
        this.listener = listener;

        this.setTitle("Set filter");
        this.setModal(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.setLayout(new GridBagLayout());
        
        Insets insets = new Insets(2, 2, 2, 2);
        
        this.labelNrDataForAverage = new JLabel("Nr. data for average");
        this.addComponent(
                this, 
                this.labelNrDataForAverage, 
                0, 
                0, 
                1, 
                1, 
                insets, 
                GridBagConstraints.LINE_START, 
                GridBagConstraints.HORIZONTAL);

        // spinner
        this.spinnerNrDataForAverage = new JSpinner();
        this.addComponent(
                this, 
                this.spinnerNrDataForAverage, 
                1, 
                0, 
                1, 
                1, 
                insets, 
                GridBagConstraints.LINE_START, 
                GridBagConstraints.HORIZONTAL);
        
        this.labelIgnoreHighestAndLowest = new JLabel("Ignore highest and lowest");
        this.addComponent(
                this, 
                this.labelIgnoreHighestAndLowest, 
                0, 
                1, 
                1, 
                1, 
                insets, 
                GridBagConstraints.LINE_START, 
                GridBagConstraints.HORIZONTAL);
        
        this.checkboxIgnoreHighestAndLowest = new JCheckBox();
        this.addComponent(
                this, 
                this.checkboxIgnoreHighestAndLowest, 
                1, 
                1, 
                1, 
                1, 
                insets, 
                GridBagConstraints.LINE_START, 
                GridBagConstraints.HORIZONTAL);
        
        // button set
        this.buttonSet = new JButton(ACTION_SET);
        this.buttonSet.addActionListener(this);
        this.buttonSet.setPreferredSize(new Dimension(200, 30));
        this.addComponent(
                this, 
                this.buttonSet, 
                0, 
                2, 
                2, 
                1, 
                insets, 
                GridBagConstraints.LINE_START, 
                GridBagConstraints.HORIZONTAL);
        
        // button cancel
        this.buttonCancel = new JButton(ACTION_CANCEL);
        this.buttonCancel.addActionListener(this);
        this.buttonCancel.setPreferredSize(new Dimension(200, 30));
        this.addComponent(
                this, 
                this.buttonCancel, 
                0, 
                3, 
                2, 
                1, 
                insets, 
                GridBagConstraints.LINE_START, 
                GridBagConstraints.HORIZONTAL);

        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case ACTION_SET:
                this.listener.set(
                        (int)this.spinnerNrDataForAverage.getValue(), 
                        this.checkboxIgnoreHighestAndLowest.isSelected());
                this.dispose();
                break;
                
            case ACTION_CANCEL:
                this.dispose();
                break;
        }
    }
    
    private void addComponent(
            Container container, 
            Component component,
            int gridx, 
            int gridy, 
            int gridwidth, 
            int gridheight, 
            Insets insets,
            int anchor, 
            int fill)
    {
        GridBagConstraints gbc = new GridBagConstraints(gridx, gridy,
                gridwidth, gridheight, 1.0D, 1.0D, anchor, fill, insets, 0, 0);
        container.add(component, gbc);
    }
}
