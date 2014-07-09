 /*
    Diese Klasse wurde erstellt, um mithilfe des SICK 2D Laserscanners
    den Robotino abzubremsen und bei Bedarf in  kompletten Stillstand 
    zu versetzen. Es wird ein Prozentwert ausgegeben, der direkt vom ComView
    bei jedem Durchgang abgefragt wird. Solange die Variable running auf true 
    gesetzt ist, wird der korrekte Wert übergeben, ansonsten wird die Berechnung 
    ignoriert und es wird 100% gesendet, also volle Geschwindigkeit.
    Es kann ausserdem die Variable obstacleOccured auf true gesetzt werden
    sobald der Roboter angehalten hat und die Zeit standstillTime abgelaufen
    ist.
    Author:Nils Röthlisberegr

*/
package Traveling;

import ComView.ComView;
import environmentSensing.Laser.Laser;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;




public class LaserControlledBreaking {

    private final static int MINDIST = 250;            // Wenn diese Distanz unterschritten wird, hält der Roboter komplett an
    private final static double FACT = 0.1;            // Faktor zum Umrechnen von Milimeter zu Prozent
    private final static int DEFAULT_DISTANCE = 2000;  // Standarddistanz, die für ein Hindernis festgelegt wird
    private final static int DEFAULT_PERCENT = 100;    // Standardwert für die Geschwindigkeit =100%
    private int speedPercent, lastSpeedPercent;        // Faktor, um den der Robotino abgebremst wird (1-20). Wert über 20=Robotino steht still
    private int hindernisdistanz;                      // Distanz eines Hindernis. Wert wird vom Laser gegeben
    public boolean running = false;                    // Von aussen setzbare Variable
    private long startTime = 0;                        // Die Variable startTime wird verwendet, um einen Timer zu erstellen
    private long currentTime = 0;                      // Die Variable currentTime wird verwendet, um einen Timer zu erstellen
    private boolean obstacleOccured = false;           // obstacleOccured wird true wenn der Roboter ein Hindernis erkannt hat und desswegen eine bestimmte Zeit stillstand.
    private final int standstillTime=5000;             // Zeit, nach der die Variable obstacleOccured true wird
 

   
    static Laser tim;
    private static LaserControlledBreaking instance;
    public static LaserControlledBreaking getInstance() {
        if (instance == null) {
            instance = new LaserControlledBreaking();
        }
        return instance;
    }

    private LaserControlledBreaking()
    {
        speedPercent = 100;
        hindernisdistanz = DEFAULT_DISTANCE;
        lastSpeedPercent = DEFAULT_PERCENT;
        tim = Laser.getInstance();
    }

    public void Calculate() {
//<editor-fold defaultstate="collapsed" desc="Berechnung">

        try {
            tim.getNewMeasurementData();                                            // Neue Laserdaten abfragen
        } catch (IOException ex) {
            Logger.getLogger(ComView.class.getName()).log(Level.SEVERE, null, ex); 
        }
        hindernisdistanz = tim.getMinDistance(0, 13);                               // Die minimale Hindernisdistanz innerhalb des Winkels wird abgefragt

        speedPercent = (int) ((hindernisdistanz - MINDIST) * FACT);                 // Formel zur Errechnung des Wertes in %

      
        if ((speedPercent > lastSpeedPercent) && (speedPercent > MINDIST)) {        // Anfahrrampe 
            speedPercent = lastSpeedPercent + 10;

            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(ComView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        lastSpeedPercent = speedPercent;

        if (speedPercent <= 0) {                                                    // obstacleOccured Teil

            if (startTime == 0) {
                startTime = System.currentTimeMillis();

            } else {
                currentTime = System.currentTimeMillis();
            }

        } else {
            obstacleOccured = false;
            startTime = 0;
            currentTime = 0;
        }

        if (currentTime - startTime >= standstillTime) {
            obstacleOccured = true;
        }
    }
//</editor-fold>
    
    
    public boolean isObstacleOccured() 
  
    {
        return obstacleOccured;
    }

    public boolean isRun()
 
    {
        return running;
    }
 
    public void setRun(boolean run) {
        this.running = run;
    }
    
    public int getBremsfaktor()
     /*
            Diese Methode wird direkt vom ComView abgefragt. Sie grenzt den
            Prozentwert, der in der Calculate Methode errechnet wurde
            entsprechend ein und gibt den richtigen Wert weiter. 
            Wenn running false ist wird immer 100% ausgegeben.
    */
    {
        if (running) {

            Calculate();
            
             if ((hindernisdistanz>250)&&(hindernisdistanz<=350))
               {
               speedPercent=10;
               }
             
            if (speedPercent > 100) {
                speedPercent = 100;
            } else if (speedPercent < 0) {
                speedPercent = 0;
            } 
        } 
        
        else {
            speedPercent = 100;
        }
        return speedPercent;
        
        
    }

    public static void main(String[] args)
  //<editor-fold defaultstate="collapsed" desc="Mainmethode für Testzwecke ">
    {
        LaserControlledBreaking lcb = new LaserControlledBreaking();

        System.out.println(lcb.getBremsfaktor() + "/" + lcb.isRun());
        lcb.setRun(true);

        lcb.setRun(true);
    }
 //</editor-fold>
}
