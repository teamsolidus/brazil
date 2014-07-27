package Refbox.newRefBox;

import MainPack.Main;
import static Refbox.ComRefBox.game;
import static Refbox.ComRefBox.gamePhase;
import static Refbox.ComRefBox.gameState;
import static Refbox.ComRefBox.orderList;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import org.apache.log4j.Logger;
import org.robocup_logistics.llsf_comm.ProtobufBroadcastPeer;
import org.robocup_logistics.llsf_comm.ProtobufMessageHandler;
import org.robocup_logistics.llsf_msgs.BeaconSignalProtos;
import org.robocup_logistics.llsf_msgs.ExplorationInfoProtos;
import org.robocup_logistics.llsf_msgs.GameStateProtos;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos;
import org.robocup_logistics.llsf_msgs.MachineReportProtos;
import org.robocup_logistics.llsf_msgs.OrderInfoProtos;
import org.robocup_logistics.llsf_msgs.TeamProtos;
import org.robocup_logistics.llsf_msgs.TimeProtos;

/**
 *
 * @author simon.buehlmann
 */
public class Handler implements ProtobufMessageHandler
{
    // Logger
    private static Logger log;
    
    private IConfigDataProvider configDataProvider;
    private IRoboPositionDataProvider roboPositionDataProvider;
    
    private boolean cryptoInitFlag;
    private TeamProtos.Team TEAM_COLOR_FROM_REFBOX;
    
    @Override
    public void handle_message(ByteBuffer in_msg, GeneratedMessage msg)
    {
        /*
        // BACON SIGNAL
        if (msg instanceof BeaconSignalProtos.BeaconSignal)
        {
            byte[] array = new byte[in_msg.capacity()];
            in_msg.rewind();
            in_msg.get(array);
            BeaconSignalProtos.BeaconSignal baconSignal;
            TimeProtos.Time t;

            try
            {
                baconSignal = BeaconSignalProtos.BeaconSignal.parseFrom(array);
                t = baconSignal.getTime();

                log.debug("Robo detected! Nr: " + baconSignal.getNumber()
                        + " Team: " + baconSignal.getTeamName()
                        + " Peer: " + baconSignal.getPeerName()
                        + " Live-Time: " + t.getSec()
                        + "s. PosX: " + baconSignal.getPose().getX()
                        + " PosY: " + baconSignal.getPose().getY()
                        + " PosAngle:" + baconSignal.getPose().getOri());

                if (baconSignal.getNumber() != this.configDataProvider.getJerseyNr() && baconSignal.getNumber() > 0)
                {
                    // Not this Robo, so inform the fieldcommander
                    this.roboPositionDataProvider.setPositionOtherRobo(baconSignal.getNumber(), 
                            (int) baconSignal.getPose().getX(), 
                            (int) baconSignal.getPose().getY(), 
                            (int) baconSignal.getPose().getOri());
                }
            }
            catch (InvalidProtocolBufferException e)
            {
                log.fatal("Fault while parsing bacon signal. Message: " + e.getMessage());
            }
        }

        // ORDER INFO
        else if (msg instanceof OrderInfoProtos.OrderInfo)
        {
            byte[] array = new byte[in_msg.capacity()];
            in_msg.rewind();
            in_msg.get(array);
            OrderInfoProtos.OrderInfo orderInfo;

            try
            {
                orderInfo = OrderInfoProtos.OrderInfo.parseFrom(array);
                log.debug("Order info received!");
                orderList = orderInfo.getOrdersList();
                int i = 0;
                for (OrderInfoProtos.Order o : orderList)
                {
                    Main.log.debug("Order " + (++i) + ":" + o);
                }

            }
            catch (InvalidProtocolBufferException ex)
            {
                log.error("Error while parsing order info. Message: " + ex);
            }
        }

        // GAME STATE
        // Gibt die Aktuelle Spielphase, Spielstatus, Spielzeit, die Punkte und ob noch Zeit vorhanden ist.
        else if (msg instanceof GameStateProtos.GameState)
        {
            byte[] array = new byte[in_msg.capacity()];
            in_msg.rewind();
            in_msg.get(array);

            try
            {
                game = GameStateProtos.GameState.parseFrom(array);

                gamePhase = game.getPhase().name();
                gameState = game.getState().name();

                log.debug("Gamestate Name : " + gamePhase + " Gamestate State: " + gameState);

                // Waiting for the first game state message, in which are the colors / temnames defined
                if (!this.cryptoInitFlag)// first time flag
                {
                    this.cryptoInitFlag = true;

                    if (this.configDataProvider.getTeamName().equals(game.getTeamCyan()))
                    {
                        TEAM_COLOR_FROM_REFBOX = TeamProtos.Team.CYAN;
                    }
                    else if (this.configDataProvider.getTeamName().equals(game.getTeamMagenta()))
                    {
                        TEAM_COLOR_FROM_REFBOX = TeamProtos.Team.MAGENTA;
                    }
                    else
                    {
                        log.debug("No equal teamname in gamestate message. Name in cyan message:" + game.getTeamCyan() + "Name in mangenta message: " + game.getTeamMagenta());
                        this.cryptoInitFlag = false; // reset flag
                        return;
                    }

                    switch (TEAM_COLOR_FROM_REFBOX)
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
                            log.fatal("Exception while start private peer. Message: " + e.getMessage());
                        }

                        // Add Handled Messages
                        peerPrivate.<BeaconSignalProtos.BeaconSignal>add_message(BeaconSignalProtos.BeaconSignal.class);
                        peerPrivate.<OrderInfoProtos.OrderInfo>add_message(OrderInfoProtos.OrderInfo.class);
                        peerPrivate.<MachineInfoProtos.MachineInfo>add_message(MachineInfoProtos.MachineInfo.class);
                        peerPrivate.<MachineReportProtos.MachineReport>add_message(MachineReportProtos.MachineReport.class); //only send!

                        Handler handler = new Handler();
                        peerPrivate.register_handler(handler);
                    }
                }
            }
            catch (InvalidProtocolBufferException e)
            {
                log.fatal("Error while parsing game state. Message: " + e.getMessage());
            }
        }

        // EXPLORATION INFO
        // In explophase, every second
        else if (msg instanceof ExplorationInfoProtos.ExplorationInfo)
        {
            byte[] array = new byte[in_msg.capacity()];
            in_msg.rewind();
            in_msg.get(array);
            ExplorationInfoProtos.ExplorationInfo info;

            log.debug("Exploration info received:");

            try
            {
                info = ExplorationInfoProtos.ExplorationInfo.parseFrom(array);

                List<ExplorationInfoProtos.ExplorationSignal> sList = info.getSignalsList();
                for (int i = 0; i < 5; i++)
                {
                    ExplorationInfoProtos.ExplorationSignal m0 = sList.get(i);
                    mTypeDef[i][RED] = m0.getLights(0).getState().getNumber();
                    mTypeDef[i][ORANGE] = m0.getLights(1).getState().getNumber();
                    mTypeDef[i][GREEN] = m0.getLights(2).getState().getNumber();
                }

                // Array[15] füllen: Station 1 - 3 platz 0 - 2...
                mTypLight = new int[15];
                int m = 0;
                for (int i = 0; i < 15; i += 3)
                {
                    mTypLight[i] = mTypeDef[m][RED];
                    mTypLight[i + 1] = mTypeDef[m][ORANGE];
                    mTypLight[i + 2] = mTypeDef[m][GREEN];

                    int logMessage = "MTyp " + m + " => RED: " + mTypLight[i] + " ORANGE: " + mTypLight[i + 1] + " GREEN: " + mTypLight[i + 2];
                    log.info(logMessage);

                    m++;
                }
                exMList = info.getMachinesList();
                jc.setExploMachinesFromRefBox(exMList);

            }
            catch (InvalidProtocolBufferException ex)
            {
                log.fatal("Error while parsing explo signal. Message: " + ex);
            }

        }

            // MACHINE INFO
        // In production phase every 0.25s
        else if (msg instanceof MachineInfoProtos.MachineInfo)
        {
            byte[] array = new byte[in_msg.capacity()];
            in_msg.rewind();
            in_msg.get(array);
            try
            {
                MachineInfoProtos.MachineInfo mInfo;
                mInfo = MachineInfoProtos.MachineInfo.parseFrom(array);
                mList = mInfo.getMachinesList();
                jc.setMachineTypesFromRefBox(mList);
            }
            catch (InvalidProtocolBufferException ex)
            {
                log.error("Error while parsing machine info. Message: " + ex);
            }
        
        }
*/
    }

    @Override
    public void connection_lost(IOException e)
    {
        log.info("Handler: Connection lost");
    }

    @Override
    public void timeout()
    {
        log.info("Handler: Timeout");
    }
}
