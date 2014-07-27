
package MainPack;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import static java.lang.Thread.sleep;

/**
 *
 * @author nils.roethlisbergern
 */
public class Start {

    Robot r = new Robot();
    ProcessBuilder roboview = new ProcessBuilder(
        "C:\\Programme\\Didactic\\RobotinoView2\\bin\\robview2.exe",
            "C:\\Prod+Explo1.18.rvw2");
    Dimension screenSize;
    int screenHeight;
    int screenWidth;
    Process p;

    public Start() throws AWTException
    {
    }

    public void Startup() throws InterruptedException, IOException {
//<editor-fold defaultstate="collapsed" desc="StartView">
        p = roboview.start();
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="MaximizeWindow">
        sleep(20000);
        r.keyPress(KeyEvent.VK_ALT);
        sleep(100);
        r.keyPress(KeyEvent.VK_SPACE);
        sleep(100);
        r.keyRelease(KeyEvent.VK_SPACE);
        sleep(100);
        r.keyRelease(KeyEvent.VK_ALT);
        sleep(100);
        r.keyPress(KeyEvent.VK_UP);
        sleep(100);
        r.keyRelease(KeyEvent.VK_UP);
        sleep(100);
        r.keyPress(KeyEvent.VK_UP);
        sleep(100);
        r.keyRelease(KeyEvent.VK_UP);
        sleep(100);
        r.keyPress(KeyEvent.VK_ENTER);
        sleep(100);
        r.keyRelease(KeyEvent.VK_ENTER);
        sleep(100);
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="StartServer">
        r.mouseMove(getScreenWidth() - 15, 200);
        r.delay(100);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.delay(100);
        r.mouseMove(getScreenWidth() - 15, 500);
        r.delay(100);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        r.delay(100);
        r.mouseMove(getScreenWidth() - 150, getScreenHeight() - 135);
        r.delay(100);
        r.mousePress(InputEvent.BUTTON3_MASK);
        r.delay(100);
        r.mouseRelease(InputEvent.BUTTON3_MASK);
        r.mouseMove(getScreenWidth() - 125, getScreenHeight() - 75);
        r.delay(100);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.delay(100);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        r.delay(100);
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="StartConnection">
        r.mouseMove(320, 50);
        r.delay(2000);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.delay(100);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        r.delay(100);
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="StartProgram">
        r.mouseMove(160, 50);
        r.delay(2000);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.delay(100);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        r.delay(100);
//</editor-fold>
    }

    public void Restart() throws InterruptedException, IOException {
//<editor-fold defaultstate="collapsed" desc="CloseView">
        p.destroy();
        sleep(1000);

//</editor-fold>
        Startup();
    }

    public int getScreenHeight() {

        screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        screenHeight = (int) screenSize.getHeight();

        return screenHeight;
    }

    public int getScreenWidth() {

        screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = (int) screenSize.getWidth();

        return screenWidth;
    }

    public static void main(String[] args) throws AWTException, InterruptedException, IOException {
        Start v = new Start();
        v.Startup();
        v.Restart();


    }
}
