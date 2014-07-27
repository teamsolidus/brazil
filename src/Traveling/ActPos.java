/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Traveling;

import ComView.ComView;
import FieldCommander.Cell;
import java.awt.AWTException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrian
 */
public class ActPos extends Thread
{

    Drive drive;
    boolean running = true;
    ComView cv;

    public ActPos() throws AWTException
    {
        drive = Drive.getInstance();
        cv = ComView.getInstance();

    }

    @Override
    public void run()
    {
        while (running)
        {
          //  Cell actCell = drive.calcPos();
            drive.calcPos(cv.getxAktuell());
          
            try
            {
                Thread.sleep(150);
                
                // System.out.println("Zelle X: " + actCell.getX() + "Zelle Y: " + actCell.getY());
                
                //System.out.println("Absolut Pos X: " + actCell.getRealX() + "Absolute Pos Y: " + actCell.getRealY());
            } catch (InterruptedException ex)
            {
                Logger.getLogger(ActPos.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }


}
