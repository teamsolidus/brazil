package environmentSensing.gui.dialogues;

import environmentSensing.gui.model.LaserscannerException;
import environmentSensing.gui.model.LaserscannerFascade;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Simon Bühlmann
 */
public class ConnectLaserDialog extends JDialog implements ActionListener
{
    // modell
    private LaserscannerFascade laserscanner;

    // gui elements
    private JTextField textFieldIpAddress;
    private JTextField textFieldPort;

    private JLabel labelIpAddress;
    private JLabel labelPort;

    private JButton buttonConnect;
    private JButton buttonCancel;

    // action commands
    private final static String ACTION_CONNECT = "connect";
    private final static String ACTION_CANCEL = "cancel";

    public ConnectLaserDialog(LaserscannerFascade laserscanner)
    {
        this.laserscanner = laserscanner;

        this.setTitle("Connect with laser");
        this.setModal(true);
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        GridBagLayout gridBagLayout = new GridBagLayout();
        this.setLayout(gridBagLayout);
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 2, 2, 2);

        // label ip address
        this.labelIpAddress = new JLabel("IP:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        gridBagLayout.setConstraints(this.labelIpAddress, constraints);
        this.add(this.labelIpAddress);

        // textfield ip address
        this.textFieldIpAddress = new JTextField("192.168.2.2");
        constraints.gridx = 1;
        constraints.gridy = 0;
        gridBagLayout.setConstraints(this.textFieldIpAddress, constraints);
        this.add(this.textFieldIpAddress);

        // label port
        this.labelPort = new JLabel("Port:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        gridBagLayout.setConstraints(this.labelPort, constraints);
        this.add(this.labelPort);

        // textfield port
        this.textFieldPort = new JTextField("2112");
        constraints.gridx = 1;
        constraints.gridy = 1;
        gridBagLayout.setConstraints(this.textFieldPort, constraints);
        this.add(this.textFieldPort);

        // button connect
        this.buttonConnect = new JButton(ACTION_CONNECT);
        this.buttonConnect.setPreferredSize(new Dimension(200, 30));
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        gridBagLayout.setConstraints(this.buttonConnect, constraints);
        this.buttonConnect.addActionListener(this);
        this.add(this.buttonConnect);
        
        // button cancel
        this.buttonCancel = new JButton(ACTION_CANCEL);
        this.buttonCancel.setPreferredSize(new Dimension(200, 30));
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        gridBagLayout.setConstraints(this.buttonCancel, constraints);
        this.buttonCancel.addActionListener(this);
        this.add(this.buttonCancel);

        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch (e.getActionCommand())
        {
            case ACTION_CONNECT:
                // getting values of the textfields
                String ipAddress = this.textFieldIpAddress.getText();
                int port = Integer.parseInt(this.textFieldPort.getText());
                
                try
                {
                    this.laserscanner.connect(ipAddress, port);
                    this.close();
                }
                catch (LaserscannerException ex)
                {
                    JOptionPane.showMessageDialog(null, "Can not connect", "Connection error", JOptionPane.ERROR_MESSAGE);
                }

                break;
                
            case ACTION_CANCEL:
                this.close();
                break;
        }
    }
    
    private void close()
    {
        this.setVisible(false);
                    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
