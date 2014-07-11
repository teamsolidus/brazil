/*
 * Projekt:         Robotino Team Solidus
 * Autor:           Steck Manuel/Alain Rohr
 * Datum:           08.06.2013
 * Geändert:        09.07.2014
 * Änderungsdatum:  
 * Version:         V_1.3.0
 */
package MainPack;

import ComView.*;
import FieldCommander.FieldCommander;
import Refbox.ComRefBox;
import Sequence.StateMachine;
import Sequence.JobController;
import java.awt.AWTException;
import java.io.*;
import java.net.SocketException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.SimpleLayout;

/**
 * Start Hauptprogramm (Entry-Point)
 */
public class Main
{

  public static org.apache.log4j.Logger log = org.apache.log4j.Logger.getRootLogger();

  public static String refBoxIp = "172.26.255.255";
  public static String name = "Solid1";
  public static String encKey = "randomkey";

  static ComRefBox comRefBox;
  static ComView comView;
  static JobController jc;
  static StateMachine sm;
  static FieldCommander fc;
  static File ipfile;
  static File encfile;
  static File namefile;
  static String relativ;
  public static int jerseyNr;

// ------------------------------   MAIN   -------------------------------------
  public static void main(String[] args) throws FileNotFoundException, InterruptedException, IOException, AWTException
  {
    //<editor-fold defaultstate="collapsed" desc="comment">
    //Logger instantieren, konfigurieren
    try
    {
      SimpleLayout layout = new SimpleLayout();
      ConsoleAppender consoleAppender = new ConsoleAppender(layout);
      FileAppender x = new FileAppender(layout, "laser_log");
      log.addAppender(consoleAppender);
      FileAppender fileAppender = new FileAppender(layout, "logs/MeineLogDatei.log", false);
      log.addAppender(fileAppender);
      // ALL | DEBUG | INFO | WARN | ERROR | FATAL | OFF:
      log.setLevel(org.apache.log4j.Level.DEBUG);

      log.debug("Meine Debug-Meldung");
      log.info("Meine Info-Meldung");
      log.warn("Meine Warn-Meldung");
      log.error("Meine Error-Meldung");
      log.fatal("Meine Fatal-Meldung");

    } catch (Exception ex)
    {
      System.out.println(ex);
    }
//</editor-fold>

    fc = FieldCommander.getInstance();
    comView = ComView.getInstance();

    fc.setVisible(true);
    jc = JobController.getInstance();

    try
    {
      ipfile = new File("C:/Robotino/iprefbox");
      namefile = new File("C:/Robotino/name");
      encfile = new File("C:/Robotino/encfile");
      
      FileIO read = new FileIO();           
      
      refBoxIp = read.getText(ipfile);
      name = read.getText(namefile);
      encKey=read.getText(encfile);
      
      jerseyNr = getJerseyNr();
      jc.setRoboNameIdx(read.getText(namefile));
      fc.refbox.roboname.setText(name);
      comRefBox = new ComRefBox(refBoxIp, 4444, 4444);
      fc.setComRefBox(comRefBox);
    } catch (IOException ex)
    {
      System.out.println("File(s) not found !!!");
    }
  }
// ----------------------------------------------------------------------------- 

  public static void setIpRefbox(String ip) throws FileNotFoundException, IOException
  {
    refBoxIp = ip;
    System.out.println(ip);
    FileWriter schreiber = new FileWriter(ipfile);
    schreiber.write(ip);
    schreiber.flush();
  }

  public static void setNameRobo(String roboname) throws FileNotFoundException, IOException
  {
    name = roboname;
    FileWriter schreiber = new FileWriter(namefile);
    schreiber.write(roboname);
    schreiber.flush();
  }

  public static String getName()
  {
    return name;
  }

  public static void setEncryptionKey(String key) throws FileNotFoundException, IOException
  {
    encKey = key;
    System.out.println(encfile);
    FileWriter schreiber = new FileWriter(encfile);
    schreiber.write(encKey);
    schreiber.flush();
    schreiber.close();
  }

// ------------------------------   START   ------------------------------------
  public static void startServer() throws SocketException, IOException, InterruptedException, AWTException
  {
    log.debug("Startbutton is clicked");

    sm = new StateMachine(comRefBox, jc);

    comView.start();
    Thread.sleep(1000);
    sm.start();
    // Thread.sleep(500);
    sm.setRunning(true);
  }

  public static void stopServer()
  {
    comView.run = false;
  }

  public static int getJerseyNr()
  {
    int nr = 0;
    switch (name)
    {
      case "Solid1":
        nr = 1;
        break;
      case "Solid2":
        nr = 2;
        break;
      case "Solid3":
        nr = 3;
        break;
    }
    return nr;
  }
}
