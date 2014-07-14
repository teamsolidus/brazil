
package Laser.References;

/**
 *
 * @author simon.buehlmann
 */
public class MainReferencePoint extends ReferencePoint
{
    private static MainReferencePoint instance;

    private MainReferencePoint()
    {
        super(0, 0, 0);
    }
    
    public static MainReferencePoint getInstance()
    {
        if(instance == null)
        {
            instance = new MainReferencePoint();
        }
        
        return instance;
    }

    @Override
    public int getX(Type refType)
    {
        return 0;
    }

    @Override
    public int getY(Type refType)
    {
        return 0;
    }

    @Override
    public int getAngle(Type refType)
    {
        return 0;
    }


}
