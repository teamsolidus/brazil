/*
 * Projekt:         Robotino Team Solidus
 * Autor:           Alain Rohr
 * Datum:           29.05.2013
 * Änderungsdatum:   4.07.2014  (Crypto-Version)
 * Version:         V_1.2.0_Explo
 */
package Refbox;

import FieldCommander.FieldCommander;
import MainPack.Main;
import Sequence.JobController;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import java.awt.AWTException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Observable;
import org.robocup_logistics.llsf_comm.*;
import org.robocup_logistics.llsf_msgs.BeaconSignalProtos.BeaconSignal;
import org.robocup_logistics.llsf_msgs.ExplorationInfoProtos;
import org.robocup_logistics.llsf_msgs.ExplorationInfoProtos.*;
import org.robocup_logistics.llsf_msgs.GameStateProtos.GameState;
import org.robocup_logistics.llsf_msgs.GameStateProtos.GameState.*;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos.*;
import org.robocup_logistics.llsf_msgs.MachineReportProtos.*;
import org.robocup_logistics.llsf_msgs.OrderInfoProtos.Order;
import org.robocup_logistics.llsf_msgs.OrderInfoProtos.OrderInfo;
import org.robocup_logistics.llsf_msgs.PuckInfoProtos;
import org.robocup_logistics.llsf_msgs.PuckInfoProtos.PuckInfo;
import roboCommunicationTest.RoboComTest;
import roboCommunicationTest.RoboComTest.RoboPos;
import org.robocup_logistics.llsf_msgs.TimeProtos.Time;

/**
 * @author stecm1/roa empfängt & sendet Meldungen von Refbox
 */
public class HandlerTry extends Observable implements ProtobufMessageHandler
{
  public static final int RED = 0, ORANGE = 1, GREEN = 2;
  FieldCommander fc;
  JobController jc;
// Lists from Communication
  public RoboPos pos;
  public GameState game;
  public List<Order> orderList;
  public List<ExplorationMachine> exMList;
  public List<Machine> mList;

// -------------------------- Game State ---------------------------------------
  public String gamePoints;           // Aktueller Punktestand
  public String gamePhase;            // Aktuelle Game Phase (PRE_GAME, EXPLORATION, PRODUCTION, POST_GAME)
  public String gameState;            // Aktueller Game Status (WAIT_START, RUNNING, PAUSED)
  public String hasTime;              // Ist noch zeit zur Verfügung
  public String gameTime;             // Aktuelle Spielzeit (EXPLORATION: 0-180, PRODUCTION: 0-900)
  public String logMessage;           // Logger Meldung für LopPanel
// ------------------------- BeaconInfo ----------------------------------------
  public int[] mTypLight;             // Lampen Informationen für Maschinentypen
// ---------------------------- Verbindung -------------------------------------
  ProtobufBroadcastPeer peer;  
  String[] send = new String[6];
// ---------------------------- MachineReport ----------------------------------
  //[Typ][Color]   (Typ 1-5-->6)(Color in RED,ORANGE,GREEN)
  int[][] mTypeDef =
  {
    {
      0, 0, 0
    },
    {
      0, 0, 0
    },
    {
      0, 0, 0
    },
    {
      0, 0, 0
    },
    {
      0, 0, 0
    },
    {
      0, 0, 0               
    }
  };

  //-----------------Test Robo Communication---------------------
  public int position, idRobo;
  public boolean isTakePuck;

  public String[] machineTyp;

  public HandlerTry(ProtobufBroadcastPeer peer) throws AWTException
  {
    this.peer = peer;
    System.out.println("gestartet");
    logMessage = "Der Server wurde gestartet !!!";
    fc = FieldCommander.getInstance();
    jc = JobController.getInstance();
//    jc.registerHandler(this);    
  }

  @Override
  public void handle_message(ByteBuffer in_msg, GeneratedMessage msg)
  {
// -------------------------- Puck Info ----------------------------------------
    if (msg instanceof PuckInfo)
    {
      byte[] array = new byte[in_msg.capacity()];
      in_msg.rewind();
      in_msg.get(array);
      /*3 Lines above could be ev. replaced by 
       byte[] array=in_msg.array();
       has to be tested --> replace in each Job*/
      PuckInfoProtos.PuckInfo info;

      try
      {
        info = PuckInfoProtos.PuckInfo.parseFrom(array);
        int count = info.getPucksCount();
        System.out.println("Number of pucks: " + count);
        List<PuckInfoProtos.Puck> pucks = info.getPucksList();
        for (int i = 0; i < pucks.size(); i++)
        {
          PuckInfoProtos.Puck puck = pucks.get(i);
          int id = puck.getId();
          System.out.println("  puck ID: " + id);
        }
      } catch (InvalidProtocolBufferException ex)
      {
        Main.log.error(ex);
      }
    } // -------------------------------- Order Info ---------------------------------
    else if (msg instanceof OrderInfo)
    {
      byte[] array = new byte[in_msg.capacity()];
      in_msg.rewind();
      in_msg.get(array);

      OrderInfo info;
      try
      {
        info = OrderInfo.parseFrom(array);

        orderList = info.getOrdersList();
        int i = 0;
        for (Order o : orderList)
        {
          Main.log.debug("Order " + (++i) + ":" + o);
        }

      } catch (InvalidProtocolBufferException ex)
      {
        Main.log.error(ex);
      }
    } // -------------------------------------   Game State   ------------------------------------------
    // Gibt die Aktuelle Spielphase, Spielstatus, Spielzeit, die Punkte und ob noch Zeit vorhanden ist.
    else if (msg instanceof GameState)
    {
      byte[] array = new byte[in_msg.capacity()];
      in_msg.rewind();
      in_msg.get(array);

      try
      {
        game = GameState.parseFrom(array);

        int points = game.getPointsCyan();
        Phase phase = game.getPhase();
        State state = game.getState();
        Time time = game.getGameTime();

        hasTime = game.hasGameTime() + "";

        setChanged();
        notifyObservers(send);

        gamePoints = points + "";
        gamePhase = phase.name();
        gameState = state.name();
        gameTime = time.getSec() + "";
      } catch (InvalidProtocolBufferException ex)
      {
        Main.log.error(ex);
      }
    } // ---- Machine Info -- ProdPhase -- 0.25s-----------------
    else if (msg instanceof MachineInfo)
    {
      System.out.println("MACHINE INFO");

      byte[] array = new byte[in_msg.capacity()];
      in_msg.rewind();
      in_msg.get(array);

      try
      {
        MachineInfo mInfo;
        mInfo = MachineInfoProtos.MachineInfo.parseFrom(array);
        mList = mInfo.getMachinesList();
        jc.setMachineTypesFromRefBox(mList);

      } catch (InvalidProtocolBufferException ex)
      {
        Main.log.error(ex);
      }

      /*    
       try
       {         
       int count = info.getMachinesCount();
       System.out.println("Anzahl Maschinen: " + count);
       List<MachineInfoProtos.Machine> machines = info.getMachinesList();
             
       MachineInfoProtos.Machine machine = machines.get(0);
            
       int inputsCount = machine.getInputsCount();
       List<LightSpec> lightlist = machine.getLightsList();                            
       }
       catch (InvalidProtocolBufferException ex)
       {
       Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
       }
       */
    } // ---- Exploration Info --ExploPhase --- 1s -----------------------------------
    else if (msg instanceof ExplorationInfo)
    {
      byte[] array = new byte[in_msg.capacity()];
      in_msg.rewind();
      in_msg.get(array);

      ExplorationInfoProtos.ExplorationInfo info;

      try
      {
        info = ExplorationInfoProtos.ExplorationInfo.parseFrom(array);

        List<ExplorationSignal> sList = info.getSignalsList();
        for (int i = 0; i < 5; i++)
        {
          ExplorationSignal m0 = sList.get(i);
          mTypeDef[i][RED] = m0.getLights(0).getState().getNumber();
          mTypeDef[i][ORANGE] = m0.getLights(1).getState().getNumber();
          mTypeDef[i][GREEN] = m0.getLights(2).getState().getNumber();
        }

// ------------------ Array[15] füllen: Station 1 - 3 platz 0 - 2... -----------
        mTypLight = new int[15];
        int m = 0;
        for (int i = 0; i < 15; i += 3)
        {
          mTypLight[i] = mTypeDef[m][RED];
          mTypLight[i + 1] = mTypeDef[m][ORANGE];
          mTypLight[i + 2] = mTypeDef[m][GREEN];
          logMessage = "MTyp " + m + " => RED: " + mTypLight[i] + " ORANGE: " + mTypLight[i + 1] + " GREEN: " + mTypLight[i + 2];
          m++;
        }
        exMList = info.getMachinesList();
        jc.setExploMachinesFromRefBox(exMList);

      } catch (InvalidProtocolBufferException ex)
      {
        Main.log.error(ex);
      }
    } // -----Beacon Signal -- 1s-------------------------------
    else if (msg instanceof BeaconSignal)
    {
      byte[] array = new byte[in_msg.capacity()];
      in_msg.rewind();
      in_msg.get(array);

      send[0] = gamePoints;
      send[1] = gamePhase;
      send[2] = gameState;
      send[3] = gameTime;
      send[4] = hasTime;
      send[5] = logMessage;
      setChanged();
      notifyObservers(send);
    } //-----------ROBO COM TEST--------------------------------        
    else if (msg instanceof RoboPos)
    {
      byte[] array = new byte[in_msg.capacity()];
      in_msg.rewind();
      in_msg.get(array);

      try
      {
        pos = RoboPos.parseFrom(array);

        position = pos.getX();
        idRobo = pos.getId();
        isTakePuck = pos.getTakePuck();

        //setChanged();
        //notifyObservers(send);
      } catch (InvalidProtocolBufferException ex)
      {
        Main.log.error(ex);
      }
    }
  }

  public void sendMachine(String name, int[] lamp)
  {
    try
    {
      Thread.sleep(500);
    } catch (InterruptedException ex)
    {
      Main.log.error(ex);
    }

    String type = "";

    if (lamp[RED] == mTypLight[0] && lamp[ORANGE] == mTypLight[1] && lamp[GREEN] == mTypLight[2])
    {
      type = "T1";
    }
    if (lamp[RED] == mTypLight[3] && lamp[ORANGE] == mTypLight[4] && lamp[GREEN] == mTypLight[5])
    {
      type = "T2";
    }
    if (lamp[RED] == mTypLight[6] && lamp[ORANGE] == mTypLight[7] && lamp[GREEN] == mTypLight[8])
    {
      type = "T3";
    }
    if (lamp[RED] == mTypLight[9] && lamp[ORANGE] == mTypLight[10] && lamp[GREEN] == mTypLight[11])
    {
      type = "T4";
    }
    if (lamp[RED] == mTypLight[12] && lamp[ORANGE] == mTypLight[13] && lamp[GREEN] == mTypLight[14])
    {
      type = "T5";
    }
    fc.machineMap.get(name).setmTyp(type);// hier wird der jeweilige Maschinentyp in die entsprechende zelle (von der Map geholt) gespeichert

    try
    {
      Thread.sleep(1000);
    } catch (InterruptedException ex)
    {
      Main.log.error(ex);
    }
    MachineReportEntry mi = MachineReportEntry.newBuilder().
      setName(name).
      setType(type).
      build();
    //MachineReport mr = MachineReport.newBuilder().setMachines(1, mi).build();  //for just one Machine
    MachineReport mr = MachineReport.newBuilder().addMachines(mi).setTeamColor(JobController.TEAM).build();
    ProtobufMessage machineReport = new ProtobufMessage(2000, 61, mr);
    peer.enqueue(machineReport);
  }

  public void sendRoboMsg(boolean takePuck, int id, int x)
  {      
    RoboComTest.RoboPos rp = RoboComTest.RoboPos.newBuilder().
      setTakePuck(takePuck).
      setId(jc.getRoboNameIdx()).
      setX(x).
      build();

    ProtobufMessage roboReport = new ProtobufMessage(2000, 61, rp);
    peer.enqueue(roboReport);
  }

  /**   
   * @return Gibt den aktuellen Spiel Status zurück (WAIT_STRT, RUNNING,
   * PAUSED).
   */
  public String getState()
  {
    return gameState;
  }
        
  /**   
   * @return Gibt die aktuelle Spiel Phase zurück (PRE_GAME, EXPLORATION,
   * PRODUCTION, POST_GAME).
   */
  public String getPhase()
  {
    return gamePhase;
  }

  /**
   *
   * @return Git an ob noch Spielzeit vorhanden ist.
   */
  public String getHasTime()
  {
    return hasTime;
  }

  /**   
   * @return Gibt aktuelle Spielzeit zurück (EXPLORATION: 0-180, PRODUCTION:
   * 0-900).
   */
  public String getTime()
  {
    return gameTime;
  }

  /**   
   * @return Gibt den Aktuellen Punktestand zurück.
   */
  public String getPoints()
  {
    return gamePoints;
  }

  /**   
   * @return Gibt die von der Refbox zugewiesenen Lichter der 5 Maschinentypen
   * zurück: array[15] array[0] - array[2] Maschinentyp 1: [0] = Rote Lampe, [1]
   * = Orange Lampe, [2] = Grüne Lampe...
   */
  public int[] getMachineTyp()
  {
    return mTypLight;
  }

  @Override
  public void connection_lost(IOException e)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void timeout()
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}