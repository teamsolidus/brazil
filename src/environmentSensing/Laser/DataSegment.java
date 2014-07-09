package environmentSensing.Laser;

/**
 * Ein Datensegment kann aus unterschiedlich vielen Chars bestehen. Das ganze
 * dient dazu, die unterschiedlichen Char-Arrays in einem weiteren Array (rsp. einer List)
 * zusammen zu fassen.
 *
 * @author simon.buehlmann
 */
public class DataSegment
{
    //Variablen
    private int pointer;//Zeigt auf zu-befüllendes Feld
    private boolean release;//Zeigt, ob das DataSegment bereits abgeschlossen wurde und somit nicht mehr beschreibbar ist.
    private char[] segment;//Array, das mit den Daten bedfüllt wird
    
    //Konstruktor
    public DataSegment(int maxSize)
    {
        segment = new char[maxSize];
        pointer = 0;
        release = false;
    }
    
    //Methoden
    public void addByte(byte dataChar)
    {
        segment[pointer] = (char)dataChar;//Wird direkt in Char gecastet
        pointer++;
    }
    
    public void releaseSegment()
    {
        //Anpassen der Länge des Char Arrays auf minimalgrösse
        char[] tempSegment = new char[pointer];
        for(int countFor = 0; countFor <tempSegment.length; countFor++)
        {
            tempSegment[countFor] = segment[countFor];
        }
        segment = tempSegment;//neue Referenz für segment
        tempSegment = null;//Refernez von tempSegment löschen
        release = true;
    }
    
    //Getter und Setter
    public char[] getSegment()
    {
            return segment;
    }
}
