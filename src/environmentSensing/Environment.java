
package environmentSensing;

import References.AReferencePoint;
import References.AbsoluteReferencePoint;
import References.ReferencePoint;

/**
 *
 * @author simon.buehlmann
 */
public class Environment
{
    private final AReferencePoint corner45, corner135, corner225, corner315;
    
    //<editor-fold defaultstate="collapsed" desc="SINGLETON">
    private static Environment instance;
    public static Environment getInstance()
    {
        if(instance == null)
        {
            instance = new Environment();
        }
        return instance;
    }
//</editor-fold>
    
    public enum EnvironmentCorner
    {
        CORNER_45,
        CORNER_135,
        CORNER_225,
        CORNER_315;
    }
    
    public Environment()
    {
        corner45 = new ReferencePoint(5000, 5000, 0, AbsoluteReferencePoint.getInstance());
        corner135 = new ReferencePoint(5000, 0, 0, AbsoluteReferencePoint.getInstance());
        corner225 = new ReferencePoint(-5000, 0, 0, AbsoluteReferencePoint.getInstance());
        corner315 = new ReferencePoint(-5000, 5000, 0, AbsoluteReferencePoint.getInstance());
    }
    
    public int getEnvironmentXLenght()
    {
        return (this.corner315.getAbsolutX() * -1) + this.corner45.getAbsolutX();
    }
    
    public int getEnvironmentYWidth()
    {
        return (this.corner225.getAbsolutY() *-1) + this.corner315.getAbsolutY();
    }
    
    public AReferencePoint getCorner(EnvironmentCorner corner)
    {
        switch(corner)
        {
            case CORNER_45:
                return this.corner45;
            case CORNER_135:
                return this.corner135;
            case CORNER_225:
                return this.corner225;
            case CORNER_315:
                return this.corner315;
        }
        return null;
    }
}
