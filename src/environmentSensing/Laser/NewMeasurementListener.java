/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package environmentSensing.Laser;

/**
 *
 * @author simon.buehlmann
 */
public interface NewMeasurementListener
{
    /**
     * Wird aufgerufen, sobald ein neues Byte mit Messdaten vorhanden ist.
     * @param value Neuer Messwert
     */
    public void newMeasurementValues(byte value);
}
