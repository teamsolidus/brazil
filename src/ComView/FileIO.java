/*
 * Projekt:         Robotino Team Solidus
 * Autor:           Steck Manuel
 * Datum:           29.05.2013
 * Geändert:        
 * Änderungsdatum:  
 * Version:         V_1.1.0_Explo
 */
package ComView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author stecm1
 */
public class FileIO
{

    

    public FileIO()
    {
        
        
    }
    
    //**********************************************************************************************************
    //******                                        READ                                                   *****
    //**********************************************************************************************************
    
    public String getText(File file) throws FileNotFoundException, IOException, InterruptedException
    {
        

        FileReader leser = new FileReader(file);
        int length =(int) file.length();
        char[] temp = new char[length];
        
        while (file.exists() == false);
        {
            //Thread.sleep(50);
        }
        
        leser.read(temp);
        leser.close();
        String out = String.valueOf(temp);

        return out;
        
    }
    
     public String getLastChar(File file) throws FileNotFoundException, IOException, InterruptedException
    {
        
        
        FileReader leser = new FileReader(file);
        int length =(int) file.length();
        char[] temp = new char[length];
        
        while (file.exists() == false);
        {
            //Thread.sleep(50);
        }
        
        leser.read(temp);
        leser.close();
        char temp2 = temp[length-1];
        String out = String.valueOf(temp2);

        return out;
        
    }
     
     /**
      * 
      * @param file
      * @return
      * @throws FileNotFoundException
      * @throws IOException 
      * Gibt den zweit letzten char[] platz zurück.
      * Da das Programm RoboView beim schreiben in ein Textdokument immer noch
      * einen Zeilenumbruch anfügt, muss der zweitletzte char[] gelesen werden
      * um das letzte Zeichen zu bekommen.
      */
     
    public String getSecendLastChar(File file) throws FileNotFoundException, IOException, InterruptedException
    {
        
        
        FileReader leser = new FileReader(file);
        int length =(int) file.length();
        char[] temp = new char[length];
        
        while (file.exists() == false);
        {
            //Thread.sleep(50);
        }
        leser.read(temp);
        leser.close();
        char temp2 = temp[length-3];
        String out = String.valueOf(temp2);

        return out;
        
    }
    
   
    
    
    
    
    //**********************************************************************************************************
    //******                                        WRITH                                                  *****
    //**********************************************************************************************************

    
    
    // Dokument schreiben mit String 
    
     public void setText(String temp, File file) throws FileNotFoundException, IOException, InterruptedException
    {

        FileWriter schreiber = new FileWriter(file);
        
        while (file.exists() == false);
        {
            //Thread.sleep(50);
        }
        schreiber.write(temp);
        schreiber.flush();
        schreiber.close();
    }
     
     
     
     
    public void setOnlyNumber(String temp, File file) throws FileNotFoundException, IOException, InterruptedException
    {
            String chnge1 = ",";
            String chnge2 = "[";
            String chnge3 = "]";
        
// --------------------------   entfernt ","   ---------------------------------
            
            char[] stringArray1 = temp.toCharArray();
            String temp1 = "";
        
            for(int i = 0; i < stringArray1.length; i++)
            {
            
                if(String.valueOf(stringArray1[i]).equals(chnge1) == false)
                {
                    temp1 += String.valueOf(stringArray1[i]).toString();
                }
            }
            
// --------------------------   entfernt "["   ---------------------------------
            
            char[] stringArray2 = temp1.toCharArray();
            String temp2 = "";
        
            for(int i = 0; i < stringArray2.length; i++)
            {
            
                if(String.valueOf(stringArray2[i]).equals(chnge2) == false)
                {
                    temp2 += String.valueOf(stringArray2[i]).toString();
                }
            }
            
// --------------------------   entfernt "]"   ---------------------------------
            
            char[] stringArray3 = temp2.toCharArray();
            String out = "";
        
            for(int i = 0; i < stringArray3.length; i++)
            {
            
                if(String.valueOf(stringArray3[i]).equals(chnge3) == false)
                {
                    out += String.valueOf(stringArray3[i]).toString();
                }
            }
            
            setText(out, file);
        
    }
        
    
}
