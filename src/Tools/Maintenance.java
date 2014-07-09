package Tools;

import Sequence.JobController;
import java.awt.AWTException;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author alwin.mani
 */
public class Maintenance                               //Für den widereinstig ins Spiel (Neustrat im Spiel)
{
    public  boolean fileExists(String restart)   //Methode ob File existiert
    {
        File f = new File(restart);
        return f.exists();
    }

    public void maintenanceWrite(Object obj, String restart)
    {
        try
        {
            FileOutputStream datAus = new FileOutputStream(restart);       // Datenstrom zum Schreiben in die Datei erzeugen      
            BufferedOutputStream oAus = new BufferedOutputStream(datAus);       // Objektstrom darueber legen
            XMLEncoder oXML = new XMLEncoder(oAus);                           // Umwandlung in XML 
            oXML.writeObject(obj);                                          // Datensaetze in die Datei schreiben            
            oXML.close();                                                   // Dateistrom schliessen      
        } catch (FileNotFoundException e)
        {
            System.out.println("Fehler beim Schreiben: " + e);
        }
    }

    public Object maintenanceRead(String restart)
    {
        Object obj = null;
        try
        {
            FileInputStream datEin = new FileInputStream(restart);         // Datenstrom zum Lesen aus der Datei erzeugen      
            BufferedInputStream oEin;                                         // Objektstrom darueberlegen
            oEin = new BufferedInputStream(datEin);
            XMLDecoder xmlInput = new XMLDecoder(oEin);                       // Umwandlung von XML
            obj = xmlInput.readObject();                                        // Datensatz aus der Datei lesen und deren Datensatzfelder
            System.out.println(obj);                                        // zur Kontrolle auf den Bildschirm ausgeben

            oEin.close();                                                   // Dateistrom schliessen
        } catch (IOException e)
        {
            System.out.println("Fehler beim Lesen: " + e);
        }
        return obj;
    }

    public static void main(String[] args) throws AWTException
    {
        Maintenance of = new Maintenance();
        JobController job = JobController.getInstance();

        String filename = "MeineDaten.dat";
        of.maintenanceWrite(job, filename);

        JobController sm = (JobController) of.maintenanceRead(filename);

        System.out.println(sm.jobCounter);
    }
}
