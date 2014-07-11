/*
 * Projekt:         Robotino Team Solidus
 * Autor:           Steck Manuel
 * Datum:           08.06.2013
 * Geändert:        
 * Änderungsdatum:  
 * Version:         V_1.1.0_Explo
 */
package GUI;

import MainPack.Main;
import Tools.PingRefbox;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;

/**
 *
 * @author stecm1
 */
public class RefboxPanel extends Panel implements MouseListener
{
    Label ipLabel = new Label("Refbox IP:");
    Label keyLabel = new Label("Encryption Key:");
    Label nameLabel = new Label("Robotino Name:");

    TextField ip = new TextField();
    TextField encKey = new TextField();
    public TextField roboname = new TextField();

    Button ping = new Button();
    Button test=new Button();
    Button ok = new Button();       

    PingRefbox pingRb = new PingRefbox();

    public RefboxPanel()
    {
        this.setBackground(new Color(100,100,240));
        setLayout(new GridLayout(3, 3, 5, 5));

        ip.setText(Main.refBoxIp);
        ip.setLocation(200, 100);
        ip.setSize(180, 40);
        ip.setBackground(Color.GREEN);

        encKey.setText(Main.encKey + "");
        encKey.setLocation(200, 150);
        encKey.setSize(180, 30);
        encKey.setBackground(Color.GREEN);

        roboname.setText(Main.name);
        roboname.setLocation(200, 200);
        roboname.setSize(180, 30);
        roboname.setBackground(Color.GREEN);

        keyLabel.setSize(100, 40);
        keyLabel.setLocation(100, 150);

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
        ping.addMouseListener(this);
        
        add(ipLabel);
        add(ip);                
        add(ping);
        add(nameLabel);
        add(roboname);   
        add(test);
        add(keyLabel);        
        add(encKey);        
        add(ok);                
        setSize(200,100);
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
                        Main.setEncryptionKey(encKey.getText());

                        System.out.println("Refbox-IP wurde auf " + Main.refBoxIp + ":" + Main.encKey + " geändert!");
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
