/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package environmentSensing.Laser;

import java.io.IOException;


/**
 *
 * @author simon.buehlmann
 */
public class MainTester
{
     static Laser tim;
     
    public static void main(String[] args) throws Exception
    {

            tim = Laser.getInstance();
            //System.out.println("Method Runtime: " + tim.getLastMethodRuntime() + " nanoseconds");
            //System.out.println("Messwert: " + tim.getDistance(0));
            //System.out.println("Reflektion: " + tim.getReflection(0));
            //System.out.println("Grenze 5m: " + tim.directionFree(0, 5000));
            //System.out.println("Grenze 30cm: " + tim.directionFree(0, 300));
            
            while(true)
            {
                tim.getNewMeasurementData();
                System.out.println("Range Measurement min. Value: " + tim.getMinDistance(0, 13)+ " mm");
                Thread.sleep(500);
            }
            
            /*try
            {
            TiM55x Tim = new TiM55x("192.168.0.1", 2112);
            DataMask data = Tim.singleMeassurement();
            System.out.println("Anzahl Daten: " + data.getAnzahlDistanceDaten());
            
            int[] distance = data.getDistance();
            
            for(int countFor = 0; countFor < distance.length; countFor++)
            {
            System.out.println(countFor + ". Distanz: " + distance[countFor]);
            }
            
            System.out.println("Startwinkel: " + data.getStartwinkel());
            
            int[] rssi = data.getRssiData();
            
            for(int countFor = 0; countFor < rssi.length; countFor++)
            {
            System.out.println(countFor + ". RSSI: " + rssi[countFor]);
            }
            
            } catch (IOException ex)
            {
            Logger.getLogger(MainTester.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex)
            {
            Logger.getLogger(MainTester.class.getName()).log(Level.SEVERE, null, ex);
            }*/
       
    }
}
