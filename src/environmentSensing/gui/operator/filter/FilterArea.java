package environmentSensing.gui.operator.filter;

import environmentSensing.gui.operator.filter.FilterDialog;
import environmentSensing.gui.model.LaserscannerFascade;
import java.awt.Component;
import static java.awt.Component.TOP_ALIGNMENT;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Simon Bühlmann
 */
public class FilterArea extends JPanel implements ActionListener, IFilterDialogListener
{

    // modell
    private LaserscannerFascade laserscanner;

    // gui elements
    private JButton buttonSetFilter;

    private JLabel labelNrDatasAverageTitel;
    private JLabel labelRemoveHighestAndLowestTitel;

    private JLabel labelNrDatasAverageValue;
    private JLabel labelRemoveHighestAndLowestValue;

    private static final Dimension BUTTON_DIM = new Dimension(200, 24);

    // action commands
    private final static String SET_FILTER = "set filter";
    private final static String REMOVE_FILTER = "remove filter";

    public FilterArea(LaserscannerFascade laserscanner)
    {
        this.laserscanner = laserscanner;

        GridBagLayout gridBagLayout = new GridBagLayout();
        this.setLayout(gridBagLayout);
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;
        Insets insets = new Insets(2, 2, 2, 2);
        constraints.insets = new Insets(2, 2, 2, 2);

        // button connect
        this.buttonSetFilter = new JButton(SET_FILTER);
        this.buttonSetFilter.setPreferredSize(BUTTON_DIM);
        this.buttonSetFilter.setMinimumSize(BUTTON_DIM);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        gridBagLayout.setConstraints(this.buttonSetFilter, constraints);
        this.buttonSetFilter.addActionListener(this);
        this.add(this.buttonSetFilter);

        this.labelNrDatasAverageTitel = new JLabel("nr. datas for average:");
        this.addComponent(
                this, 
                this.labelNrDatasAverageTitel, 
                0, 
                1, 
                1, 
                1, 
                insets, 
                GridBagConstraints.LINE_START, 
                GridBagConstraints.HORIZONTAL);

        this.labelNrDatasAverageValue = new JLabel("null");
        this.addComponent(
                this, 
                this.labelNrDatasAverageValue, 
                1, 
                1, 
                1, 
                1, 
                insets, 
                GridBagConstraints.LINE_START, 
                GridBagConstraints.HORIZONTAL);

        this.labelRemoveHighestAndLowestTitel = new JLabel("remove highest and lowest:");
        this.addComponent(
                this, 
                this.labelRemoveHighestAndLowestTitel, 
                0, 
                2, 
                1, 
                1, 
                insets, 
                GridBagConstraints.LINE_START, 
                GridBagConstraints.HORIZONTAL);

        this.labelRemoveHighestAndLowestValue = new JLabel("null");
        this.addComponent(
                this, 
                this.labelRemoveHighestAndLowestValue, 
                1, 
                2, 
                1, 
                1, 
                insets, 
                GridBagConstraints.LINE_START, 
                GridBagConstraints.HORIZONTAL);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case SET_FILTER:
                new FilterDialog(this);
                break;
                
            case REMOVE_FILTER:
                this.laserscanner.removeAverageFilter();
                this.labelNrDatasAverageValue.setText("null");
                this.labelRemoveHighestAndLowestValue.setText("null");
                this.buttonSetFilter.setText(SET_FILTER);
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

    @Override
    public void set(int nrDataForAverage, boolean ignoreHighestAndLowest)
    {
        this.labelNrDatasAverageValue.setText(nrDataForAverage+"");
        this.labelRemoveHighestAndLowestValue.setText(ignoreHighestAndLowest+"");
        this.buttonSetFilter.setText(REMOVE_FILTER);
        
        this.laserscanner.setAverageFilter(nrDataForAverage, ignoreHighestAndLowest);
    }
}
