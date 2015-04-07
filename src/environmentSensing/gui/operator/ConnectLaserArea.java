package environmentSensing.gui.operator;

import environmentSensing.gui.dialogues.ConnectLaserDialog;
import environmentSensing.gui.model.LaserscannerFascade;
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
public class ConnectLaserArea extends JPanel implements ActionListener
{
    // modell
    private LaserscannerFascade laserscanner;
    
    // gui elements
    private JButton buttonConnect;
    
    private static final Dimension BUTTON_DIM = new Dimension(200, 24);
    
    // action commands
    private final static String CONNECT_LASER = "connect laser";
    private final static String DISCONNECT_LASER = "disconnect laser";

    public ConnectLaserArea(LaserscannerFascade laserscanner)
    {
        this.laserscanner = laserscanner;
        
        GridBagLayout gridBagLayout = new GridBagLayout();
        this.setLayout(gridBagLayout);
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 2, 2, 2);
        
        // button connect
        this.buttonConnect = new JButton(CONNECT_LASER);
        this.buttonConnect.setPreferredSize(BUTTON_DIM);
        this.buttonConnect.setMinimumSize(BUTTON_DIM);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        gridBagLayout.setConstraints(this.buttonConnect, constraints);
        this.buttonConnect.addActionListener(this);
        this.add(this.buttonConnect);
        
        this.setAlignmentY(TOP_ALIGNMENT);
    }

    public void setConnectionState(boolean connected)
    {
        if(connected)
        {
            this.buttonConnect.setText(DISCONNECT_LASER);
        }
        else
        {
            this.buttonConnect.setText(CONNECT_LASER);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case CONNECT_LASER:
                new ConnectLaserDialog(this.laserscanner);
                break;
                
            case DISCONNECT_LASER:
                this.laserscanner.disconnect();
                break;
        }
    }
}
