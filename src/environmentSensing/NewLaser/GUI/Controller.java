package environmentSensing.NewLaser.GUI;

import environmentSensing.NewLaser.Solidus.ILaserOperatorSolidus;
import environmentSensing.NewLaser.Solidus.Laser;
import java.awt.Color;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author simon.buehlmann
 */
public class Controller implements Observer
{

    private ILaserOperatorSolidus solidusTim;
    private Laser tim;
    private View view;

    public Controller() throws Exception
    {
        this.solidusTim = new Laser();
        this.solidusTim.addObserver(this);
        this.view = new View();

        //Not Solidus API
        this.tim = (Laser) this.solidusTim;
        this.solidusTim.startContinuousMeasurement();
    }

    @Override
    public void update(Observable o, Object arg)
    {
        try
        {
            view.setLaserMap(this.tim.getCoordinatesData().getDistance(90, 180));
            
            if(this.solidusTim.getMeasurementData().isMonitorAreaFree())
            {
                view.setMonitorAreaColor(Color.RED);
            }
            else
            {
                view.setMonitorAreaColor(Color.GREEN);
            }
            
            view.setValueClosestCoord(this.solidusTim.getMeasurementData().getMinDistance());
            
        }
        catch (Exception ex)
        {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        

//        int startIdx = 90;
//        int endIdx = 180;
//        
//        System.out.println("Observer call");
//        try
//        {
//            view.setLaserMap(this.tim.getCoordinatesData().getDistance(startIdx, endIdx));
//            if(collAera.checkArea(this.tim.getCoordinatesData().getDistance(startIdx, endIdx)))
//            {
//                view.setMonitorAreaColor(Color.RED);
//            }
//            else
//            {
//                view.setMonitorAreaColor(Color.GREEN);
//            }
//            view.setValueClosestCoord((int) collAera.getClosestCoordToY(this.tim.getCoordinatesData().getDistance(startIdx, endIdx)).getY());
//        }
//        catch (Exception ex)
//        {
//            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
