package environmentSensing.Laser;

/**
 *
 * @author simon.buehlmann
 */
public class BasicInterpreter implements NewMeasurementListener
{
    //Variablen

    //Konstanten Steuerzeichen Protokoll
    private final byte STX = 2;//ASCII Zeichen
    private final byte ETX = 3;//ASCII Zeichen;
    private final byte SPACE = 32;//ASCII Zeichen

    //Objekte
    private Communication Tim;
    private IDataTaker Data;

    //Konstruktor
    public BasicInterpreter()
    {

    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter_And_Setter">
    public Data getMeasurementData()
    {
        return (Data)Data;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private_Methods">
    /**
     * Analisiert das angekommene Byte auf Steuerzeichen (STX, ETX, SPACE) und
     * erzeugt/beendet entsprechend Messdaten und Segmente rsp. leitet die Daten
     * weiter.
     *
     * @param value zu analysierendes Byte
     */
    private void assignToState(byte value)
    {
        switch (value)
        {
            case STX://Erzeugen von neuem Messdaten-Objekt
                Data = new Data(2000, 1000);
                break;
            case ETX://Abschliessen von Messdatenobjekt
                Data.release();
                break;
            case SPACE://Erzeugen von neuem Messdaten-Abschnitt
                Data.newSegment();
                break;
            default://Hinzufügen zu bestehendem Messdaten-Abschnitt in bestehendem -Objekt.
                Data.addByte(value);
                break;
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="NewMeasurementListener">
    @Override
    public void newMeasurementValues(byte value)
    {
        //System.out.println("BasicInterpreter Value: " + value);
        this.assignToState(value);
    }
//</editor-fold>

}
