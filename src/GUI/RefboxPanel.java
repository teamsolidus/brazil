/*
 * Projekt:         Robotino Team Solidus
 * Autor:           Steck Manuel
 * Datum:           08.06.2013
 * Geändert:        
 * Änderungsdatum:  
 * Version:         V_1.1.0_Explo
 */
package GUI;

import FieldCommander.FieldCommander;
import MainPack.Main;
import Tools.PingRefbox;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author stecm1
 */
public class RefboxPanel extends Panel implements MouseListener
{

    Label ipLabel = new Label("Refbox IP:");
    Label portLabel = new Label("Refbox Port:");
    Label nameLabel = new Label("Robotino Name:");

    TextField ip = new TextField();
    TextField port = new TextField();
    public TextField roboname = new TextField();

    Button ping = new Button();
    Button ok = new Button();

    PingRefbox pingRb = new PingRefbox();

    public RefboxPanel()
    {

        setLayout(null);

        ip.setText(Main.refBoxIp);
        ip.setLocation(200, 100);
        ip.setSize(180, 40);
        ip.setBackground(Color.GREEN);

        port.setText(Main.refBoxPortIn + "");
        port.setLocation(200, 150);
        port.setSize(180, 40);
        port.setBackground(Color.GREEN);

        roboname.setText(Main.name);
        roboname.setLocation(200, 200);
        roboname.setSize(180, 40);
        roboname.setBackground(Color.GREEN);

        portLabel.setSize(100, 45);
        portLabel.setLocation(100, 150);

        nameLabel.setSize(100, 45);
        nameLabel.setLocation(100, 200);

        ipLabel.setSize(100, 45);
        ipLabel.setLocation(100, 100);

        ok.setLabel("OK");
        ok.setSize(100, 50);
        ok.setLocation(400, 150);
        ok.addMouseListener(this);

        ping.setBackground(Color.PINK);
        ping.setLabel("PING REFBOX");
        ping.setSize(100, 50);
        ping.setLocation(400, 90);
        this.add(ping);
        ping.addMouseListener(this);

        add(roboname);
        add(nameLabel);
        add(port);
        add(portLabel);
        add(ipLabel);
        add(ip);
        add(ok);

    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

        if (e.getSource() == ping)
        {
            try
            {
                pingRb.pingRefbox();
            } catch (IOException ex)
            {
                Logger.getLogger(RefboxPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (e.getButton() == MouseEvent.BUTTON1)
        {

            if (e.getClickCount() == 1)
            {

                // Zulaufventil
                if (e.getSource() == ok)
                {
                    try
                    {
                        Main.setIpRefbox(ip.getText());
                        Main.setNameRobo(roboname.getText());

                        System.out.println("Refbox-IP wurde auf " + Main.refBoxIp + ":" + Main.refBoxPortIn + " geändert!");
                    } catch (IOException ex)
                    {
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

}
