/*
 * Projekt:         Robotino Team Solidus
 * Autor:           Steck Manuel
 * Datum:           08.06.2013
 * Geändert:        
 * Änderungsdatum:  
 * Version:         V_1.1.0_Explo
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
 *
 * @author stecm1
 */
public class Main
{

  public static org.apache.log4j.Logger log = org.apache.log4j.Logger.getRootLogger();

  public static String refBoxIp = "172.26.100.100";
  public static String refBoxPortIn = "4444";
  public static String refBoxPortOut = "4444";
  public static String name = "MrPink";
  static ComRefBox comRefBox;
  static ComView comView;
  static JobController way;
  static StateMachine sm;
  static FieldCommander fc;
  static File ipfile;
  static File portfile;
  static File namefile;
  static String relativ;
  public static int jerseyNr;

// ------------------------------   MAIN   -------------------------------------
  public static void main(String[] args) throws FileNotFoundException, InterruptedException, IOException, AWTException
  {
    //<editor-fold defaultstate="collapsed" desc="comment">
// Logger instantieren, konfigurieren
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
    way = JobController.getInstance();

    try
    {
      ipfile = new File("C:/Robotino/iprefbox");
      namefile = new File("C:/Robotino/name");

      FileIO read = new FileIO();
      read.getText(ipfile);
      read.getText(namefile);

      refBoxIp = read.getText(ipfile);
      name = read.getText(namefile);
      jerseyNr = getJerseyNr();
      way.setRoboNameIdx(read.getText(namefile));
      comRefBox = new ComRefBox(refBoxIp, Integer.valueOf(refBoxPortIn), Integer.valueOf(refBoxPortOut));
      fc.refbox.roboname.setText(name);

    } catch (IOException ex)
    {
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

  public static void setPortRefbox(String port) throws FileNotFoundException, IOException
  {
    refBoxPortIn = port;
    System.out.println(port);
    FileWriter schreiber = new FileWriter(portfile);
    schreiber.write(port);
    schreiber.flush();
    schreiber.close();
  }

// ------------------------------   START   ------------------------------------
  public static void startServer() throws SocketException, IOException, InterruptedException, AWTException
  {
    log.debug("Startbutton is clicked");

    sm = new StateMachine(comRefBox, way);

// Start Robotinos
    //robo.start();
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
      case "MrPink":
        nr = 1;
        break;
      case "MrBrown":
        nr = 2;
        break;
      case "MrBlond":
        nr = 3;
        break;
    }
    return nr;
  }
}