package environmentSensing.Laser;

import java.io.IOException;

/**
 * Fasade für das System zum ansprechen des TiM55x
 * @author simon.buehlmann
 */
public class TiM55x
{
    //Objekte
    private Communication TiM_Com;
    private BasicInterpreter TiM_BasicInt;
    
    //Konstruktor
    public TiM55x (String ipAdress, int port) throws IOException
    {
        TiM_Com = new Communication(ipAdress, port);
        TiM_BasicInt = new BasicInterpreter();
    }
    
    //Messmethoden
    public DataMask1 singleMeassurement() throws IOException
    {
        TiM_Com.singleMeasurement(TiM_BasicInt);
        return new DataMask1(TiM_BasicInt.getMeasurementData());
    }
}
