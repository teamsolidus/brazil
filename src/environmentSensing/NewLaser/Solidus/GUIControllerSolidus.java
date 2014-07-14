
package Laser.Solidus;

import Laser.GUI.Controller;
import Laser.GUI.View;
import java.awt.Color;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GUIControllerSolidus implements Observer
{
    private View view;
    private Laser tim;
    
    public GUIControllerSolidus(Laser tim)
    {
        this.tim = tim;
    }

    @Override
    public void update(Observable o, Object arg)
    {
        try
        {
            view.setLaserMap(this.tim.getCoordinatesData().getDistance(90, 180));
            
            if(this.tim.getMeasurementData().isMonitorAreaFree())
            {
                view.setMonitorAreaColor(Color.RED);
            }
            else
            {
                view.setMonitorAreaColor(Color.GREEN);
            }
            
            view.setValueClosestCoord(this.tim.getMeasurementData().getMinDistance());
            
        }
        catch (Exception ex)
        {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
