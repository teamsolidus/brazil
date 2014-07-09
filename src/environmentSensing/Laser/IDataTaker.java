package environmentSensing.Laser;

/**
 *
 * @author simon.buehlmann
 */
public interface IDataTaker
{
    /**
     * Hinzufügen eines Bytes zum aktuellen DataSegment
     * @param data
     */
    public void addByte(byte data);
    
    /**
     * Aktuelles DataSegment abschliessen und ein neues eröffnen
     */
    public void newSegment();
    
    /**
     * Die gesammten Messdaten freigeben
     */
    public void release();
}
