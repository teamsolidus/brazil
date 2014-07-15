package environmentSensing.NewLaser;

import environmentSensing.NewLaser.Data.IDataProvider;
import environmentSensing.NewLaser.TiM.TiM55x;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author simon.buehlmann
 */
public class Tester_Main implements Observer
{

    @Override
    public void update(Observable o, Object arg)
    {
    }

    public static void main(String[] args) throws Exception
    {
        TiM55x tim = new TiM55x();
        Tester_Main main = new Tester_Main();

        tim.addObserver(main);

        IDataProvider<Integer> data = tim.synchRunSingleMeas();

        System.out.println(data.getDistance(135));
        System.out.println(data.getStartAngle());
        
        Integer[] temp = data.getDistance();
        
        for(int countFor = 0; countFor < temp.length; countFor++)
        {
            System.out.println("DISTANCE NR. " + countFor + " VALUE: " + temp[countFor]);
        }
        

//        tim.startContinuousMeas();
//
//        while (true)
//        {
//            System.out.println("DISTANCE: " + tim.getData().getDistance(135));
//            System.out.println("START-ANGLE: " + data.getStartAngle());
//            Thread.sleep(100);
//        }

//        Laser2 tim = new Laser2();
//        
//        tim.runSingleMeasurement();
//        
//        Thread.sleep(500);
//        
//        System.out.println(tim.getNewMeasurementData().getDistance(0));
    }


    /*static Laser tim;
    
     public static void main(String[] args) throws IOException, Exception
     {
     TiM55x tim = new TiM55x("169.254.87.60", 2112);
    
     tim.runSingleMeasurement();
    
     System.out.println(tim.getMeasurementData().getDistance(0));
    
    
     while(true)
     {
     System.out.println("Distance: " + tim.runSingleMeasurementBlocking().getDistance(0));
     Thread.sleep(500);
     }*/
//        tim = Laser.getInstance();
//
//        DataSolidus data;
//        try
//        {
//            data = tim.runSingleMeasurementBlocking();
//            System.out.println("Method Runtime: " + tim.getLastMethodRuntime() + " nanoseconds");
//            System.out.println("Distance: " + data.getDistance(0));
//            System.out.println("Rssi: " + data.getReflection(0));
//            System.out.println("Direction Free 5m: " + data.directionFree(0, 5000));
//            System.out.println("Direction Free 30cm: " + data.directionFree(0, 300));
//            System.out.println("Angle Steps: " + data.getDataObject().getSteps());
//            System.out.println("Start Angle: " + data.getDataObject().getStartAngle());
//            System.out.println("Nr Distance Data: " + data.getDataObject().getNrDistance());
//            
//        }
//        catch (Exception ex)
//        {
//            Logger.getLogger(Tester_Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
}
