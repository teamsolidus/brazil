package Traveling;

import References.ReferencePoint;

/**
 *
 * @author simon.buehlmann
 */
public interface IPositioning
{
    public ReferencePoint correctPosition(ReferencePoint vaguePosition);
}
