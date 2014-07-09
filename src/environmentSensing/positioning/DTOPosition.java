
package environmentSensing.positioning;

import References.ReferencePoint;

/**
 *
 * @author simon.buehlmann
 */
public class DTOPosition
{
    private ReferenceCase referenceCase;
    private Integer x, y, angle;
    private ReferencePoint reference;
    
    public DTOPosition(ReferenceCase referenceCase, Integer x, Integer y, Integer angle, ReferencePoint reference)
    {
        this.referenceCase = referenceCase;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.reference = reference;
    }

    public ReferenceCase getReferenceCase()
    {
        return referenceCase;
    }

    public Integer getX()
    {
        return x;
    }

    public Integer getY()
    {
        return y;
    }

    public Integer getAngle()
    {
        return angle;
    }

    public ReferencePoint getReference()
    {
        return reference;
    }
    
    public enum ReferenceCase
    {
        ALL_REFERENCES,
        X_REFERENCE,
        Y_REFERENCE;
    }
}
