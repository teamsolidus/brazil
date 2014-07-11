package FieldCommander;

import ComView.ComView;
import GUI.RefboxPanel;
import MainPack.Main;
import MainPack.Start;
import Refbox.ComRefBox;
import Sequence.StateMachine;
import Tools.PingRefbox;
import Traveling.Drive;
import java.awt.AWTException;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D;

/**
 * @author Adrian representing the Contest Field with all Maschines, their
 * Direction, Numeration, Coordinates and Identification
 *
 * 0° (N) | (W) 270° - Station - 90° (E) | 180° (S)
 */
public class FieldCommander extends Frame implements MouseListener
{
  private static FieldCommander instance = null;
  Start startUpView;
  PingRefbox pingRb;
  ComView comView;
  Button startView, restartView, start, avoid, maint, test;
  ComRefBox cRB;
  
  public static final int NORTH = 0;
  public static final int EAST = 90;
  public static final int SOUTH = 180;
  public static final int WEST = 270;

  final int starty = 50;
  final int startx = 563 + 50; // Offset vom rand des Fensters weg in x
  public int[][] roboPos =
  {
    {
      0, 0, 0   //x,y,onPuckStation
    },
    {
      0, 0, 0
    },
    {
      0, 0, 0
    }
  };
  
  public Cell[][] cell = new Cell[20][9];                 //Field Array for Coord-Access
  public Map<String, Cell> machineMap = new HashMap();    //Map for Machine-ID-Access

  int county = 0;
  int countx = 0;
  public boolean avoidTest, maintenance;

  public RefboxPanel refbox;

  /**
   * Singleton Pattern Constructor
   *
   * @return the only instance to FieldCommander
   */
  public static FieldCommander getInstance() throws AWTException
  {
    if (instance == null)
    {
      instance = new FieldCommander();
    }
    return instance;
  }

  private FieldCommander() throws AWTException
  {
    startUpView = new Start();
    comView = ComView.getInstance();

    initFieldModel();
    initFieldGraphic();

    refbox = new RefboxPanel();
    refbox.setBounds(50, 700, 300, 80);
    this.add(refbox);

    start = new Button();
    start.setBackground(Color.PINK);
    start.setLabel("START JAVA");
    start.setSize(150, 50);
    start.setLocation(50, 625);
    this.add(start);
    start.addMouseListener(this);

    startView = new Button();
    startView.setBackground(Color.PINK);
    startView.setLabel("START VIEW");
    startView.setSize(150, 50);
    startView.setLocation(250, 625);
    this.add(startView);
    startView.addMouseListener(this);

    restartView = new Button();
    restartView.setBackground(Color.PINK);
    restartView.setLabel("RESTART VIEW");
    restartView.setSize(150, 50);
    restartView.setLocation(445, 625);
    this.add(restartView);
    restartView.addMouseListener(this);

    avoid = new Button();
    avoid.setBackground(Color.PINK);
    avoid.setLabel("AVOID TEST SIGNAL");
    avoid.setSize(150, 50);
    avoid.setLocation(640, 625);
    this.add(avoid);
    avoid.addMouseListener(this);

    maint = new Button();
    maint.setBackground(Color.PINK);
    maint.setLabel("MAINTENANCE");
    maint.setSize(150, 50);
    maint.setLocation(835, 625);
    this.add(maint);
    maint.addMouseListener(this);

    test = new Button();
    test.setBackground(Color.PINK);
    test.setLabel("TEST");
    test.setSize(150, 50);
    test.setLocation(1025, 625);
    this.add(test);
    test.addMouseListener(this);
  }

  public void initFieldGraphic()
  {
    this.setLayout(null);
    this.setSize(1220, 800);
    this.addWindowListener(new WindowAdapter()
    {
      @Override
      public void windowClosing(WindowEvent e)
      {
        System.exit(0);
      }
    });
  }

  public void setRoboPos(int nr, int x, int y, int puck)
  {
    roboPos[nr - 1][0] = x;
    roboPos[nr - 1][1] = y;
    roboPos[nr - 1][2] = puck;
  }

  public int[] getRoboPos(int nr)
  {
    return roboPos[nr - 1];
  }

  public void initFieldModel()
  {
    //<editor-fold defaultstate="collapsed" desc="free cell definitions">
    for (int y = starty + 31; y < 563; y = y + 56) // 50 von oben weg und dann in 56er schritten weiter
    {
      for (int x = 50 + 34; x < 563 + startx - 50; x = x + 56) // 50 von der Seite weg und dann in 56er schritten
      {
        cell[countx][county] = new Cell(countx, county);
        cell[countx][county].setLayout(null);
        // cell[countx][county].setX(x);
        // cell[countx][county].setY(y);
        cell[countx][county].setRealX(-(x + 25 - startx) * 10); // effektiver x wert setzten
        cell[countx][county].setRealY((y + 25 - starty) * 10); // effektiver y wert setzen
        cell[countx][county].setLocation(x, y); // hier gibst du lediglich den Offset von der Seite aus an
        cell[countx][county].setSize(50, 50); // hier die grösse des Panels ( da eine Zelle 0,5 m gross ist 50
        cell[countx][county].setId("");
        this.add(cell[countx][county]);
        countx++;
      }
      countx = 0;
      county++;

    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Machine Definitions">
    setCell(10, 2, "M13", false, NORTH);
    machineMap.put("M13", cell[10][2]);

    setCell(10, 4, "M14", false, SOUTH);
    machineMap.put("M14", cell[10][4]);

    setCell(10, 8, "R2", false, NORTH);
    machineMap.put("R2", cell[10][8]);

    setCell(12, 2, "M15", false, EAST);
    machineMap.put("M15", cell[12][2]);

    setCell(12, 4, "M16", false, WEST);
    machineMap.put("M16", cell[12][4]);

    setCell(12, 6, "M17", false, WEST);
    machineMap.put("M17", cell[12][6]);

    setCell(14, 2, "M18", false, SOUTH);
    machineMap.put("M18", cell[14][2]);

    setCell(14, 6, "M19", false, NORTH);
    machineMap.put("M19", cell[14][6]);

    setCell(14, 8, "M20", false, WEST);
    machineMap.put("M20", cell[14][8]);

    setCell(16, 2, "M21", false, EAST);
    machineMap.put("M21", cell[16][2]);

    setCell(16, 4, "M22", false, SOUTH);
    machineMap.put("M22", cell[16][4]);

    setCell(16, 8, "M23", false, NORTH);
    machineMap.put("M23", cell[16][8]);

    setCell(18, 8, "M24", false, NORTH);
    machineMap.put("M24", cell[18][8]);

    setCell(8, 2, "M1", false, NORTH);
    machineMap.put("M1", cell[8][2]);

    setCell(8, 4, "M2", false, SOUTH);
    machineMap.put("M2", cell[8][4]);

    setCell(8, 8, "R1", false, NORTH);
    machineMap.put("R1", cell[8][8]);

    setCell(6, 2, "M3", false, WEST);
    machineMap.put("M3", cell[6][2]);

    setCell(6, 4, "M4", false, EAST);
    machineMap.put("M4", cell[6][4]);

    setCell(6, 6, "M5", false, EAST);
    machineMap.put("M5", cell[6][6]);

    setCell(4, 2, "M6", false, SOUTH);
    machineMap.put("M6", cell[4][2]);

    setCell(4, 6, "M7", false, NORTH);
    machineMap.put("M7", cell[4][6]);

    setCell(4, 8, "M8", false, EAST);
    machineMap.put("M8", cell[4][8]);

    setCell(0, 8, "M12", false, NORTH);
    machineMap.put("M12", cell[0][8]);

    setCell(2, 2, "M9", false, WEST);
    machineMap.put("M9", cell[2][2]);

    setCell(2, 4, "M10", false, SOUTH);
    machineMap.put("M10", cell[2][4]);

    setCell(2, 8, "M11", false, NORTH);
    machineMap.put("M11", cell[2][8]);

    setCell(6, 0, "P1", true, NORTH);
    machineMap.put("P1", cell[6][0]);

    setCell(12, 0, "P2", true, NORTH);// Puck cells
    machineMap.put("P2", cell[12][0]);

    setCell(0, 4, "D1", true, WEST);
    machineMap.put("D1", cell[0][4]);

    setCell(18, 4, "D2", true, EAST);
    machineMap.put("D2", cell[18][4]);

//</editor-fold>
  }

  public void setCell(int x, int y, String id, boolean free, int dir)
  {
    cell[x][y].setId(id);
    cell[x][y].setFree(free);
    cell[x][y].setDirection(dir);
  }
  
  public void setComRefBox(ComRefBox cRB)
  {
    this.cRB=cRB;
  }

  public void paint(Graphics g)
  {
    g.setColor(Color.red);
    // Field Frame
    g.drawRect(startx, starty, 563, 563);
    g.drawRect(50, 50, 563, 563);

    // Pucks symbolisch
    g.fillOval(395 + 50 - 10, 50 + 10, 20, 20);
    g.fillOval(50 + 731 - 10, 50 + 10, 20, 20);

    //<editor-fold defaultstate="collapsed" desc="Delivery Gates">
    g.setColor(Color.black);
    g.drawLine(50 + 20, 50 + 280, 50 + 35, 50 + 280);
    g.drawLine(50 + 20, 50 + 270, 50 + 20, 50 + 290);
    g.drawString("D1", 50 + 2, 50 + 250);

    g.drawLine(50 + 20, 50 + 245, 50 + 35, 50 + 245);
    g.drawLine(50 + 20, 50 + 235, 50 + 20, 50 + 255);
    g.drawString("D2", 50 + 2, 50 + 285);

    g.drawLine(50 + 20, 50 + 315, 50 + 35, 50 + 315);
    g.drawLine(50 + 20, 50 + 305, 50 + 20, 50 + 325);
    g.drawString("D3", 50 + 2, 50 + 320);

    g.drawLine(50 + 1125 - 20, 50 + 280, 50 + 1125 - 35, 50 + 280);
    g.drawLine(50 + 1125 - 20, 50 + 270, 50 + 1125 - 20, 50 + 290);
    g.drawString("D4", 50 + 1108, 50 + 250);

    g.drawLine(50 + 1125 - 20, 50 + 245, 50 + 1125 - 35, 50 + 245);
    g.drawLine(50 + 1125 - 20, 50 + 235, 50 + 1125 - 20, 50 + 255);
    g.drawString("D5", 50 + 1108, 50 + 285);

    g.drawLine(50 + 1125 - 20, 50 + 315, 50 + 1125 - 35, 50 + 315);
    g.drawLine(50 + 1125 - 20, 50 + 305, 50 + 1125 - 20, 50 + 325);
    g.drawString("D6", 50 + 1108, 50 + 320);
//</editor-fold>
  }

  public static void main(String[] args) throws AWTException
  {
    FieldCommander f = new FieldCommander();

    f.setVisible(true);

    //  System.out.println(f.machineMap.get("D1").getX());
    // System.out.println(f.machineMap.get("D1").getY());
// hier kann die Mittelpunktkoordianten des Panels abgefragt werden ( evtl brauchen wir noch eine Variable mehr mit dem Effektivwert )
    System.out.println(f.cell[17][1].getRealX());
    System.out.println(f.cell[17][1].getRealY());
    // System.out.println(f.cell[0][0].getRealY());
    //System.out.println(f.cell[0][0].getRealX());

    f.cell[1][1].setRoute(true);
    f.repaint();
    f.cell[1][1].repaint();
  }

  @Override
  public void mouseClicked(MouseEvent e)
  {
    if (e.getSource() == startView)
    {
      try
      {
        startUpView.Startup();
      } catch (InterruptedException | IOException ex)
      {
        Logger.getLogger(FieldCommander.class.getName()).log(Level.SEVERE, null, ex);
      }
    } else if (e.getSource() == restartView)
    {
      try
      {
        startUpView.Restart();
      } catch (InterruptedException | IOException ex)
      {
        Logger.getLogger(FieldCommander.class.getName()).log(Level.SEVERE, null, ex);
      }
    } else if (e.getSource() == start)
    {
      try
      {
        Main.startServer();
      } catch (InterruptedException | IOException | AWTException ex)
      {
      }
    } else if (e.getSource() == avoid)
    {
      for (int i = 100; i >= 0; i = i - 10)
      {
        comView.breakingFactor = i;
        try
        {
          Thread.sleep(100);
        } catch (InterruptedException ex)
        {
          Logger.getLogger(FieldCommander.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      // abbremsen und danach das ausweichen auslösen
      avoidTest = true;
    } else if (e.getSource() == maint)
    {
      maintenance = true;
    } else if (e.getSource() == test)
    {
      //this.setRoboPos(Main.getJerseyNr(), 25, 25, 1);  //Test Position fow Own Robot
      int[] lamp={1,1,1};
      cRB.sendMachine("M1", lamp);
    }
  }

  @Override
  public void mousePressed(MouseEvent e)
  {
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
  }

  @Override
  public void mouseEntered(MouseEvent e)
  {
  }

  @Override
  public void mouseExited(MouseEvent e)
  {
  }
}