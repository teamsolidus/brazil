package environmentSensing.gui.views.percentDiagramm;

import References.view.GUIReference;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Panel;
import org.apache.log4j.Logger;

/**
 *
 * @author simon.buehlmann
 */
public class PercentDiagrammView extends Panel implements Runnable
{
    // Loger
    private static Logger log;
    
    /**
     * Repainting everey x milisecond
     */
    private int TIME_RESOLUTION;
    private int X_MAX_VALUE;
    private int Y_MAX_VALUE;

    private Byte[] drawingDatas;

    // Data Provider
    private IPercentDataProvider dataProvider;

    // Thread
    private Thread thread;
    
    private GUIReference guiReference;

    public PercentDiagrammView(int bufferSize, int TIME_RESOLUTION, byte bufferInitValue, IPercentDataProvider dataProvider, int width, int height)
    {
        log = org.apache.log4j.Logger.getLogger("Laser_Logger");
        System.out.println("creating percent diagramm");

        this.dataProvider = dataProvider;

        // Init Buffer
        this.drawingDatas = new Byte[bufferSize];
        for (int countFor = 0; countFor < bufferSize; countFor++)
        {
            this.drawingDatas[countFor] = bufferInitValue;
        }

        this.X_MAX_VALUE = bufferSize; // Byte
        this.Y_MAX_VALUE = 100; // Percent

        // Config Panel (this)
        this.setSize(width, height);
        this.setVisible(true);
        this.guiReference = new GUIReference(0, height, 1, width, height);

        // Creat Drawing Thread
        this.TIME_RESOLUTION = TIME_RESOLUTION;
        this.thread = new Thread(this);
        this.thread.setName("Speed_Percent_Diagramm_Thread");
        this.thread.start();

    }

    private void addByteToFifoBuffer(Byte data)
    {
        for (int countFor = (this.drawingDatas.length - 1); countFor >= 0; countFor--)
        {
            if (countFor > 0)
            {
                this.drawingDatas[countFor] = this.drawingDatas[countFor - 1];
            }
        }

        this.drawingDatas[0] = data;
    }

    private int calculatePositionInAxis(int maxValue, int value, int axisLenght)
    {
        int temp = (axisLenght / maxValue) * value;
        return temp;
    }

    private int calculatePositionInXAxis(int value)
    {
        return this.calculatePositionInAxis(this.X_MAX_VALUE, value, this.getSize().width);
    }

    /**
     * Calculate Percent Position
     * @param value
     * @return 
     */
    private int calculatePositionInYAxis(int value)
    {
        return this.calculatePositionInAxis(this.Y_MAX_VALUE, value, this.getSize().height);
    }

    @Override
    public void paint(Graphics g)
    {
        // Draw Border
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, this.getSize().width-1, this.getSize().height-1);
        
        // Draw Axis
        g.setColor(Color.RED);
        
        // Draw Skala
        g.setColor(Color.GRAY);
        for(int countFor = 1; countFor <= 10; countFor++)
        {
            int tempYPos = this.calculatePositionInYAxis(countFor * 10);
            g.drawLine(0, tempYPos, this.getSize().width, tempYPos);
        }
        

        // Draw Data
        g.setColor(Color.RED);
        for (int countFor = 0; countFor < this.drawingDatas.length - 1; countFor++)
        {
            int tempY1 = this.calculatePositionInYAxis(this.drawingDatas[countFor]);
            int tempY2 = this.calculatePositionInYAxis(this.drawingDatas[countFor+ 1]);
            int tempX1 = this.calculatePositionInXAxis(countFor);
            int tempX2 = this.calculatePositionInXAxis(countFor + 1);
            
            tempY1 = this.guiReference.calculateGuiYPosition(tempY1);
            tempY2 = this.guiReference.calculateGuiYPosition(tempY2);
            tempX1 = this.guiReference.calculateGuiXPosition(tempX1);
            tempX2 = this.guiReference.calculateGuiXPosition(tempX2);

            g.drawLine(tempX1, tempY1, tempX2, tempY2);
        }
    }

    @Override
    public void run()
    {
        System.out.println("Speed percent diagramm thread started");
        while (true)
        {
            try
            {
                // Refill Buffer with a new Variable
                this.addByteToFifoBuffer(this.dataProvider.getSpeedPercent());
                this.repaint();
                this.thread.sleep(this.TIME_RESOLUTION);
            }
            catch (InterruptedException ex)
            {
                // Nothing
            }
        }
    }
}
