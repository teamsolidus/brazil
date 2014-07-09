/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Refbox;
// hier ist ein neuer Kommentar
import MainPack.Main;
import Sequence.JobController;
import org.robocup_logistics.llsf_comm.*;
import org.robocup_logistics.llsf_msgs.*;
import org.robocup_logistics.llsf_utils.NanoSecondsTimestampProvider;

/**
 *
 * @author alain.rohr
 */
public class Beacon implements Runnable
{
  ProtobufBroadcastPeer peer;
  private long seq = 1;

  public Beacon(ProtobufBroadcastPeer peer)
  {
    this.peer = peer;
  }

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

      TimeProtos.Time t = TimeProtos.Time.newBuilder().setSec(sec).setNsec(nsec).build();
      BeaconSignalProtos.BeaconSignal bs = BeaconSignalProtos.BeaconSignal.newBuilder().
        setTime(t).
        setSeq(seq++).
        setNumber(Main.jerseyNr).
        setPeerName(Main.name).
        setTeamName("Solidus").
        setTeamColor(JobController.TEAM).
        build();

      ProtobufMessage msg = new ProtobufMessage(2000, 1, bs);
      peer.enqueue(msg); //private
    }
  }
}