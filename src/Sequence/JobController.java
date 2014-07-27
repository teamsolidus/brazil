package Sequence;

import FieldCommander.Cell;
import FieldCommander.FieldCommander;
import Refbox.ComRefBox;
import java.awt.AWTException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.robocup_logistics.llsf_msgs.ExplorationInfoProtos.ExplorationMachine;
import org.robocup_logistics.llsf_msgs.MachineInfoProtos.Machine;
import org.robocup_logistics.llsf_msgs.OrderInfoProtos.Order;
import org.robocup_logistics.llsf_msgs.OrderInfoProtos.Order.DeliveryGate;
import org.robocup_logistics.llsf_msgs.TeamProtos;
import org.robocup_logistics.llsf_msgs.TeamProtos.Team;

/**
 * @author alain contains the optimized ways for each Robot in the Exploration
 * Phase also some tools and methods for way finding
 */
public class JobController implements Serializable
{

  private static JobController instance = null;
//------> vorläufig manuell anpassen     
  public static final Team TEAM = Team.CYAN;
  private final int FIELDHALF = SOUTH;   // CYAN is SOUTH  // MAGENTA is NORTH
  private final int REALISTICTIME = 30;  //realistic Time 2 reach Delivery Gate with product
//------> vor jeder Wettbewerbsrunde    

  FieldCommander fc;
  ComRefBox comRefBox;

//Index Constants for Way-Arrays
  public static final int NORTH = 0, SOUTH = 1;
  public static final int PINK = 0, BROWN = 1, BLOND = 2;

  private int roboNameIdx;
  private String prodJob;
  public int jobCounter = 0;

    //Order to check machines ev. the way could be optimized 
  //[round][field][robo][job]
  public String[][] exploMachines =
  {
    //Pink
    {
      null, null, null, null
    },
    //Brown
    {
      null, null, null, null
    },
    //Blond
    {
      null, null, null, null
    }
  };

  //       [Field Half][Robo][coord]  
  public static int[][][] startCell =
  {
    //----------Field Side MAGENTA / NORTH----------------------------------
    {
      //Pink
      {
        14, 1
      },
      //Brown
      {
        16, 1
      },
      //Blond
      {
        17, 1
      }
    },
    //----------Field Side CYAN / SOUTH-------------------------------------
    {
      //Pink
      {
        4, 1
      },
      //Brown
      {
        2, 1
      },
      //Blond
      {
        1, 3
      }
    }
  };

    // Startkoordinaten der einzelnen Robos  
  // [Field Half][Robo][coord]  
  public static int[][][] startKoords =
  {
    {
      //Pink
      {
        -2800, 200  // has startCell 14/0
      },
      //Brown
      {
        -3920, 200  // has startCell 16/0
      },
      //Blond
      {
        -4480, 200 // has startCell 18/0
      },
    },
    {
      //Pink
      {
        2800, 200  // has startCell 14/0
      },
      //Brown
      {
        3920, 200  // has startCell 16/0
      },
      //Blond
      {
        4480, 200 // has startCell 18/0
      },
    }

  };

  public static int[][] deliveryGate =
  {
    {
      18, 4
    },
    {
      0, 4
    }
  };

  public Cell getDeliveryCell()
  {
    int x = deliveryGate[FIELDHALF][0];
    int y = deliveryGate[FIELDHALF][1];
    return fc.cell[x][y];
  }

  public static int[][] puckArea =
  {
    {
      12, 0
    },
    {
      6, 0
    }
  };

  public Cell getPuckCell()
  {
    int x = puckArea[FIELDHALF][0];
    int y = puckArea[FIELDHALF][1];
    return fc.cell[x][y];
  }

  
   // [Field Half][Robo][coord]  
  public static String [][][] waitCell =
  {
    {
      //Pink
      {
        "W3" // has startCell 14/0
      },
      //Brown
      {
        "W4"  // has startCell 16/0
      },
      //Blond
      {
        "W5" // has startCell 18/0
      },
    },
    {
      //Pink
      {
        "W1"  // has startCell 14/0
      },
      //Brown
      {
        "W2"// has startCell 16/0
      },
      //Blond
      {
       "W3" // has startCell 18/0
      },
    }

  };

 





  public String getWaitCell()
  {
    String cell = waitCell[FIELDHALF][roboNameIdx][0];
  
    return cell;
  }

  public int getStartKoordX()
  {
    return startKoords[FIELDHALF][roboNameIdx][0];
  }

  public int getStartKoordY()
  {
    return startKoords[FIELDHALF][roboNameIdx][1];
  }

  public static JobController getInstance() throws AWTException
  {
    if (instance == null)
    {
      instance = new JobController();
    }
    return instance;
  }

  public JobController() throws AWTException
  {
    setRoboNameIdx("Solid1");  //Default name later overwritten by Config-File
    fc = FieldCommander.getInstance();
  }

  public void setNextExploJob() throws Exception
  {
    jobCounter++;
    if (exploMachines[roboNameIdx][jobCounter] == null)
    {
      throw new Exception("no more Jobs for this robo");
    }
  }

  public String getExploJob()
  {
    return exploMachines[roboNameIdx][jobCounter];
  }

  /**
   * store received MachineList in ExploPhase into our own List
   *
   * @param mList (List of Machines; unsorted)
   */
  public void setExploMachinesFromRefBox(List<ExplorationMachine> mList)
  {
    List<ExplorationMachine> tempList = new ArrayList<>();

    //Write my machines in seperate temporary list
    for (ExplorationMachine machine : mList)
    {
      if (machine.getTeamColor() == this.TEAM)
      {
        tempList.add(machine);
      }
    }

    //Sort the temporary list by machine nr in ascending order
    Collections.sort(tempList, new ExploMachinesComparator(this.TEAM));

    //Fill the Array
    int tempIndex = 0;

    if (TEAM == TeamProtos.Team.MAGENTA)
    {
      for (int robo = 0; robo <= 2; robo++) //3 Robos
      {
        for (int job = 3; job >= 0; job--) //4 Jobs
        {
          exploMachines[robo][job] = tempList.get(tempIndex++).getName();
        }
      }
    } else // bei CYAN 
    {
      for (int robo = 0; robo <= 2; robo++) //3 Robos                        
      {
        for (int job = 3; job >= 0; job--) //4 Jobs
        {
          int last = tempList.size() - 1;
          ExplorationMachine mach = tempList.get(last);
          String mName = mach.getName();
          String nrStr = mName.substring(1);
          int mNr = Integer.parseInt(nrStr);

          if (mNr > 12) //im gegnerischen Feld?
          {
            exploMachines[robo][job] = tempList.remove(last).getName();  //grösste Maschine im Gegnerfeld
          } else
          {
            exploMachines[robo][job] = tempList.remove(0).getName();  //vorderste Maschinen einfüllen (gegen Mittellinie)
          }
        }
      }
    }
    //Problemzellen (wegen View) umstellen/ Workaround wegen Fehler in Driveklasse
    for (int job = 0; job <= 3; job++)
    {
      if (exploMachines[BLOND][job].equals("M12") || exploMachines[BLOND][job].equals("M24"))
      {   // nur bei Problemzellen M12 oder M24
        if (job == 0) //wenn zu Beginn, dann nach hinten legen
        {
          String tmp;        // 0 und 3 tauschen
          tmp = exploMachines[BLOND][3];
          exploMachines[BLOND][3] = exploMachines[BLOND][0];
          exploMachines[BLOND][0] = tmp;
        }
      }
    }
  }

  /**
   * store received MachineList in ProdPhase into our own machineMap
   *
   * @param mList (List of Machines; unsorted)
   */
  public void setMachineTypesFromRefBox(List<Machine> mList)
  {
    for (Machine m : mList)    //iterate through machines
    {
      if (m.getTeamColor() == JobController.TEAM)
      {
        String name = m.getName();
        if (name.charAt(0) == 'M')
        {
          Cell cell = fc.machineMap.get(m.getName());      //get Cell of act. machine
          if (cell != null)
          {
            int mNr = cell.getMachineNr();
            cell.setmTyp(m.getType());
            cell.setTeam(JobController.TEAM);
            fc.machineMap.put(m.getName(), cell);
          }
        }
      }
    }
  }

  /**
   * get the Machine for searched type
   *
   * @param mType in "T1" to "T5"
   * @return corresponding machine "M1" to "M24"
   * @author roa
   */
  public String getProdMachine(String mType, int nr)
  {
    int cnt = 0;
    for (String s : fc.machineMap.keySet()) //iterate through machines
    {
      Cell cell = fc.machineMap.get(s);   //get Cell of act. machine      
      if (cell.getTeam() == JobController.TEAM)//one of our Cell?
      {
        String typ = cell.getmTyp();

        if ((typ != null) && (typ.equals(mType))) //compare searched Type
        {
          cnt++;
          if (nr == cnt)
          {
            return s;                       //return corr. machine   
          }

        }

      }
    }
    return null;                          //nothing found/error
  }

  public String getProdJob()
  {
    return prodJob;
  }

  /**
   * Decide which Job/Machine is perfect for the actual Robo //has to be discuss
   * with the strategy of Vincent
   *
   * @param product
   * @return Machine Name
   */
  /*    public void findActProdJobMachine()
   {
   switch (roboNameIdx)
   {
   case 0:
   //Pink producing P1 on T3
   //first T1 and first T2 for intermediate Products (S1/S2)                
   for (int i = 0; i <= 24; i++)
   {
   Cell c;
   c = fc.machineMap.get("M" + i);
   //search next T1
   }
   break;
   case 1:
   //Brown producing P2 on T4
   //Second T1 and second T2 for his intermediate Products
   break;
   case 2:
   //Blond producing P3 on T5
   //No intermediate product
   break;
   default:
   }

   for (int i = 0; i <= 10; i++)
   {
   Order order = hnd.orderList.get(i);

   switch (order.getProduct())
   {
   case P1:
   break;
   case P2:
   break;
   case P3:
   break;
   }
   }
   //finding matching machine for Typ/Robo corresponding to actual Order from refbox
   //just a demo because of missing Algorithm//algorithm to catch next 
   //handler.orderList.get();
   prodJob = "P3";
   prodMachine = "M9";
   }*/
  /**
   * returns the delivery Gate, if is open to deliver the requested Product
   *
   * @param product
   * @return DeliveryGate
   * @author roa
   *
   */
  public DeliveryGate isProductDelGateOpen(String product)
  {
    for (Order order : comRefBox.orderList)
    {//keine spezifisches TeamMeldung oder betrifft eigenes Team
      if (!order.hasTeamColor() || order.getTeamColor() == JobController.TEAM)
      {   //Matches this product ?            
        if (order.getProduct().name().equals(product))
        {   //more Products needed ? 
          if (order.getQuantityRequested() > 0)
          {   //is the delivery gate already open
            if (order.getDeliveryPeriodBegin() <= comRefBox.game.getGameTime().getSec())
            {   //is the delivery gate still open in realistic time
              if (order.getDeliveryPeriodEnd() >= comRefBox.game.getGameTime().getSec() + REALISTICTIME)
              {
                return order.getDeliveryGate();
              }
            }
          }
        }
      }
    }
    return null;
  }

  public Cell getStartCell()
  {
    int x = startCell[FIELDHALF][roboNameIdx][0];
    int y = startCell[FIELDHALF][roboNameIdx][1];
    return fc.cell[x][y];
  }

  public void setRoboNameIdx(String roboName)
  {
    switch (roboName)
    {
      case "Solid1":
        roboNameIdx = 0;
        break;
      case "Solid2":
        roboNameIdx = 1;
        break;
      case "Solid3":
        roboNameIdx = 2;
        break;
      default:
        roboNameIdx = -1;
    }
  }

  /**
   * Found the nearest opimal cell to the machine (LoadCell)
   *
   * @param machine the optimal cell and phi beside the machine
   * @return
   */
  public Cell getLoadCellNearMachine(String machine)
  {
    int x, y;
    String loadDirection;
    Cell machineCell, loadCell;

    machineCell = fc.machineMap.get(machine);
    x = machineCell.getX();
    y = machineCell.getY();
    switch (machineCell.getDirLetter())
    {
      case "W":
        x--;
        loadDirection = "E";
        break;
      case "N":
        y--;
        loadDirection = "S";
        break;
      case "E":
        x++;
        loadDirection = "W";
        break;
      case "S":
        y++;
        loadDirection = "N";
        break;
      default:
        loadDirection = "S";
    }
    Cell.getPhiFromDirLetter(loadDirection);
    loadCell = fc.cell[x][y];
    loadCell.setDirection(Cell.getPhiFromDirLetter(loadDirection));
    return loadCell;
  }

  public int getDirectionOfMachine(String machine)
  {
    int x, y;

    Cell machineCell, loadCell;

    machineCell = fc.machineMap.get(machine);
    x = machineCell.getX();
    y = machineCell.getY();

    loadCell = fc.cell[x][y];

    return loadCell.getDirection();

  }

  public int getRoboNameIdx()
  {
    return roboNameIdx + 1;
  }

  public void setRoboNameIdx(int roboNameIdx)
  {
    this.roboNameIdx = roboNameIdx;
  }

  public static void main(String[] args)
  {
    try
    {
      JobController way = new JobController();
      way.setRoboNameIdx("Solid1");
      System.out.println("Start" + way.getStartCell());
      System.out.println(way.getProdMachine("T5", 1));
      while (true)
      {
        System.out.println(way.getExploJob());
        way.setNextExploJob();
      }

    } catch (Exception ex)
    {
      System.out.println(ex.getMessage());
    }
  }

  public void registerComRefBox(ComRefBox com)
  {
    this.comRefBox = com;
  }
}
