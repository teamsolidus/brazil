
package Refbox.newRefBox;

import java.util.List;
import org.robocup_logistics.llsf_msgs.ExplorationInfoProtos;
import org.robocup_logistics.llsf_msgs.GameStateProtos;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos;
import org.robocup_logistics.llsf_msgs.OrderInfoProtos;

/**
 *
 * @author simon.buehlmann
 */
public class ComDataBuffer
{
    static String[] send = new String[6];
    public static List<OrderInfoProtos.Order> orderList;
    public static List<ExplorationInfoProtos.ExplorationMachine> exMList;
    public static List<MachineInfoProtos.Machine> mList;
    // -------------------------- Game State ---------------------------------------
    public static GameStateProtos.GameState game;              // Game Status
    public static String gamePhase;            // Aktuelle Game Phase (PRE_GAME, EXPLORATION, PRODUCTION, POST_GAME)
    public static String gameState;            // Aktueller Game Status (WAIT_START, RUNNING, PAUSED)

    public static int[] mTypLight;             // Lampen Informationen für Maschinentypen
}
