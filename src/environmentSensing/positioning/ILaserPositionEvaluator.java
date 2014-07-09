
package environmentSensing.positioning;

import References.ReferencePoint;

/**
 *
 * @author simon.buehlmann
 */
public interface ILaserPositionEvaluator
{
    public DTOPosition evaluatePosition(ReferencePoint vaguePosition) throws NoReferenceException;
}
