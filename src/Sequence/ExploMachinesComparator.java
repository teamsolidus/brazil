package Sequence;

import FieldCommander.Cell;
import java.util.Comparator;
import org.robocup_logistics.llsf_msgs.ExplorationInfoProtos;
import org.robocup_logistics.llsf_msgs.TeamProtos;

/**
 *
 * @author simon.buehlmann
 */
public class ExploMachinesComparator implements Comparator<ExplorationInfoProtos.ExplorationMachine>
{
    private TeamProtos.Team team;

    public ExploMachinesComparator(TeamProtos.Team team)
    {
        this.team = team;
    }

    @Override
    public int compare(ExplorationInfoProtos.ExplorationMachine o1, ExplorationInfoProtos.ExplorationMachine o2)
    {
        //Check: Has the first machine the smaller number as the second?
        if (Cell.parseMachineNameToNr(o1.getName()) < Cell.parseMachineNameToNr(o2.getName()))
        {
            return -1;   //First machine is superordinate
        }
        return 1;  //First machine is subordinate
    }
}
