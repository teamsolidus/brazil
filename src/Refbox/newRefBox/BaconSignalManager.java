package Refbox.newRefBox;

import MainPack.Main;
import Sequence.JobController;
import org.apache.log4j.Logger;
import org.robocup_logistics.llsf_comm.ProtobufBroadcastPeer;
import org.robocup_logistics.llsf_comm.ProtobufMessage;
import org.robocup_logistics.llsf_msgs.BeaconSignalProtos;
import org.robocup_logistics.llsf_msgs.Pose2DProtos;
import org.robocup_logistics.llsf_msgs.TeamProtos.Team;
import org.robocup_logistics.llsf_msgs.TimeProtos;
import org.robocup_logistics.llsf_utils.NanoSecondsTimestampProvider;

/**
 * Managed the sending of the bacon message and the 2d position
 * @author simon.buehlmann
 */
public class BaconSignalManager implements Runnable
{
    // Logger
    private static Logger log;
    
    private ProtobufBroadcastPeer peerPrivate;
    private final Team TEAM_COLOR;
    private IConfigDataProvider configDataProvider;
    private IRoboPositionDataProvider roboPositionDataProvider;
    
    // Constructor
    public BaconSignalManager(Team TEAM_COLOR, 
            IConfigDataProvider configDataProvider, 
            IRoboPositionDataProvider roboPositionDataProvider, 
            ProtobufBroadcastPeer peerPrivate)
    {
        // Init Logger
        log = Main.log;
        
        this.TEAM_COLOR = TEAM_COLOR;
        this.configDataProvider = configDataProvider;
        this.roboPositionDataProvider = roboPositionDataProvider;
        this.peerPrivate = peerPrivate;
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                Thread.sleep(2000);
            }
            catch (InterruptedException e)
            {
                log.fatal("Error in BaconThread while thread sleep. Message:" + e.getMessage());
            }
            
            // Create Timestamp for Message (for Bacon)
            NanoSecondsTimestampProvider nstp = new NanoSecondsTimestampProvider();

            long currentTime_ms = System.currentTimeMillis();
            long currentTime_ns = nstp.currentNanoSecondsTimestamp();

            int sec = (int) (currentTime_ms / 1000);
            long nsec = currentTime_ns - (currentTime_ms * 1000000L);

            TimeProtos.Time timeMsg = TimeProtos.Time.newBuilder().setSec(sec).setNsec(nsec).build();

            // Creat Position Message (for Bacon)
            int[] positionRobo = this.roboPositionDataProvider.getRoboPosition(this.configDataProvider.getJerseyNr());
            
            Pose2DProtos.Pose2D positionMsg = Pose2DProtos.Pose2D.newBuilder().setX(positionRobo[0]).setY(positionRobo[1]).setOri(positionRobo[2]).setTimestamp(timeMsg).build();

            // Create Bacon Signal
            BeaconSignalProtos.BeaconSignal bs = BeaconSignalProtos.BeaconSignal.newBuilder().
                    setTime(timeMsg).
                    setSeq(1).
                    setNumber(this.configDataProvider.getJerseyNr()).
                    setPeerName(this.configDataProvider.getRoboName()).
                    setTeamName(this.configDataProvider.getRoboName()).
                    setTeamColor(TEAM_COLOR).
                    setPose(positionMsg).
                    build();

            ProtobufMessage msg = new ProtobufMessage(2000, 1, bs);

            peerPrivate.enqueue(msg); // send bacon and pose2d
        }
    }
}
