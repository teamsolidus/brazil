/*
 ===TO-DO===
 - Skalierungsfaktor berücksichtigen
 - Anpassen überschreibbare Methoden in Konstruktor
 *//*
 ===TO-DO===
 - Skalierungsfaktor berücksichtigen
 - Anpassen überschreibbare Methoden in Konstruktor
 */
package environmentSensing.Laser;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * API für das Empfangen von Messresultaten des TiM55x von Sick. Ausgelegt für
 * das Team Solidus der HFT Mittelland.
 *
 * Momentane Nutzungsbedingungen: - Der TiM muss über das SICK SOPAS
 * konfiguriert werden - Der Messwinkel darf nicht eingeschränkt werden (-45
 * Grad bis 225 Grad) - RSSI muss aktiviert werden
 *
 * @version 0.0
 * @author simon.buehlmann
 */
public class Laser
{

    private static Laser instance;
    private long methodRuntime;
int [] save;
    
    public static Laser getInstance()
    {
        if (instance == null)
        {
            try
            {
                instance = new Laser();
            }
            catch (IOException ex)
            {
                Logger.getLogger(Laser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return instance;
    }

    //<editor-fold defaultstate="collapsed" desc="VARIABLEN_OBJEKTE_KONSTANTEN">
    //Objekte
    private TiM55x tim;//Lasermessgerät
    private DataMask1 data;//aktuelles Datenset

//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="KONSTRUKTOREN">
    /**
     * Standart-Konstruktor. Baut eine Verbindung auf die IP-Adresse 192.168.0.1
     * über den Port 2112 auf (Werkseinstellungen TiM).
     *
     * @throws IOException Fehler, wenn unter der IP & Port kein TiM
     * angesprochen werden konnte.
     */
    private Laser() throws IOException
    {
        tim = new TiM55x("192.168.2.2", 2112);
        this.getNewMeasurementData();
    }

    /**
     * Parametrierbarer Konstruktor. Versucht eine Verbindung auf die
     * entsprechende IP-Adresse über den entsprechenden Port aufzubauen.
     *
     * @param ipAdress Adresse des TiM55x
     * @param port Ausgabeport des TiM55x
     * @throws IOException IOException Fehler, wenn unter der IP & Port kein TiM
     * angesprochen werden konnte.
     */
    public Laser(String ipAdress, int port) throws IOException
    {
        tim = new TiM55x(ipAdress, port);
        this.getNewMeasurementData();//Fehlerquelle bei Verebung --> Anpassen!
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PUBLIC_METHODEN">
    /**
     * Es wird eine neue Messung getätigt und die Messdaten im internen Buffer
     * gespeichert.
     *
     * @throws IOException Probleme mit der Verbindung zum TiM
     */
    public void getNewMeasurementData() throws IOException
    {
        methodRuntime = System.nanoTime();
        data = tim.singleMeassurement();
        methodRuntime = System.nanoTime() - methodRuntime;
    }

    /**
     * Prüfen, ob in einer bestimmten Richtung keine Reflektion unterhalb einer
     * gegebenen Grenzen auftritt. Arbeitet mit den im internen Buffer
     * gespeicherten Messdaten.
     *
     * @param angle Richtungh in Grad. (Erlaubter Range: -135 bis +135)
     * @param distance Grenze in mm
     * @return wenn true = keine Reflektion, wenn false = Reflektion vorhanden
     * @deprecated 
     */
    public boolean directionFree(int angle, int distance)
    {
        return false;
    }

    /**
     * Abfragen eines Distanzwertes im gegebenen Winkel. Liest die Daten aus dem
     * internen Buffer aus.
     *
     * @param angle Richtungh in Grad. (Erlaubter Range: -135 bis +135)
     * @return Distanzwert in mm. Falls keine Reflektion, wird 10000mm
     * ausgegeben . Falls = -1 ist der übergenen Winkel nicht erlaubt.
     * @deprecated
     */
    public int getDistance(int angle)
    {
        return -1;
    }

    public int getMinDistance(int angle, int symetricRange)
    {
            try
            {
                int indexMiddle = this.angleToIndexConverter(angle);
                
                int[] tempData = data.getDistance(indexMiddle - symetricRange, indexMiddle + symetricRange);
                int tempReturn = 10000;

                this.save = tempData;
                
                for (int countFor = 0; countFor < tempData.length; countFor++)
                {
                    if(tempData[countFor] > 0)//0 = no reflection
                    {
                        if (tempData[countFor] < tempReturn)
                        {
                            tempReturn = tempData[countFor];
                        }
                    }                    
                }
                if(tempReturn > 50)
                {
                    return tempReturn;
                }
                else
                {
                    return 10000;
                }
            }
            catch (Exception ex)
            {
                return -1;
            }
        }

    /**
     * Abfragen eines Reflektionwertes im gegebenen Winkel. Liest die Daten aus
     * dem internen Buffer aus.
     *
     * @param angle Richtungh in Grad. (Erlaubter Range: -135 bis +135)
     * @return Wert im Range 0 - 255. Falls = -1 ist der übergenen Winkel nicht
     * erlaubt.
     * @deprecated
     */
    public int getReflection(int angle)
    {
        return -1;
    }

    public long getLastMethodRuntime()
    {
        return methodRuntime;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PRIVATE_METHODEN">
    private int angleToIndexConverter(int angle)
    {
        return angle + 90 + 45;
    }
//</editor-fold>
}
