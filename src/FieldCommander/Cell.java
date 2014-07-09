package FieldCommander;

import static FieldCommander.FieldCommander.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Panel;
import org.robocup_logistics.llsf_msgs.TeamProtos;

/**
 * @author Adrian
 *
 * representing one Cell with their characteristic attributes combined model and
 * graphic
 */
public class Cell extends Panel
{
    private boolean free = true;
    private boolean route = false;

    private int direction = 800;
    private int x = 0;
    private int y = 0;
    private int realY, realX; // die effektiven werte der Koordinaten ( - der Rand und - die hälfte des panels)

    private String id;
    private String mTyp = null;  // T1 to T5
    private TeamProtos.Team team;         // 

    public Cell(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public boolean isFree()
    {
        return free;
    }

    public void setFree(boolean free)
    {
        this.free = free;
    }

    public int getDirection()
    {
        return direction;
    }

    public int getRealY()
    {
        return realY;
    }

    public void setRealY(int realY)
    {
        this.realY = realY;
    }

    public int getRealX()
    {
        return realX;
    }

    public void setRealX(int realX)
    {
        this.realX = realX;
    }

    public void setDirection(int direction)
    {
        this.direction = direction;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int xval)
    {
        this.x = xval;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int yval)
    {
        this.y = yval;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public int getMachineNr()
    {
        return Integer.parseInt(id.substring(1));  //Cut the first letter
    }

    public String getmTyp()
    {
        return mTyp;
    }

    public void setmTyp(String mTyp)
    {
        this.mTyp = mTyp;
    }

    public TeamProtos.Team getTeam()
    {
        return team;
    }

    public void setTeam(TeamProtos.Team team)
    {
        this.team = team;
    }

    public boolean isRoute()
    {
        return route;
    }

    public void setRoute(boolean route)
    {
        this.route = route;
    }
    
    

    @Override
    public void paint(Graphics g)
    {
        
          if (route== true)
        {
            g.setColor(Color.PINK);
        }
        if (free == true && route==false)
        {
            g.setColor(Color.LIGHT_GRAY);
            
        } 
        
        
        if (!free && route == false)
        {
            g.setColor(Color.red);
           

        }
        g.fillRect(0, 0, 50, 50);
     
         if (!free)
         {
              g.setColor(Color.black);
                g.drawString(getDirLetter(), 35, 48);
         }
       

        if (free == false && direction == NORTH)
        {
            g.setColor(Color.BLACK);
            g.drawLine(15, 25, 35, 25); // start x,y ende x,y
            g.drawLine(25, 15, 25, 25);
        }
        if (free == false && direction == SOUTH)
        {
            g.setColor(Color.BLACK);
            g.drawLine(15, 25, 35, 25); // ---
            g.drawLine(25, 25, 25, 35);   // |
        }

        if (free == false && direction == EAST)
        {
            g.setColor(Color.BLACK);
            g.drawLine(25, 25, 35, 25); // ---
            g.drawLine(25, 15, 25, 35);   // |  
        }

        if (free == false && direction == WEST)
        {
            g.setColor(Color.BLACK);
            g.drawLine(15, 25, 25, 25); // ---
            g.drawLine(25, 15, 25, 35);   // |  
        }
    
       g.setColor(Color.black);
        g.drawString(x + "/" + this.y, 5, 13);
        g.drawString(this.id, 5, 48);
    }

    public String getDirLetter()
    {
        return getDirLetterFromPhi(direction);
    }

    public static String getDirLetterFromPhi(int direction)
    {
        switch (direction)
        {
            case 0:
                return "N";
            case 90:
                return "E";
            case 180:
                return "S";
            case 270:
                return "W";
            default:
                return null;
        }
    }

    public static int getPhiFromDirLetter(String letter)
    {
        switch (letter)
        {
            case "N":
                return 0;
            case "E":
                return 90;
            case "S":
                return 180;
            case "W":
                return 270;
            default:
                return -1;
        }
    }
    
    /**
     * Pars the machine name in the number, which is contained in the name
     * @param name Machine name (for example: M13)
     * @return Machine number (for example: 13)
     */
    public static int parseMachineNameToNr(String name)
    {
        return Integer.parseInt(name.substring(1));
    }
}
