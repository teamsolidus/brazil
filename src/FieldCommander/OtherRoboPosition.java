
package FieldCommander;

/**
 * Includes datas about the other team Robos (infos recived over bacon signal)
 * @author simon.buehlmann
 */
public class OtherRoboPosition
{
    private boolean isCatchingPuck;
    private int posX, posY, posOrientation;
    
    public OtherRoboPosition(boolean catchingPuck, int posX, int posY, int posOrientation)
    {
        this.posX = posX;
        this.posY = posY;
        this.posOrientation = posOrientation;
        this.isCatchingPuck = catchingPuck;
    }
    
    public OtherRoboPosition(int posX, int posY, int orientationAndCatchingPuckFlag)
    {
        this.posX = posX;
        this.posY = posY;
        this.resolveOrientationAndIsPuckCatching(orientationAndCatchingPuckFlag);
    }
    
    private void resolveOrientationAndIsPuckCatching(int orientationAndCatchingPuckFlag)
    {
        this.posOrientation = orientationAndCatchingPuckFlag;
        this.isCatchingPuck = (orientationAndCatchingPuckFlag & 1) == 1;
    }
    
    public enum BaconMessagePart
    {
        POS_X,
        POS_Y,
        ORIENTATION_AND_CATCHING_PUCK;
    }
    
    public int getBaconMessagePart(BaconMessagePart part)
    {
        switch(part)
        {
            case POS_X:
                return this.posX;
            case POS_Y:
                return this.posY;
            case ORIENTATION_AND_CATCHING_PUCK:
                int manipulatetOrientation = this.posOrientation;
                // Forcing last Bit
                if(this.isCatchingPuck)
                {
                    // Forcing on 1
                    manipulatetOrientation = manipulatetOrientation | 1;
                }
                else
                {
                    // Forcing on 0
                    if((manipulatetOrientation & 1) == 1)// Last Bit set, so toggle
                    {
                        manipulatetOrientation = manipulatetOrientation ^ 1; // ^ = XOR
                    }
                }
                
                return manipulatetOrientation;
        }
        return 0;
    }
    
    public OtherRoboPosition setBaconMessagePart(BaconMessagePart part, int value)
    {
        switch(part)
        {
            case POS_X:
                this.posX = value;
                break;
            case POS_Y:
                this.posY = value;
                break;
            case ORIENTATION_AND_CATCHING_PUCK:
                this.resolveOrientationAndIsPuckCatching(value);
                break;
        }
        return this;
    }

    public boolean getIsCatchingPuck()
    {
        return isCatchingPuck;
    }

    public int getPosX()
    {
        return posX;
    }

    public int getPosY()
    {
        return posY;
    }

    public int getPosOrientation()
    {
        return posOrientation;
    }

    public OtherRoboPosition setIsCatchingPuck(boolean isCatchingPuck)
    {
        this.isCatchingPuck = isCatchingPuck;
        return this;
    }

    public OtherRoboPosition setPosX(int posX)
    {
        this.posX = posX;
        return this;
    }

    public OtherRoboPosition setPosY(int posY)
    {
        this.posY = posY;
        return this;
    }

    public OtherRoboPosition setPosOrientation(int posOrientation)
    {
        this.posOrientation = posOrientation;
        return this;
    }
    
    public static void main(String[] args)
    {
        OtherRoboPosition orp1 = new OtherRoboPosition(true, 10, 10, 0);
        System.out.println("Bacon X: " + orp1.getBaconMessagePart(BaconMessagePart.POS_X) + 
                " Bacon Y: " + orp1.getBaconMessagePart(BaconMessagePart.POS_Y) + 
                " Bacon Orientation: " + orp1.getBaconMessagePart(BaconMessagePart.ORIENTATION_AND_CATCHING_PUCK)); // Orientation should be 1 degrees
        
        orp1 = new OtherRoboPosition(false, 10, 10, 0);
        System.out.println("Bacon X: " + orp1.getBaconMessagePart(BaconMessagePart.POS_X) + 
                " Bacon Y: " + orp1.getBaconMessagePart(BaconMessagePart.POS_Y) + 
                " Bacon Orientation: " + orp1.getBaconMessagePart(BaconMessagePart.ORIENTATION_AND_CATCHING_PUCK)); // Orientation should be 0 degrees
        
        orp1 = new OtherRoboPosition(10, 10, 11);
        System.out.println("Pos X: " + orp1.getPosX() + 
                " Pos Y: " + orp1.getPosY() + 
                " Pos Orientation: " + orp1.getPosOrientation() +
                " Is catching Puck: " + orp1.getIsCatchingPuck()); //Should be true
        
        orp1 = new OtherRoboPosition(10, 10, 10);
        System.out.println("Pos X: " + orp1.getPosX() + 
                " Pos Y: " + orp1.getPosY() + 
                " Pos Orientation: " + orp1.getPosOrientation() +
                " Is catching Puck: " + orp1.getIsCatchingPuck()); //Should be false
    }
}
