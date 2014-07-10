/**
 * Projekt:         Robotino Team Solidus
 * Autor:           Steck Manuel
 * Datum:           07.06.2013
 * Geändert:        
 * Änderungsdatum:  
 * Version:         V_1.1.0_Explo
 */
package Refbox;

import ComView.FileIO;
import static MainPack.Main.*;
import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.util.Observer;
import org.robocup_logistics.llsf_comm.ProtobufBroadcastPeer;
import org.robocup_logistics.llsf_msgs.ExplorationInfoProtos.ExplorationInfo;
import org.robocup_logistics.llsf_msgs.GameStateProtos.GameState;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos.MachineInfo;
import org.robocup_logistics.llsf_msgs.OrderInfoProtos.OrderInfo;
import roboCommunicationTest.RoboComTest.RoboPos;

/**
 *
 * @author stecm1
 */
public class ComRefBoxTry
{
  public HandlerOld handler;
  public ProtobufBroadcastPeer peerPublic;
  public ProtobufBroadcastPeer peerPrivate;

  public int gamePoints;             // Aktueller Punktestand
  public String gamePhase;           // Aktuelle Game Phase (PRE_GAME, EXPLORATION, PRODUCTION, POST_GAME)
  public String gameState;           // Aktueller Game Status (WAIT_STRT, RUNNING, PAUSED)
  public boolean hasTime;            // Ist noch zeit zur Verfügung
  public int gameTime;               // Aktuelle Spielzeit (EXPLORATION: 0-180, PRODUCTION: 0-900)

  public ComRefBoxTry(String ip, int portIn, int portOut) throws AWTException
  {
    // --------------------------- Verbinden mit Refbox per UDP ----------------------------
    peerPublic = new ProtobufBroadcastPeer(ip, portIn, portOut);
    
    peerPublic.<ExplorationInfo>add_message(ExplorationInfo.class);//alle 1sec refbox --> robots        
    peerPublic.<GameState>add_message(GameState.class);
    peerPublic.<MachineInfo>add_message(MachineInfo.class);  //0.25sec ??? refbox --> all P2P & C-S    
    peerPublic.<OrderInfo>add_message(OrderInfo.class);//any P2P & C-S        
    //peer.<AttentionMessage>add_message(AttentionMessage.class);//refbox --> controller Client-Server
    //peer.<VersionInfo>add_message(VersionInfo.class);//refbox --> any  P2P&C-S    
    //peer.<PuckInfo>add_message(PuckInfo.class);  // 1sec ok refbox --> controller Client-Server
    //peer.<RobotInfo>add_message(RobotInfo.class);
    //peer.<RoboPos>add_message(RoboPos.class);      

    handler = new HandlerOld(peerPublic);
    peerPublic.register_handler(handler);

    try
    {
      peerPublic.start();
    } catch (IOException e)
    {
      e.printStackTrace();
    }
    new Thread(new Beacon(peerPublic)).start();
  }

  public void addObserver(Observer observer)
  {
    handler.addObserver(observer);
  }

  public String getState()
  {
    return handler.getState();
  }

  /**
   * @return Gibt die aktuelle Spiel Phase zurück (PRE_GAME, EXPLORATION,PRODUCTION, POST_GAME).
   */
  public String getPhase()
  {
    return handler.getPhase();
  }

  /**
   * @return Git an ob noch Spielzeit vorhanden ist.
   */
  public String getHasTime()
  {
    return handler.getHasTime();
  }

  /**  
   * @return Gibt die aktuelle Spielzeit zurück (EXPLORATION: 0-180, PRODUCTION: 0-900).
   */
  public String getTime()
  {
    return handler.getTime();
  }

  /**   
   * @return Gibt den Aktuellen Punktestand zurück.
   */
  public String getPoints()
  {
    return handler.getPoints();
  }

  public static void main(String[] args)
  {
    String refBoxIp = "172.26.100.100";
    int refBoxPortIn = 4444;
    int refBoxPortOut = 4444;
    String name = "MrPink";

    File ipfile;
    File namefile;
    int jerseyNr;

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
      ComRefBox comRefBox = new ComRefBox(refBoxIp, refBoxPortIn, refBoxPortOut);  
      //Thread.sleep(5000);
      //comRefBox.handler.sendRoboMsg(true,111,222);
    } catch (Exception ex)
    {
      System.out.println(ex);
    }
  }
}