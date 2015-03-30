package environmentSensing.gui.operator;

import environmentSensing.gui.model.LaserscannerFascade;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Simon Bühlmann
 */
public class OperatorArea extends JPanel implements ActionListener
{
    // modell
    private LaserscannerFascade laserscanner;

    // gui elements
    private JButton buttonRunSingle;
    private JButton buttonStartCont;
    private JButton buttonStopCont;
    
    private static final Dimension BUTTON_DIM = new Dimension(200, 24);

    // action commands
    private final static String RUN_SINGLE = "run single";
    private final static String START_CONT = "start cont";
    private final static String STOP_CONT = "stop cont";

    public OperatorArea(LaserscannerFascade laserscanner)
    {
        this.laserscanner = laserscanner;

        GridBagLayout gridBagLayout = new GridBagLayout();
        this.setLayout(gridBagLayout);
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 2, 2, 2);
        
        this.buttonRunSingle = new JButton(RUN_SINGLE);
        this.buttonRunSingle.setPreferredSize(BUTTON_DIM);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        gridBagLayout.setConstraints(this.buttonRunSingle, constraints);
        this.add(this.buttonRunSingle);
        
        this.buttonStartCont = new JButton(START_CONT);
        this.buttonRunSingle.setPreferredSize(BUTTON_DIM);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        gridBagLayout.setConstraints(this.buttonStartCont, constraints);
        this.add(this.buttonStartCont);
        
        this.buttonStopCont = new JButton(STOP_CONT);
        this.buttonRunSingle.setPreferredSize(BUTTON_DIM);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        gridBagLayout.setConstraints(this.buttonStopCont, constraints);
        this.add(this.buttonStopCont);
        
        this.setAlignmentY(TOP_ALIGNMENT);
        
        // add listener
        this.buttonRunSingle.addActionListener(this);
        this.buttonStartCont.addActionListener(this);
        this.buttonStopCont.addActionListener(this);
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        this.buttonRunSingle.setEnabled(enabled);
        this.buttonStartCont.setEnabled(enabled);
        this.buttonStopCont.setEnabled(enabled);
        
        super.setEnabled(enabled);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        try
        {
            System.out.println(e.getActionCommand());
            switch (e.getActionCommand())
            {
                case RUN_SINGLE:
                    this.laserscanner.runSingle();
                    break;

                case START_CONT:
                    this.laserscanner.startCont();
                    break;

                case STOP_CONT:
                    this.laserscanner.stopCont();
                    break;
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(OperatorArea.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
