package Tools;

import GUI.RefboxPanel;
import Sequence.JobController;
import Sequence.StateMachine;
import Traveling.Drive;
import java.awt.AWTException;
import java.awt.Button;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Schreiben von Objekten
 *
 * @author roa
 * @version 1.0
 */
public class ObjFile
{

    public static boolean fileExists(String filename)
    {
        File f = new File(filename);
        return f.exists();
    }

    public void maintenanceWrite(Object obj, String filename)
    {
        try
        {
            FileOutputStream datAus = new FileOutputStream(filename);       // Datenstrom zum Schreiben in die Datei erzeugen      
            BufferedOutputStream oAus = new BufferedOutputStream(datAus);       // Objektstrom darueber legen
            XMLEncoder oXML = new XMLEncoder(oAus);                           // Umwandlung in XML 
            oXML.writeObject(obj);                                          // Datensaetze in die Datei schreiben            
            oXML.close();                                                   // Dateistrom schliessen      
        } catch (FileNotFoundException e)
        {
            System.out.println("Fehler beim Schreiben: " + e);
        }
    }

    public Object maintenanceRead(String filename)
    {
        Object obj = null;
        try
        {
            FileInputStream datEin = new FileInputStream(filename);         // Datenstrom zum Lesen aus der Datei erzeugen      
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

    public static void main(String[] args) throws AWTException, Exception
    {

        ObjFile of = new ObjFile();
        JobController job = JobController.getInstance();


      
        String filename = "MeineDaten.dat";
        of.maintenanceWrite(job, filename);

        JobController sm = (JobController) of.maintenanceRead(filename);

        System.out.println(sm.jobCounter);
    }
}
