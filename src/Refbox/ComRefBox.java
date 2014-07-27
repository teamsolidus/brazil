package Refbox;

import FieldCommander.FieldCommander;
import FieldCommander.OtherRoboPosition;
import FieldCommander.OtherRoboPosition.BaconMessagePart;
import MainPack.Main;
import Sequence.JobController;
import Tools.SolidusLoggerFactory;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import java.awt.AWTException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import org.apache.log4j.Logger;
import org.robocup_logistics.llsf_comm.ProtobufBroadcastPeer;
import org.robocup_logistics.llsf_comm.ProtobufMessage;
import org.robocup_logistics.llsf_comm.ProtobufMessageHandler;
import org.robocup_logistics.llsf_msgs.BeaconSignalProtos.BeaconSignal;
import org.robocup_logistics.llsf_msgs.ExplorationInfoProtos.ExplorationInfo;
import org.robocup_logistics.llsf_msgs.ExplorationInfoProtos.ExplorationMachine;
import org.robocup_logistics.llsf_msgs.ExplorationInfoProtos.ExplorationSignal;
import org.robocup_logistics.llsf_msgs.GameStateProtos.GameState;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos.Machine;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos.MachineInfo;
import org.robocup_logistics.llsf_msgs.MachineReportProtos.MachineReport;
import org.robocup_logistics.llsf_msgs.MachineReportProtos.MachineReportEntry;
import org.robocup_logistics.llsf_msgs.OrderInfoProtos.Order;
import org.robocup_logistics.llsf_msgs.OrderInfoProtos.OrderInfo;
import org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D;
import org.robocup_logistics.llsf_msgs.RobotInfoProtos.RobotInfo;
import org.robocup_logistics.llsf_msgs.TeamProtos.Team;
import org.robocup_logistics.llsf_msgs.TimeProtos.Time;
import org.robocup_logistics.llsf_msgs.VersionProtos.VersionInfo;
import org.robocup_logistics.llsf_utils.NanoSecondsTimestampProvider;

public class ComRefBox
{
    // Logging
    private static org.apache.log4j.Logger MACHINE_LOGGER, mainLogger;

    private static String TEAM_NAME = "Solidus";
    private static String ENCRYPTION_KEY;
    public final static int JERSEY_NR = Main.getJerseyNr();

    /**
     *
     */
    private static String ROBOT_NAME;

    /**
     * Recived from refbox (game state message)
     */
    private static Team TEAM_COLOR;
    private static String HOST;

    private static boolean local = false;// if refbox is on same machine

    private final static int PUBLIC_SENDPORT = 4445;
    private final static int PUBLIC_RECVPORT = 4444;

    private final static int CYAN_SENDPORT = 4446;
    private final static int CYAN_RECVPORT = 4441;

    private final static int MAGENTA_SENDPORT = 4447;
    private final static int MAGENTA_RECVPORT = 4442;

    // CONNECTION
    private static ProtobufBroadcastPeer peerPublic;   //open public channel for all
    private static ProtobufBroadcastPeer peerPrivate;  //private encrypted channel for team

    /**
     * Flag for open the private peer when the refbox send the
     */
    private static boolean cryptoInitFlag = false;

    // ADDED FOR SOLIDUS
    private static FieldCommander fc;
    private static JobController jc;
    public static final int RED = 0, ORANGE = 1, GREEN = 2;

    // Lists from Communication
    public static List<Order> orderList;
    public static List<ExplorationMachine> exMList;
    public static List<Machine> mList;

    // GameStates
    public static GameState game;              // Game Status
    public static String gamePhase;            // Aktuelle Game Phase (PRE_GAME, EXPLORATION, PRODUCTION, POST_GAME)
    public static String gameState;            // Aktueller Game Status (WAIT_START, RUNNING, PAUSED)

    public static int[] mTypLight;             // Lampen Informationen für Maschinentypen

    // MachineReport
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
        ROBOT_NAME = Main.name;
        TEAM_COLOR = JobController.TEAM;
        ENCRYPTION_KEY = Main.encKey;
        HOST = ip;

        // Set Logger
        this.mainLogger = Main.log;// Root Logger
        this.MACHINE_LOGGER = SolidusLoggerFactory.getMachine();

        peerPublic = new ProtobufBroadcastPeer(HOST, local ? PUBLIC_SENDPORT : PUBLIC_RECVPORT, PUBLIC_RECVPORT);
        try
        {
            peerPublic.start();
        }
        catch (IOException e)
        {
            mainLogger.fatal("Error while starting public peer. Message: " + e.getMessage());
        }

        peerPublic.<GameState>add_message(GameState.class);
        peerPublic.<ExplorationInfo>add_message(ExplorationInfo.class);
        peerPublic.<VersionInfo>add_message(VersionInfo.class);
        peerPublic.<RobotInfo>add_message(RobotInfo.class);

        Handler handler = new Handler();
        peerPublic.register_handler(handler);

        fc = FieldCommander.getInstance();
        jc = JobController.getInstance();
        jc.registerComRefBox(this);
    }

    public void sendMachine(String name, int[] lamp)
    {   
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
        // Check Result
        if(type.isEmpty())
        {
            MACHINE_LOGGER.error("Detected color code contains to no machine type. -> RED: " + lamp[RED] +" ORANGE: " + lamp[ORANGE] + " GREEN: " + lamp[GREEN]);
            return;
        }
        
        fc.machineMap.get(name).setmTyp(type); // hier wird der jeweilige Maschinentyp in die entsprechende zelle (von der Map geholt) gespeichert

        MachineReportEntry mi = MachineReportEntry.newBuilder().
                setName(name).
                setType(type).
                build();

        MACHINE_LOGGER.debug("Send machine to refbox. Name: " + name
                + " Type: " + type
                + " RED: " + lamp[this.RED]
                + " ORANGE: " + lamp[this.ORANGE]
                + " GREEN: " + lamp[this.GREEN]);

        MachineReport mr = MachineReport.newBuilder().addMachines(mi).setTeamColor(JobController.TEAM).build(); //alle bereits erkannte senden(additiv)
        ProtobufMessage machineReport = new ProtobufMessage(2000, 61, mr);
        peerPrivate.enqueue(machineReport);
    }

    /**
     * Thread for Beacon Signal each 2 sec. Started when the private peer can be
     * build (set teamcolor from refbox)
     */
    private static class BeaconThread extends Thread
    {

        public void run()
        {
            mainLogger.debug("Bacon Thread started!");
            while (true)
            {
                try
                {
                    Thread.sleep(2000);
                }
                catch (InterruptedException e)
                {
                    mainLogger.fatal("Error in BaconThread while thread sleep.");
                }

                NanoSecondsTimestampProvider nstp = new NanoSecondsTimestampProvider();

                long ms = System.currentTimeMillis();
                long ns = nstp.currentNanoSecondsTimestamp();

                int sec = (int) (ms / 1000);
                long nsec = ns - (ms * 1000000L);

                Time t = Time.newBuilder().setSec(sec).setNsec(nsec).build();

                OtherRoboPosition otherRoboPosition = fc.getRoboPos(JERSEY_NR);
                Pose2D pos = Pose2D.newBuilder().setX(otherRoboPosition.getBaconMessagePart(BaconMessagePart.POS_X))
                        .setY(otherRoboPosition.getBaconMessagePart(BaconMessagePart.POS_Y))
                        .setOri(otherRoboPosition.getBaconMessagePart(BaconMessagePart.ORIENTATION_AND_CATCHING_PUCK)).setTimestamp(t).build();

                BeaconSignal baconSignal = BeaconSignal.newBuilder().
                        setTime(t).
                        setSeq(1).
                        setNumber(JERSEY_NR).
                        setPeerName(ROBOT_NAME).
                        setTeamName(TEAM_NAME).
                        setTeamColor(TEAM_COLOR).
                        setPose(pos).
                        build();

                ProtobufMessage msg = new ProtobufMessage(2000, 1, baconSignal);

                peerPrivate.enqueue(msg);
            }
        }
    }

    private static class Handler implements ProtobufMessageHandler
    {

        @Override
        public void handle_message(ByteBuffer in_msg, GeneratedMessage msg)
        {
            // BACON SIGNAL
            if (msg instanceof BeaconSignal)
            {
                byte[] array = new byte[in_msg.capacity()];
                in_msg.rewind();
                in_msg.get(array);
                BeaconSignal beaconSignal;
                Time t;

                try
                {
                    beaconSignal = BeaconSignal.parseFrom(array);
                    t = beaconSignal.getTime();

                    /*log.debug("Robo detected! Nr: " + beaconSignal.getNumber() + 
                     " Team: " +  beaconSignal.getTeamName() + 
                     " Peer: " + beaconSignal.getPeerName() + 
                     " Live-Time: " + t.getSec() + 
                     "s. PosX: " + beaconSignal.getPose().getX() + 
                     " PosY: " + beaconSignal.getPose().getY() + 
                     " PosAngle:" + beaconSignal.getPose().getOri());*/
                    if (beaconSignal.getNumber() != JERSEY_NR && beaconSignal.getNumber() > 0)
                    {
                        // Not this Robo, so inform the fieldcommander
                        fc.getRoboPos(beaconSignal.getNumber()).setBaconMessagePart(BaconMessagePart.POS_X, (int) beaconSignal.getPose().getX())
                                .setBaconMessagePart(BaconMessagePart.POS_Y, (int) beaconSignal.getPose().getY())
                                .setBaconMessagePart(BaconMessagePart.ORIENTATION_AND_CATCHING_PUCK, (int) beaconSignal.getPose().getOri());

                        OtherRoboPosition tempForDebug = fc.getRoboPos(beaconSignal.getNumber());
                        mainLogger.debug("Solidus Robo detected! Nr: " + beaconSignal.getNumber()
                                + " PosX: " + tempForDebug.getPosX()
                                + " PosY: " + tempForDebug.getPosY()
                                + " PosAngle:" + tempForDebug.getPosOrientation()
                                + " Is Puck Catching: " + tempForDebug.getIsCatchingPuck());
                    }
                }
                catch (InvalidProtocolBufferException e)
                {
                    mainLogger.fatal("Fault while parsing bacon signal. Message: " + e.getMessage());
                }
            }

            // ORDER INFO
            else if (msg instanceof OrderInfo)
            {
                byte[] array = new byte[in_msg.capacity()];
                in_msg.rewind();
                in_msg.get(array);
                OrderInfo info;

                try
                {
                    info = OrderInfo.parseFrom(array);
                    mainLogger.debug("Order info received:");
                    orderList = info.getOrdersList();
                    int i = 0;
                    for (Order o : orderList)
                    {
                        Main.log.debug("Order " + (++i) + ":" + o);
                    }

                }
                catch (InvalidProtocolBufferException ex)
                {
                    mainLogger.error("Error while parsing order info. Message: " + ex);
                }
            }

            // GAME STATE
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

                    mainLogger.debug("Gamestate Name : " + gamePhase + " Gamestate State: " + gameState);

                    // Waiting for the first game state message, in which are the colors / temnames defined
                    if (!cryptoInitFlag)// first time flag
                    {
                        cryptoInitFlag = true;

                        if (TEAM_NAME.equals(game.getTeamCyan()))
                        {
                            TEAM_COLOR = Team.CYAN;
                        }
                        else if (TEAM_NAME.equals(game.getTeamMagenta()))
                        {
                            TEAM_COLOR = Team.MAGENTA;
                        }
                        else
                        {
                            mainLogger.debug("No equal teamname in gamestate message. Name in cyan message:" + game.getTeamCyan() + "Name in mangenta message: " + game.getTeamMagenta());
                            cryptoInitFlag = false; // reset flag
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
                            }
                            catch (IOException e)
                            {
                                mainLogger.fatal("Exception while start private peer. Message: " + e.getMessage());
                            }

                            // Add Handled Messages
                            peerPrivate.<BeaconSignal>add_message(BeaconSignal.class);
                            peerPrivate.<OrderInfo>add_message(OrderInfo.class);
                            peerPrivate.<MachineInfo>add_message(MachineInfo.class);
                            peerPrivate.<MachineReport>add_message(MachineReport.class); //only send!

                            Handler handler = new Handler();
                            peerPrivate.register_handler(handler);

                            // Private Peer created, so start the thread
                            BeaconThread thread = new BeaconThread();
                            thread.setName("ComRefBox_Bacon_Thread");
                            thread.start();
                        }
                    }
                }
                catch (InvalidProtocolBufferException e)
                {
                    mainLogger.fatal("Error while parsing game state. Message: " + e.getMessage());
                }
            }

            // EXPLORATION INFO
            // In explophase, every second
            // Contains the info, which machine type represents which color code
            else if (msg instanceof ExplorationInfo)
            {
                byte[] array = new byte[in_msg.capacity()];
                in_msg.rewind();
                in_msg.get(array);
                ExplorationInfo info;

                mainLogger.debug("Exploration info received:");

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

                    // Array[15] füllen: Station 1 - 3 platz 0 - 2...
                    mTypLight = new int[15];
                    int m = 0;
                    
                    MACHINE_LOGGER.debug("MACHINE DEFINITIONS arrived!");

                    for (int i = 0; i < 15; i += 3)
                    {
                        mTypLight[i] = mTypeDef[m][RED];
                        mTypLight[i + 1] = mTypeDef[m][ORANGE];
                        mTypLight[i + 2] = mTypeDef[m][GREEN];

                        String logMessage = "Machine typ M" + m + " -> RED: " + mTypLight[i] + " ORANGE: " + mTypLight[i + 1] + " GREEN: " + mTypLight[i + 2];
                        MACHINE_LOGGER.debug(logMessage);

                        m++;
                    }
                    exMList = info.getMachinesList();
                    jc.setExploMachinesFromRefBox(exMList);

                }
                catch (InvalidProtocolBufferException ex)
                {
                    mainLogger.fatal("Error while parsing explo signal. Message: " + ex);
                }

            }

            // MACHINE INFO
            // In production phase every 0.25s
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

                    MACHINE_LOGGER.debug("MACHINE INFOS recived!");
                    for (Machine machine : mList)
                    {
                        MACHINE_LOGGER.debug("Name: " + machine.getName() + "-> Type: M" + machine.getType());
                    }
                }
                catch (InvalidProtocolBufferException ex)
                {
                    mainLogger.fatal("Error while parsing machine info. Message: " + ex);
                }
            }
        }

        @Override
        public void connection_lost(IOException e)
        {
            mainLogger.info("Handler: Connection lost. Message: " + e.getMessage());
        }

        @Override
        public void timeout()
        {
            mainLogger.info("Handler: Timeout");
        }
    }
}
