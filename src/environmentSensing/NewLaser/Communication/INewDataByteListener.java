
package Laser.Communication;

/**
 *
 * @author simon.buehlmann
 */
public interface INewDataByteListener
{
    /**
     * Wird aufgerufen, sobald ein neues Byte mit Messdaten vorhanden ist.
     * @param value Neuer Messwert
     */
    public void newMeasurementValues(byte value);
}
