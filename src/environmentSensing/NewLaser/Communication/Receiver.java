
package Laser.Communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author simon.buehlmann
 */
public class Receiver implements Runnable
{
    private InputStream response;
    private BufferedReader buff;
    private INewDataByteListener listener;

    public Receiver(InputStream response, INewDataByteListener listener)
    {
        this.response = response;
        this.buff = new BufferedReader(new InputStreamReader(response));
        this.listener = listener;
    }
    
    @Override
    public void run()
    {
        int count = 0;
        while(true)
        {
            try
            {
                this.listener.newMeasurementValues((byte) buff.read());
            }
            catch (IOException ex)
            {
                Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
