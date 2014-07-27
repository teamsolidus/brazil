/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Tools;

/**
 *
 * @author Adrian
 */
public class Stoppuhr
{
    
 public long lastTime,actTime,stopTime;
public boolean firstTimeStop;    

   
  
  public long stopTime ()
  {
  actTime=System.currentTimeMillis();
  stopTime=actTime-lastTime;
      System.out.println("stoptime"+stopTime);
      if (!firstTimeStop) {
  lastTime=actTime;
  firstTimeStop=true;
          
      }
  
  return  stopTime;
  } 
    
    
    // strg umschalt r um bereich zu kopieren
  // auf zeile mit control c und v muss nicht markiert werden und es kopiert trotzdem
    
    public static void main(String[] args)
    {
       
        
        
        
    }
  
}
