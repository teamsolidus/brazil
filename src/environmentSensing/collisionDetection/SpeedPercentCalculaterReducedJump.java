package environmentSensing.collisionDetection;

import Tools.SolidusLoggerFactory;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author simon.buehlmann
 */
public class SpeedPercentCalculaterReducedJump
{

    private static Logger log;

    // Data Buffer
    private int lastPercent;
    private long lastTime;

    //ramp definitions
    private double gradientMaxSinkingRamp;
    private double gradientMaxRisingRamp;
    private double gradientNormalRamp;
    
    private int staticFallbackMaxSinking;
    private int staticFallbackMaxRising;

    // Limits
    private int minDistanceLimit;
    private int fullSpeedLimit;

    public SpeedPercentCalculaterReducedJump(int fullspeedlimit)
    {
        // Limits
        this.fullSpeedLimit = fullspeedlimit;
        this.minDistanceLimit = 50;

        // Max Sinking
        {
            int deltaTime = 100; // in ms
            int deltaPercent = -1000; // um % * 100
            this.gradientMaxSinkingRamp = (double) deltaPercent / (double) deltaTime; // Allowed to sink deltaPercent in deltaTime;
            this.staticFallbackMaxSinking = 10;
        }

        // Max Rising
        {
            int deltaTime = 100; // in ms
            int deltaPercent = 1000; // um % * 100
            this.gradientMaxRisingRamp = (double) deltaPercent / (double) deltaTime; // Allowed to sink deltaPercent in deltaTime;
            this.staticFallbackMaxRising = 10;
        }

        // Normal
        int deltaY = 10000; //%
        int deltaX = this.fullSpeedLimit;
        this.gradientNormalRamp = ((double) deltaY / (double) deltaX);

        // Init Values
        this.lastTime = System.currentTimeMillis();
        this.lastPercent = 10000;
    }

    public int calculate(int distance)
    {
        int tempReturn = this.calcSpeedPercentWithoutJump(distance);
        this.lastTime = System.currentTimeMillis();
        
        this.lastPercent = tempReturn;
        return tempReturn;
    }

    public int calcSpeedPercentWithoutJump(int distance)
    {
        int percentRef = this.calcMainRamp(distance);

        if (distance < minDistanceLimit)
        {
            // Jump!
            SolidusLoggerFactory.getSpeedPercent().info("Speed percent ramp jump!");
            
            return this.calcMainRamp(distance);
        }
        else
        {
            // Check: Rising or sinking?
            if (percentRef < this.lastPercent)
            {
                // sinking
                int tempPercentMain = this.calcMainRamp(distance);
                int tempMaxSinkingValue = this.calcMaxSinkingValue();
                
                // Static Fallback (be sure that the robo break)
                if(tempMaxSinkingValue == 0)
                {
                    tempMaxSinkingValue = this.staticFallbackMaxSinking;
                }
                
                int tempPercentMax = this.lastPercent + tempMaxSinkingValue;

                if (tempPercentMain > tempPercentMax)
                {
                    return tempPercentMain;
                }
                else
                {
                    return tempPercentMax;
                }
            }
            else if (percentRef > this.lastPercent)
            {
                // rising
                int tempPercentMain = this.calcMainRamp(distance);
                int tempMaxRisingValue = this.calcMaxRisingValue();
                
                if(tempMaxRisingValue == 0)
                {
                    tempMaxRisingValue = this.staticFallbackMaxRising;
                }
                
                int tempPercentMax = this.lastPercent + tempMaxRisingValue;

                if (tempPercentMain < tempPercentMax)
                {
                    return tempPercentMain;
                }
                else
                {
                    return tempPercentMax;
                }
            }
            else
            {
                return percentRef;
            }
        }
    }

    private int calcMainRamp(int distance)
    {
        // Main Ramp function
        int tempSpeedPercent = (int) (this.gradientNormalRamp * distance);
        return tempSpeedPercent;
    }

    private int calcMaxRisingValue()
    {
        int deltaTime = (int) (System.currentTimeMillis() - this.lastTime);
        int tempMaxRising = (int) ((double) (this.gradientMaxRisingRamp * deltaTime));

        return tempMaxRising;
    }

    private int calcMaxSinkingValue()
    {
        // define max. breaking ramp
        int deltaTime = (int) (System.currentTimeMillis() - this.lastTime);
        int tempMaxSinking = (int) ((double) (this.gradientMaxSinkingRamp * deltaTime));

        return tempMaxSinking;
    }

    public static void main(String[] args)
    {
        System.out.println("START==================================================");
        SpeedPercentCalculaterReducedJump spc = new SpeedPercentCalculaterReducedJump(800);

        System.out.println(spc.calculate(800));

        for (int countFor = 0; countFor < 40; countFor++)
        {

            try
            {
                Thread.sleep(10);
                System.out.println(spc.calculate(400) / 100 + "%               Count: " + countFor);

            }
            catch (InterruptedException ex)
            {
                java.util.logging.Logger.getLogger(SpeedPercentCalculaterReducedJump.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println("===============================================================================================");

        try
        {
            for (int countFor = 0; countFor < 100; countFor++)
            {

                Thread.sleep(10);
                System.out.println(spc.calculate(800) / 100 + "%               Count: " + countFor);

            }

            Thread.sleep(10);
            System.out.println(spc.calculate(301) / 100 + "%");
            Thread.sleep(10);
            System.out.println(spc.calculate(301) / 100 + "%");
            Thread.sleep(10);
            System.out.println(spc.calculate(301) / 100 + "%");
            Thread.sleep(10);
            System.out.println(spc.calculate(291) / 100 + "%");
        }
        catch (InterruptedException ex)
        {
            java.util.logging.Logger.getLogger(SpeedPercentCalculaterReducedJump.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
