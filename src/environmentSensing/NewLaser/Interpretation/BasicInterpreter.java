package Laser.Interpretation;

import Laser.Communication.INewDataByteListener;

/**
 *
 * @author simon.buehlmann
 */
public class BasicInterpreter implements INewDataByteListener
{
    //Variablen

    //Konstanten Steuerzeichen Protokoll
    private final byte STX = 2;//ASCII Zeichen
    private final byte ETX = 3;//ASCII Zeichen;
    private final byte SPACE = 32;//ASCII Zeichen

    private enum Step
    {
        STX,
        ETX,
        COMMAND_TYPE,
        WRITE_DATA;
    }

    private Step protocollStep;

    private String commandDescription;
    private Command command;

    //Objekte
    private ICommandListener listener;

    //Konstruktor
    public BasicInterpreter(ICommandListener listener)
    {
        this.listener = listener;
        
        //Initializations
        this.protocollStep = Step.STX;
        this.commandDescription = "";
    }

    //<editor-fold defaultstate="collapsed" desc="Private_Methods">
    private synchronized void assignToCommand(byte value)
    {
        switch (protocollStep)
        {
            case STX://Start new Protocoll
                protocollStep = Step.COMMAND_TYPE;
                break;

            case COMMAND_TYPE://Interpret command type
                if (value != SPACE)
                {
                    this.commandDescription = commandDescription + (char) value;
                }
                else
                {
                    protocollStep = Step.WRITE_DATA;
                    this.interpretCommand();
                }
                break;

            case WRITE_DATA:
                this.writeData(value);
                break;
                
            case ETX:
                this.command.getData().release();
                this.listener.newData(this.command);
                this.commandDescription = "";
                protocollStep = Step.STX;
                break;
        }
    }

    private void writeData(byte value)
    {
        switch (value)
        {
            case SPACE://Erzeugen von neuem Messdaten-Abschnitt
                this.command.getData().newSegment();
                break;

            case ETX://Abschliessen von Messdatenobjekt & Informieren Listener
                this.protocollStep = Step.ETX;
                this.assignToCommand(value);
                break;

            default://Hinzufügen zu bestehendem Messdaten-Abschnitt in bestehendem -Objekt.
                this.command.getData().addByte(value);
                break;

        }
    }

    /**
     * Erzeugen des richtigen command objekts
     */
    private void interpretCommand()
    {
        switch (this.commandDescription)
        {
            case "sRA":
                this.command = new Measurement();
                break;
            case "sSN":
                this.command = new Measurement();
                break;
            case "sEA":
                this.command = new Confirm();
                break;
            default:
                break;
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="NewMeasurementListener">
    @Override
    public void newMeasurementValues(byte value)
    {
        this.assignToCommand(value);
        //System.out.println("New DATA-BYTE");
    }
//</editor-fold>

}
