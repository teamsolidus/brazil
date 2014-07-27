
package Refbox.newRefBox;

/**
 *
 * @author simon.buehlmann
 */
public interface IRoboPositionDataProvider
{
    public int[] getRoboPosition(int jerseyNr);
    
    public void setPositionOtherRobo(int jerseyNr, int x, int y, int orientation);
}
