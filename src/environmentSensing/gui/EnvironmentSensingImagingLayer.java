/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package environmentSensing.gui;

import References.AReferencePoint;
import References.AbsoluteReferencePoint;
import References.ReferencePoint;
import References.view.GUIReference;
import References.view.ReferencePointView;
import environmentSensing.Environment;
import environmentSensing.NewLaser.ReflectionPoint;
import environmentSensing.positioning.positionEvaluation.IEnvironmentSensor;
import environmentSensing.positioning.positionEvaluation.StraightLine;
import environmentSensing.positioning.positionEvaluation.dummyScenarios.Scenario1;
import environmentSensing.positioning.positionEvaluation.dummyScenarios.Scenario2;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author simon.buehlmann
 */
public class EnvironmentSensingImagingLayer extends JPanel
{    
    //Objects
    private GUIReference guiReference;
    private ReferencePointView referencePointView;
    private List<Point> laserMeasurementPoints;
    private IEnvironmentSensor environmentSensor;
    private LaserReflectionPointView laserRefView;
    private StraightLineView lineView;
    private Environment environment;
    
    //<editor-fold defaultstate="collapsed" desc="SINGLETON">
    private static EnvironmentSensingImagingLayer instance;
    public static EnvironmentSensingImagingLayer getInstance()
    {
        if(instance==null)
        {
            instance = new EnvironmentSensingImagingLayer();
        }
        return instance;
    }

//</editor-fold>
    
    private EnvironmentSensingImagingLayer()
    {
        this.environment = Environment.getInstance();
        
        int temp1 = this.environment.getEnvironmentXLenght();
        int temp2 = this.environment.getEnvironmentYWidth();
        
        
        this.guiReference = new GUIReference((this.environment.getEnvironmentXLenght()/2)*-1, this.environment.getEnvironmentYWidth(), 15, this.environment.getEnvironmentXLenght(), this.environment.getEnvironmentYWidth());
        this.referencePointView = new ReferencePointView(this.guiReference);
        this.environmentSensor = Scenario1.getInstance();
        this.laserRefView = new LaserReflectionPointView();
        this.lineView = new StraightLineView();
        
        this.setSize(this.guiReference.getGuiLength(), this.guiReference.getGuiWidth());
        this.setBackground(Color.YELLOW);
        this.setVisible(true);
        this.validate();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        this.paintComponents(g);
        
        g.setColor(Color.BLACK);
        //g.clearRect(0, 0, this.FIELD_LENGHT/this.SCALE, this.FIELD_WIDTH/this.SCALE);
        //g.drawRect(0, 0, this.FIELD_LENGHT/this.SCALE, this.FIELD_WIDTH/this.SCALE);
        
        g.setColor(Color.YELLOW);
        g.fillOval(10, 10, 10, 10);
        //g.drawLine(0, 0, 1000, 1000);
        
        this.referencePointView.drawReferencePoint(new ReferencePoint(2500, 2500, 0, AbsoluteReferencePoint.getInstance()), g.create());
        this.referencePointView.drawAbsoluteReferencePoint(AbsoluteReferencePoint.getInstance(), g);
        
        // MeasurementPoints
        //this.laserRefView.drawMeasuredResult(this.environmentSensor.getEnvironmentReflections(), g, guiReference);
        
        // Lines
        //StraightLine tempLine = new StraightLine(AbsoluteReferencePoint.getInstance(), new Point(1000, 1000), new Point(2000, 2000));
        //this.lineView.drawStraightLine(tempLine, this.environment, g, guiReference);
        
    }    
}
