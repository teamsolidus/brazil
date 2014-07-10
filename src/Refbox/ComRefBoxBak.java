package Refbox;

import ComView.FileIO;
import FieldCommander.FieldCommander;
import MainPack.Main;
import Sequence.JobController;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.robocup_logistics.llsf_comm.ProtobufBroadcastPeer;
import org.robocup_logistics.llsf_comm.ProtobufMessage;
import org.robocup_logistics.llsf_comm.ProtobufMessageHandler;
import org.robocup_logistics.llsf_msgs.BeaconSignalProtos.BeaconSignal;
import org.robocup_logistics.llsf_msgs.ExplorationInfoProtos.ExplorationInfo;
import org.robocup_logistics.llsf_msgs.ExplorationInfoProtos.ExplorationMachine;
import org.robocup_logistics.llsf_msgs.ExplorationInfoProtos.ExplorationSignal;
import org.robocup_logistics.llsf_msgs.GameStateProtos.GameState;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos.Machine;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos.MachineInfo;
import org.robocup_logistics.llsf_msgs.MachineReportProtos.MachineReportInfo;
import org.robocup_logistics.llsf_msgs.OrderInfoProtos.Order;
import org.robocup_logistics.llsf_msgs.OrderInfoProtos.OrderInfo;
import org.robocup_logistics.llsf_msgs.RobotInfoProtos.RobotInfo;
import org.robocup_logistics.llsf_msgs.TeamProtos.Team;
import org.robocup_logistics.llsf_msgs.TimeProtos.Time;
import org.robocup_logistics.llsf_msgs.VersionProtos.VersionInfo;
import org.robocup_logistics.llsf_utils.NanoSecondsTimestampProvider;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import java.awt.AWTException;
import java.io.File;
import java.util.List;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos;
import org.robocup_logistics.llsf_msgs.MachineReportProtos.MachineReport;
import org.robocup_logistics.llsf_msgs.MachineReportProtos.MachineReportEntry;
import org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D;
import roboCommunicationTest.RoboComTest;
import roboCommunicationTest.RoboComTest.RoboPos;

public class ComRefBox
{

  private static String ROBOT_NAME;
  private static String TEAM_NAME;
  private static Team TEAM_COLOR;
  private static String ENCRYPTION_KEY;

  private static String HOST;
  private static boolean local = false;

  private final static int SENDPORT = 4445;
  private final static int RECVPORT = 4444;

  private final static int CYAN_SENDPORT = 4446;
  private final static int CYAN_RECVPORT = 4441;

  private final static int MAGENTA_SENDPORT = 4447;
  private final static int MAGENTA_RECVPORT = 4442;

// ---------------------------- Verbindung -------------------------------------
  private static ProtobufBroadcastPeer peerPublic;
  private static ProtobufBroadcastPeer peerPrivate;

  private static boolean crypto_setup = false;

//------added for HFTMProj
  private static FieldCommander fc;
  private static JobController jc;
  public static final int RED = 0, ORANGE = 1, GREEN = 2;

// Lists from Communication
  static String[] send = new String[6];
  public static List<Order> orderList;
  public static List<ExplorationMachine> exMList;
  public static List<Machine> mList;
  // -------------------------- Game State ---------------------------------------
  public static GameState game;              // Game Status
  public static String gamePhase;            // Aktuelle Game Phase (PRE_GAME, EXPLORATION, PRODUCTION, POST_GAME)
  public static String gameState;            // Aktueller Game Status (WAIT_START, RUNNING, PAUSED)

  public static String logMessage;           // Logger Meldung für LopPanel

  public static int[] mTypLight;             // Lampen Informationen für Maschinentypen

// ---------------------------- MachineReport ----------------------------------
  //[Typ][Color]   (Typ 1-5-->6)(Color in RED,ORANGE,GREEN)
  static int[][] mTypeDef =
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

  public ComRefBox(String ip, int portIn, int portOut) throws AWTException
  {
    ROBOT_NAME = "Solid1";
    TEAM_NAME = "Solidus";
    ENCRYPTION_KEY = "randomkey";
    TEAM_COLOR = JobController.TEAM;
    HOST = ip;
    local = false;  //if refbox is on same machine
    peerPublic = new ProtobufBroadcastPeer(HOST, local ? SENDPORT : RECVPORT, RECVPORT);
    try
    {
      peerPublic.start();
    } catch (IOException e)
    {
      e.printStackTrace();
    }

    peerPublic.<BeaconSignal>add_message(BeaconSignal.class);
    peerPublic.<OrderInfo>add_message(OrderInfo.class);
    peerPublic.<GameState>add_message(GameState.class);
    peerPublic.<ExplorationInfo>add_message(ExplorationInfo.class);

    //peerPublic.<MachineInfo>add_message(MachineInfo.class);
    //peerPublic.<RoboPos>add_message(RoboPos.class);
    //wird momentan nicht gebraucht
    //peerPublic.<VersionInfo>add_message(VersionInfo.class);
    //peerPublic.<MachineReportInfo>add_message(MachineReportInfo.class);
    //peerPublic.<RobotInfo>add_message(RobotInfo.class);   
    Handler handler = new Handler();
    peerPublic.register_handler(handler);

    BeaconThread thread = new BeaconThread();
    thread.start();

    fc = FieldCommander.getInstance();
    jc = JobController.getInstance();
    jc.registerComRefBox(this);
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
    peerPublic.enqueue(machineReport);
  }

  public void sendRoboMsg(boolean takePuck, int id, int x)
  {
    RoboComTest.RoboPos rp = RoboComTest.RoboPos.newBuilder().
      setTakePuck(takePuck).
      setId(jc.getRoboNameIdx()).
      setX(x).
      build();

    ProtobufMessage roboReport = new ProtobufMessage(2000, 61, rp);
    peerPublic.enqueue(roboReport);
  }

//------------------------------------------------------------------------------
  /**
   * Thread for Beacon Signal each 2 sec
   */
  private static class BeaconThread extends Thread
  {

    int seq = 1;

    public void run()
    {
      while (true)
      {
        try
        {
          Thread.sleep(2000);
        } catch (InterruptedException e)
        {
          e.printStackTrace();
        }

        NanoSecondsTimestampProvider nstp = new NanoSecondsTimestampProvider();

        long ms = System.currentTimeMillis();
        long ns = nstp.currentNanoSecondsTimestamp();

        int sec = (int) (ms / 1000);
        long nsec = ns - (ms * 1000000L);

        Time t = Time.newBuilder().setSec(sec).setNsec(nsec).build();
        Pose2D pos = Pose2D.newBuilder().setX(100).setY(100).setOri(0).setTimestamp(t).build();

        BeaconSignal bs = BeaconSignal.newBuilder().
          setTime(t).
          setSeq(seq++).
          setNumber(1).
          setPeerName(ROBOT_NAME).
          setTeamName(TEAM_NAME).
          setTeamColor(TEAM_COLOR).
          //setPose(pos).
          build();

        ProtobufMessage msg = new ProtobufMessage(2000, 1, bs);

        if (crypto_setup)
        {
          peerPrivate.enqueue(msg);
        } else
        {
          peerPublic.enqueue(msg);
        }
      }
    }
  }

//------------------------------------------------------------------------------
  private static class Handler implements ProtobufMessageHandler
  {

    @Override
    public void handle_message(ByteBuffer in_msg, GeneratedMessage msg)
    {
      if (msg instanceof BeaconSignal)
      {
        byte[] array = new byte[in_msg.capacity()];
        in_msg.rewind();
        in_msg.get(array);
        BeaconSignal bs;
        Time t;

        try
        {
          bs = BeaconSignal.parseFrom(array);
          t = bs.getTime();
          System.out.printf("Detected robot: %d %s:%s (seq %d)\n", bs.getNumber(), bs.getTeamName(), bs.getPeerName(), t.getSec());

        } catch (InvalidProtocolBufferException e)
        {
          e.printStackTrace();
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
          System.out.println("Order info received:");
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
          
          gamePhase = game.getPhase().name();
          gameState = game.getState().name();

          if (!crypto_setup)                                  //first time flag
          {
            crypto_setup = true;

            if (TEAM_NAME.equals(game.getTeamCyan()))
            {
              TEAM_COLOR = Team.CYAN;
            } else if (TEAM_NAME.equals(game.getTeamMagenta()))
            {
              TEAM_COLOR = Team.MAGENTA;
            } else
            {
              System.out.println("Our team is not set, training game? Disabling crypto.");
              crypto_setup = false;
              return;
            }

            switch (TEAM_COLOR)
            {
              case CYAN:
                peerPrivate = new ProtobufBroadcastPeer(HOST, local ? CYAN_SENDPORT : CYAN_RECVPORT, CYAN_RECVPORT, true, 2, ENCRYPTION_KEY);
                break;
              case MAGENTA:
                peerPrivate = new ProtobufBroadcastPeer(HOST, local ? MAGENTA_SENDPORT : MAGENTA_RECVPORT, MAGENTA_RECVPORT, true, 2, ENCRYPTION_KEY);
                break;
            }

            if (peerPrivate != null)
            {
              try
              {
                peerPrivate.start();
              } catch (IOException e)
              {
                e.printStackTrace();
              }

              peerPrivate.<BeaconSignal>add_message(BeaconSignal.class);
              peerPrivate.<OrderInfo>add_message(OrderInfo.class);
              peerPrivate.<GameState>add_message(GameState.class);
              peerPrivate.<VersionInfo>add_message(VersionInfo.class);
              peerPrivate.<ExplorationInfo>add_message(ExplorationInfo.class);
              peerPrivate.<MachineInfo>add_message(MachineInfo.class);
              peerPrivate.<MachineReportInfo>add_message(MachineReportInfo.class);
              peerPrivate.<RobotInfo>add_message(RobotInfo.class);

              Handler handler = new Handler();
              peerPrivate.register_handler(handler);
            }
          }
        } catch (InvalidProtocolBufferException e)
        {
          e.printStackTrace();
        }
      } // ---- Exploration Info --ExploPhase --- 1s -----------------------------------
      else if (msg instanceof ExplorationInfo)
      {
        byte[] array = new byte[in_msg.capacity()];
        in_msg.rewind();
        in_msg.get(array);
        ExplorationInfo info;

        try
        {
          info = ExplorationInfo.parseFrom(array);

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

      }// ---- Machine Info -- ProdPhase -- 0.25s----------------- 
      else if (msg instanceof MachineInfo)
      {
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
      } //-----------ROBO COM TEST--------------------------------
      else if (msg instanceof RoboPos)
      {
        byte[] array = new byte[in_msg.capacity()];
        in_msg.rewind();
        in_msg.get(array);

        try
        {
          RoboPos pos = RoboPos.parseFrom(array);
          //setChanged();
          //notifyObservers(send);
        } catch (InvalidProtocolBufferException ex)
        {
          Main.log.error(ex);
        }
      }
    }

    public void connection_lost(IOException e)
    {
      System.out.println("Connection lost");
    }

    public void timeout()
    {
    }
  }

  public static void main(String[] args)
  {
    int refBoxPortIn = 4444;
    int refBoxPortOut = 4444;
    String name;

    File ipfile;
    File namefile;

    try
    {
      ipfile = new File("C:/Robotino/iprefbox");
      namefile = new File("C:/Robotino/name");

      FileIO read = new FileIO();
      read.getText(ipfile);
      read.getText(namefile);

      String refBoxIp = read.getText(ipfile);
      name = read.getText(namefile);

      ComRefBox comRefBox = new ComRefBox(refBoxIp, refBoxPortIn, refBoxPortOut);
      //Thread.sleep(5000);
      //comRefBox.sendRoboMsg(true,111,222);
    } catch (Exception ex)
    {
      System.out.println(ex);
    }
  }
}
