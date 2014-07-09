/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package environmentSensing.newGUI.view;

import javax.swing.JFrame;

/**
 *
 * @author simon.buehlmann
 */
public class EnvironmentSensingTestWindow extends JFrame
{
    ReferencePointsLayer refPointsLayer;
    MeasurementPointsLayer measPointsLayer;
    WallLayer lineLayer;
    
    public EnvironmentSensingTestWindow()
    {
        this.refPointsLayer = new ReferencePointsLayer();
        this.measPointsLayer = new MeasurementPointsLayer();
        this.lineLayer = new WallLayer();
        
        this.add(this.refPointsLayer);
        this.add(this.measPointsLayer);
        this.add(this.lineLayer);
        this.setLayout(null);
        this.setVisible(true);
    }
    
    public static void main(String[] args)
    {
        EnvironmentSensingTestWindow estw = new EnvironmentSensingTestWindow();
    }
}
