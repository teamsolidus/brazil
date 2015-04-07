package environmentSensing.gui.layers;

import References.AbsoluteReferencePoint;
import References.view.GUIReference;
import References.view.ReferencePointView;
import environmentSensing.scandatahandling.coordinates.CoordinatesScandata;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;

/**
 *
 * @author Simon Bühlmann
 */
public class MeasurementDataLayer
{

    // Constantes
    private final int DIAMETER_REFERENCE_POINT = 3;

    // objects
    private List<CoordinatesScandata> scandatas;
    private GUIReference guiReference;

    // Constructor
    public MeasurementDataLayer(GUIReference guiReference)
    {
        this.guiReference = guiReference;
    }

    public void setScandataSet(List<CoordinatesScandata> scandatas)
    {
        this.scandatas = scandatas;
    }
    
    public void drawMeasuredResult(Graphics g)
    {


        g.setColor(Color.RED);
        ReferencePointView.drawAbsoluteReferencePoint(guiReference, AbsoluteReferencePoint.getInstance(), g);
        
        //g.fillOval(newPoint.x - DIAMETER_REFERENCE_POINT / 2, newPoint.y - DIAMETER_REFERENCE_POINT / 2, DIAMETER_REFERENCE_POINT, DIAMETER_REFERENCE_POINT);
        
        if (this.scandatas != null)
        {
            for (CoordinatesScandata scandata : scandatas)
            {
                this.drawReflectionPoint(scandata, g, guiReference);
            }
        }
    }
    
    // private methods
    private void drawReflectionPoint(CoordinatesScandata scandata, Graphics g, GUIReference guiReference)
    {
        Point newPoint = guiReference.calculatePointInGUI(AbsoluteReferencePoint.getInstance(), scandata.getPoint());

        g.setColor(Color.BLUE);
        g.fillOval(newPoint.x - DIAMETER_REFERENCE_POINT / 2, newPoint.y - DIAMETER_REFERENCE_POINT / 2, DIAMETER_REFERENCE_POINT, DIAMETER_REFERENCE_POINT);
    }
}
