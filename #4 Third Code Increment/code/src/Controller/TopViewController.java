package Controller;

import Model.*;
import Model.Helper.Utility;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class TopViewController implements Initializable {

    @FXML
    private Pane dragPane;
    @FXML
    private Label compassDegree;
    //compass
    @FXML
    private Pane compass;
    @FXML
    private Label leftDirection;
    @FXML
    private Label rightDirection;
    @FXML
    private AnchorPane topDownRunwayPane;
    @FXML
    private Label leftDesignator;
    @FXML
    private Label rightDesignator;
    @FXML
    private Rectangle runway;
    @FXML
    private Rectangle obstacleBlock;
    @FXML
    private Line centreLine;
    @FXML
    private Rectangle minCGArea;

    //tora labels
    @FXML
    private Line toraStart;
    @FXML
    private Line toraEnd;
    @FXML
    private Label toraLabel;
    @FXML
    private Line toraLength;
    @FXML
    private Line toraStart1;
    @FXML
    private Line toraEnd1;
    @FXML
    private Line toraLength1;
    @FXML
    private Label toraLabel1;

    //toda labels
    @FXML
    private Line todaStart;
    @FXML
    private Line todaEnd;
    @FXML
    private Label todaLabel;
    @FXML
    private Line todaLength;
    @FXML
    private Line todaStart1;
    @FXML
    private Line todaEnd1;
    @FXML
    private Label todaLabel1;
    @FXML
    private Line todaLength1;

    //lda labels
    @FXML
    private Line ldaStart;
    @FXML
    private Line ldaEnd;
    @FXML
    private Label ldaLabel;
    @FXML
    private Line ldaLength;
    @FXML
    private Line ldaStart1;
    @FXML
    private Line ldaEnd1;
    @FXML
    private Label ldaLabel1;
    @FXML
    private Line ldaLength1;

    //asda labels
    @FXML
    private Line asdaStart;
    @FXML
    private Line asdaEnd;
    @FXML
    private Label asdaLabel;
    @FXML
    private Line asdaLength;
    @FXML
    private Line asdaStart1;
    @FXML
    private Line asdaEnd1;
    @FXML
    private Label asdaLabel1;
    @FXML
    private Line asdaLength1;

    //other labels
    @FXML
    private Line toraOtherLineLength;
    @FXML
    private Line toraOtherLineStart;
    @FXML
    private Label toraOtherLineLabel;
    @FXML
    private Line toraOtherLineLength1;
    @FXML
    private Line toraOtherLineEnd;
    @FXML
    private Label toraOtherLineLabel1;
    @FXML
    private Line ldaOtherLineLength;
    @FXML
    private Line ldaOtherLineStart;
    @FXML
    private Label ldaOtherLineLabel;
    @FXML
    private Line ldaOtherLineLength1;
    @FXML
    private Line ldaOtherLineEnd;
    @FXML
    private Label ldaOtherLineLabel1;

    //references
    @FXML
    private Rectangle clearwayL;
    @FXML
    private Rectangle clearwayR;
    @FXML
    private Rectangle stopwayL;
    @FXML
    private Rectangle stopwayR;
    @FXML
    private Line displacedThresholdL;
    @FXML
    private Line displacedThresholdR;
    @FXML
    private Line thresholdL;
    @FXML
    private Line thresholdR;

    //arrows
    @FXML
    private Polygon todaArrow;
    @FXML
    private Polygon asdaArrow;
    @FXML
    private Polygon ldaArrow;
    @FXML
    private Polygon toraArrow;
    @FXML
    private Polygon todaArrow1;
    @FXML
    private Polygon ldaArrow1;
    @FXML
    private Polygon asdaArrow1;
    @FXML
    private Polygon toraArrow1;
    @FXML
    private Polygon toraOtherArrow;
    @FXML
    private Polygon ldaOtherArrow;
    @FXML
    private Polygon toraOtherArrow1;
    @FXML
    private Polygon ldaOtherArrow1;

    //scales
    @FXML
    private Label scaleLabel;
    @FXML
    private Rectangle scaleStart;
    @FXML
    private Rectangle scaleEnd;
    @FXML
    private Rectangle scaleLength;
    @FXML
    private Label scale0;
    @FXML
    private Label scale500;
    @FXML
    private Label scale1000;
    @FXML
    private Label scale1500;
    @FXML
    private Label scaleUnit;

    @FXML
    private Rectangle gradedAreaLegend;
    @FXML
    private Rectangle stopwayLegend;
    @FXML
    private Rectangle clearwayLegend;
    @FXML
    private Rectangle obstacleLegend;
    @FXML
    private Rectangle displacedThresholdLegend;
    @FXML
    private Rectangle minCGArea1;
    @FXML
    private Polyline minCGArea2;
    @FXML
    private Polyline minCGArea3;
    @FXML
    private Polyline minCGArea4;
    @FXML
    private Polyline minCGArea5;
    @FXML
    private Label minCGLabel;
    @FXML
    private Label maxCGLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //adding listeners to update views

        //listener for different airport selection
        MainController.airportItem.addListener((observable, oldValue, newValue) -> {
            leftDesignator.setText("___");
            rightDesignator.setText("___");
        });
        //listener for different physical runway selections
        MainController.physRunwayItem.addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                resetValues(MainController.getPhysRunwaySelected());
                if(MainController.getObstacleSelected() != null){
                    relocateObstacle();
                }
            }
        });
        //listener for different obstacle selection
        MainController.obstacleProperty.addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                if(oldValue != null){
                    oldValue.setDistFThreshold(0);
                    oldValue.setDistFCent(0);
                }
                newValue.setDistFThreshold(MainController.disFromThreshold.get());
                relocateObstacle();
            }
        });

        //listener for result change to update with the revised parameters
        MainController.dirFromCentre.addListener((observable, oldValue, newValue) -> relocateObstacle());
        MainController.disFromThreshold.addListener((observable, oldValue, newValue) -> relocateObstacle());
        MainController.disFromCentre.addListener((observable, oldValue, newValue) -> relocateObstacle());
        MainController.valueChanged.addListener((observable, oldValue, newValue) -> updateLabel());
        MainController.themeProperty.addListener((observable, oldValue, newValue) -> setVisualColor(newValue.intValue()));
        //variables
        AnchorPane topDownRunwayPane = getTopDownRunwayPane();
        Pane compass = getCompass();
        //initialize drag, zoom and rotate
        Utility.initializeZoom(topDownRunwayPane);
        Utility.initializeDrag(dragPane,getTopDownRunwayPane());
        initializeRotate(topDownRunwayPane,compass);

        if(MainController.beforeCalculation){
            if(MainController.getPhysRunwaySelected() != null){
                resetValues(MainController.getPhysRunwaySelected());
            }
            if(MainController.getObstacleSelected() != null){
                relocateObstacle();
            }
        } else{
            updateLabel();
        }

        setVisualColor(1);
        initLabelFlip();
    }

    public void setVisualColor(int i){
        //1 = default
        //2 = red-green
        //3 = blue-yellow
        Paint gradedArea = Paint.valueOf(i == 1? "#dddddd":
                i == 2? "#EAEAEA": //rgb(237,232,233)
                        "#E2E2E2"); //rgb(227,223,239)
        Paint stopway = Paint.valueOf(i == 1? "#fcf264":
                i == 2? "#FBCB92":  //rgb(225,210,155)
                        "#EFA5A5");  //rgb(228, 165, 174)
        Paint clearway = Paint.valueOf(i == 1? "#64cd72":
                i == 2? "#35C9FF":  //rgb(170,1134,139)
                        "#739DCC");  //rgb(117, 160, 172)
        Paint obstacle = Paint.valueOf(i == 1? "#f53e3e":
                "#FF0000");          //rgb(141,126,50)
                                    //rgb(233,56,36)
        Paint displacedThreshold = Paint.valueOf(i == 1? "#0000ff":
                i == 2? "#9052FF":  //rgb(42,119,234)
                        "#00F5FF");  //rgb(143,236,252)
        gradedAreaLegend.setFill(gradedArea);
        stopwayLegend.setFill(stopway);
        clearwayLegend.setFill(clearway);
        obstacleLegend.setFill(obstacle);
        displacedThresholdLegend.setFill(displacedThreshold);

        minCGArea.setFill(gradedArea);
        minCGArea1.setFill(gradedArea);
        minCGArea2.setFill(gradedArea);
        minCGArea3.setFill(gradedArea);
        minCGArea4.setFill(gradedArea);
        minCGArea5.setFill(gradedArea);

        stopwayL.setFill(stopway);
        stopwayR.setFill(stopway);

        clearwayL.setFill(clearway);
        clearwayR.setFill(clearway);

        obstacleBlock.setFill(obstacle);

        displacedThresholdL.setFill(displacedThreshold);
        displacedThresholdL.setStroke(displacedThreshold);
        displacedThresholdR.setFill(displacedThreshold);
        displacedThresholdR.setStroke(displacedThreshold);
    }

    //function to update labels and line in top view
    public void updateLabel(){
        relocateObstacle();
        PhysicalRunway selectedPhyRunway = MainController.getPhysRunwaySelected();
        LogicalRunway llogRunway = selectedPhyRunway.getLogicalRunways().get(0);
        Obstacle obstacle = MainController.getObstacleSelected();
        //Setting up arrows for lines
        Arrow ToraArrow = new Arrow(toraStart,toraLength,toraEnd,toraLabel,toraArrow);
        Arrow LdaArrow = new Arrow(ldaStart,ldaLength,ldaEnd,ldaLabel,ldaArrow);
        Arrow AsdaArrow = new Arrow(asdaStart,asdaLength,asdaEnd,asdaLabel,asdaArrow);
        Arrow TodaArrow = new Arrow(todaStart,todaLength,todaEnd,todaLabel,todaArrow);
        Arrow ToraArrow1 = new Arrow(toraEnd1,toraLength1,toraStart1,toraLabel1,toraArrow1);
        Arrow LdaArrow1 = new Arrow(ldaEnd1,ldaLength1,ldaStart1,ldaLabel1,ldaArrow1);
        Arrow AsdaArrow1 = new Arrow(asdaEnd1,asdaLength1,asdaStart1,asdaLabel1,asdaArrow1);
        Arrow TodaArrow1 = new Arrow(todaEnd1,todaLength1,todaStart1,todaLabel1,todaArrow1);

        boolean needRedeclare = Calculator.needRedeclare(obstacle, llogRunway);

        if(Calculator.needRedeclare(obstacle, llogRunway)){
            Calculator.performCalc(obstacle,selectedPhyRunway);
            resetValues(selectedPhyRunway);
            //setting up new lines for new runway parameters
            setNewLine("TORA","Left",selectedPhyRunway,obstacle,ToraArrow);
            setNewLine("LDA","Left",selectedPhyRunway,obstacle,LdaArrow );
            setNewLine("ASDA","Left",selectedPhyRunway,obstacle,AsdaArrow);
            setNewLine("TODA","Left",selectedPhyRunway,obstacle,TodaArrow);
            setNewLine("TORA","Right",selectedPhyRunway,obstacle,ToraArrow1);
            setNewLine("LDA","Right",selectedPhyRunway,obstacle,LdaArrow1);
            setNewLine("ASDA","Right",selectedPhyRunway,obstacle,AsdaArrow1);
            setNewLine("TODA","Right",selectedPhyRunway,obstacle,TodaArrow1);
            //setting up other lines (resa/stripend/alstocs/blast protection)
            boolean isTalo = Calculator.getFlightMethod(obstacle,llogRunway).equals(Calculator.talo);
            setToraOtherLine(needRedeclare,isTalo, false, toraOtherLineLabel, toraOtherLineLength, toraOtherLineStart, toraOtherArrow);
            setLdaOtherLine(needRedeclare,isTalo, false, ldaOtherLineLabel, ldaOtherLineLength, ldaOtherLineStart, ldaOtherArrow);
            setToraOtherLine(needRedeclare,isTalo, true, toraOtherLineLabel1, toraOtherLineLength1, toraOtherLineEnd, toraOtherArrow1);
            setLdaOtherLine(needRedeclare,isTalo, true, ldaOtherLineLabel1, ldaOtherLineLength1, ldaOtherLineEnd, ldaOtherArrow1);
        } else{
            //if not runway redeclaration is needed, simply reset the value
            resetValues(selectedPhyRunway);
        }
        int colour;
        if(needRedeclare){
            colour = 1;
        } else{
            colour = 2;
        }
        changeColor(colour, ToraArrow);
        changeColor(colour, TodaArrow);
        changeColor(colour, AsdaArrow);
        changeColor(colour, LdaArrow);
        changeColor(colour, ToraArrow1);
        changeColor(colour, TodaArrow1);
        changeColor(colour, AsdaArrow1);
        changeColor(colour, LdaArrow1);
    }

    private void changeColor(int i, Arrow arrow){
        Color colour;
        if(i == 1){
            colour = Color.DARKBLUE;
        } else{
            colour = Color.BLACK;
        }
        arrow.getLabel().setTextFill(colour);
    }

    protected void resetValues(PhysicalRunway physicalRunway){
        //hide labelling for additional lines for reference
        toraOtherLineEnd.setVisible(false);
        toraOtherLineStart.setVisible(false);
        toraOtherLineLabel.setVisible(false);
        toraOtherLineLabel1.setVisible(false);
        toraOtherLineLength1.setVisible(false);
        toraOtherLineLength.setVisible(false);
        toraOtherArrow.setVisible(false);
        toraOtherArrow1.setVisible(false);

        ldaOtherLineEnd.setVisible(false);
        ldaOtherLineStart.setVisible(false);
        ldaOtherLineLabel.setVisible(false);
        ldaOtherLineLabel1.setVisible(false);
        ldaOtherLineLength.setVisible(false);
        ldaOtherLineLength1.setVisible(false);
        ldaOtherArrow.setVisible(false);
        ldaOtherArrow1.setVisible(false);

        //resetting runway and areas to the original parameters
        LogicalRunway lLogicalRunway = physicalRunway.getLogicalRunways().get(0);
        setUpPhyRunway(physicalRunway,lLogicalRunway);
        setUpStopwayAndClearway(physicalRunway,lLogicalRunway);
        setUpLogicalRunway(physicalRunway);
        setUpScale(lLogicalRunway);
    }

    //function to set up stopway and clearway areas for the runway
    protected void setUpStopwayAndClearway(PhysicalRunway physicalRunway, LogicalRunway selectedLogRunway){
        LogicalRunway lLogicalRunway = physicalRunway.getLogicalRunways().get(0);
        LogicalRunway rLogicalRunway = physicalRunway.getLogicalRunways().get(1);
        setStopClearway(lLogicalRunway,"Right");
        setStopClearway(rLogicalRunway,"Left");
    }

    //function to set up physical runway based on the one selected, showing the original parameter values
    protected void setUpPhyRunway(PhysicalRunway physicalRunway, LogicalRunway selectedLogRunway){
        LogicalRunway lLogicalRunway = physicalRunway.getLogicalRunways().get(0);
        LogicalRunway rLogicalRunway = physicalRunway.getLogicalRunways().get(1);

        //Set Up Threshold
        thresholdL.setLayoutX(runway.getLayoutX());
        thresholdR.setLayoutX(runway.getLayoutX()+runway.getWidth());

        //Set Up Designator
        String lDesignator = lLogicalRunway.getDesignator();
        String rDesignator = rLogicalRunway.getDesignator();

        //separate num and alphabet
        String lDegree = lDesignator.replaceAll("[a-zA-Z]","");
        String rDegree = rDesignator.replaceAll("[a-zA-Z]","");
        String lDirection = lDesignator.replaceAll("[0-9]","");
        String rDirection = rDesignator.replaceAll("[0-9]","");

        //Set Up DisplacedThreshold
        double lDisplacedThreshold = lLogicalRunway.getDisplacedThreshold();
        double rDisplacedThreshold = rLogicalRunway.getDisplacedThreshold();
        double lDisplacedThresholdX;
        double rDisplacedThresholdX;

        leftDesignator.setText(lDegree);
        rightDesignator.setText(rDegree);
        leftDirection.setText(lDirection);
        rightDirection.setText(rDirection);
        lDisplacedThresholdX = thresholdL.getLayoutX() + getNumberOfPx(lDisplacedThreshold,lLogicalRunway);
        rDisplacedThresholdX = thresholdR.getLayoutX() - getNumberOfPx(rDisplacedThreshold,rLogicalRunway);

        displacedThresholdL.setLayoutX(lDisplacedThresholdX);
        displacedThresholdR.setLayoutX(rDisplacedThresholdX);
        rotateRunway();
    }

    //function to set up lines
    protected void setNewLine(String type, String LeftorRight, PhysicalRunway physicalRunway, Obstacle obstacle, Arrow arrow){
        LogicalRunway logicalRunway = null;
        if (LeftorRight.equals("Left")){
            logicalRunway = physicalRunway.getLogicalRunways().get(0);
        }else {
            logicalRunway = physicalRunway.getLogicalRunways().get(1);
        }

        double originalValue = oldAndNewValue(type,logicalRunway)[0];
        double newValue = oldAndNewValue(type,logicalRunway)[1];
        double difference = originalValue - newValue;
        double differenceInPx = getNumberOfPx(difference,logicalRunway);
        double labelLayout;

        //Arrow variables
        Line start = arrow.getStart();
        Line length = arrow.getLength();
        Line end = arrow.getEnd();
        Label label = arrow.getLabel();
        Polygon arrowHead = arrow.getArrowHead();

        String flightMethod = Calculator.getFlightMethod(obstacle,logicalRunway);
        String talo = Calculator.talo;
        String ttlt = Calculator.ttlt;

        if (LeftorRight.equals("Left")){
            if (flightMethod.equals(talo)){
                start.setLayoutX(start.getLayoutX() + differenceInPx);
                length.setLayoutX(length.getLayoutX() + differenceInPx);
                length.setEndX(length.getEndX() - differenceInPx);
            } else if (flightMethod.equals(ttlt)) {
                end.setLayoutX(end.getLayoutX() - differenceInPx);
                length.setEndX(length.getEndX() - differenceInPx);
            }
        }else {
            //when flightMethod for left runway is ttlt, the flightmethod for right runway will be talo, vice versa
            if (flightMethod.equals(ttlt)){
                //for flightmethod talo
                start.setLayoutX(start.getLayoutX() - differenceInPx);
                length.setLayoutX(end.getLayoutX());
                length.setEndX(length.getEndX() - differenceInPx);
            } else if (flightMethod.equals(talo)) {
                //for flightmethod ttlt
                end.setLayoutX(end.getLayoutX() + differenceInPx);
                length.setLayoutX(end.getLayoutX());
                length.setEndX(length.getEndX() - differenceInPx);
                System.out.println(type + " " + LeftorRight + " = \n" + end.getLayoutX()+ "\n" +length.getStartX() + "\n" + length.getEndX() + "\n");
            }
        }

        label.setText(" " + type +" = " + newValue + "m ");
        labelLayout = getLabelLayout(LeftorRight.equals("Left")? start: end, length, label);
        label.setLayoutX(labelLayout);
        arrowHead.setLayoutX(end.getLayoutX());
    }

    protected double getLabelLayout(Line start,Line length,Label label){
        return start.getLayoutX() + (length.getEndX()/2 - label.getWidth()/2);
    }

    protected double[] oldAndNewValue(String type, LogicalRunway logicalRunway){
        double originalValue = 0;
        double newValue = 0;
        switch (type) {
            case "TORA" -> {
                originalValue = logicalRunway.getTora();
                newValue = logicalRunway.getNewTora();
            }
            case "LDA" -> {
                originalValue = logicalRunway.getLda();
                newValue = logicalRunway.getNewLda();
            }
            case "ASDA" -> {
                originalValue = logicalRunway.getAsda();
                newValue = logicalRunway.getNewAsda();
            }
            case "TODA" -> {
                originalValue = logicalRunway.getToda();
                newValue = logicalRunway.getNewToda();
            }
        }
        return new double[]{originalValue,newValue};
    }

    protected double getNumberOfPx(double length,LogicalRunway logicalRunway){
        return length/getMeterPerPx(logicalRunway);
    }

    protected double getMeterPerPx(LogicalRunway logicalRunway){
        return logicalRunway.getTora()/ runway.getWidth();
    }

    public void setLdaOtherLine(boolean bool, boolean isTALO, boolean lower, Label label, Line length, Line startEnd, Polygon arrow){
        label.setVisible(bool);
        length.setVisible(bool);
        startEnd.setVisible(bool);
        arrow.setVisible(bool);

        if(bool){
            Obstacle obstacle = MainController.getObstacleSelected();

            double stripEnd = PhysicalRunway.getStripEnd();
            double resa = PhysicalRunway.getResa();
            double alsTocs = obstacle.getAlsTocs();
            double blastProtection = PhysicalRunway.getBlastProtection();
            double change;
            Line reference;
            if(isTALO){
                if(lower){
                    reference = ldaStart1;
                } else{
                    reference = ldaStart;
                }
            } else {
                if(lower){
                    reference = ldaEnd1;
                } else{
                    reference = ldaEnd;
                }
            }

            if(!isTALO && !lower || isTALO && lower){
                label.setText("  RESA + Strip End" +
                        "\n"+"= "+resa+"m + "+stripEnd+"m");
                change = resa+stripEnd;
            } else{
                //ttlt
                if(alsTocs < resa){
                    if(resa + stripEnd < blastProtection){
                        label.setText("Blast protection" +
                                "\n"+"    = "+PhysicalRunway.getBlastProtection()+"m");
                        change = blastProtection;
                    } else{
                        label.setText("  RESA + Strip End" +
                                "\n"+"= "+resa+"m + "+stripEnd+"m");
                        change = resa + stripEnd;
                    }
                } else{
                    if(alsTocs + stripEnd < blastProtection){
                        label.setText("Blast protection" +
                                "\n"+"    = "+PhysicalRunway.getBlastProtection()+"m");
                        change = blastProtection;
                    } else{
                        label.setText("  ALS + Strip End" +
                                "\n"+"= "+alsTocs+"m + "+stripEnd+"m");
                        change = alsTocs + stripEnd;
                    }
                }
            }

            if(!isTALO){
                startEnd.setLayoutX(reference.getLayoutX()+(change*meterPerPx()));
                length.setLayoutX(reference.getLayoutX());
                length.setEndX(startEnd.getLayoutX()-reference.getLayoutX());
                label.setLayoutX(reference.getLayoutX()-(reference.getLayoutX()-startEnd.getLayoutX())/2-label.getWidth()/2);
            } else{
                startEnd.setLayoutX(reference.getLayoutX()-(change*meterPerPx()));
                length.setLayoutX(startEnd.getLayoutX());
                length.setEndX(reference.getLayoutX()-startEnd.getLayoutX());
                label.setLayoutX(startEnd.getLayoutX()+(reference.getLayoutX()-startEnd.getLayoutX())/2-label.getWidth()/2);
            }

            boolean isTalo = Calculator.getFlightMethod(obstacle,MainController.getPhysRunwaySelected().getLogicalRunways().get(0)).equals(Calculator.talo);
            if(!lower){

                if(isTalo){
                    arrow.setLayoutX(reference.getLayoutX());
                }else {
                    arrow.setLayoutX(startEnd.getLayoutX());
                }

            } else{
                if(isTalo){
                    arrow.setLayoutX(startEnd.getLayoutX());
                }else {
                    arrow.setLayoutX(reference.getLayoutX());
                }

            }
        }
    }

    public void setToraOtherLine(boolean bool, boolean isTALO, boolean lower, Label label, Line length, Line startEnd, Polygon arrow){
        label.setVisible(bool);
        length.setVisible(bool);
        startEnd.setVisible(bool);
        arrow.setVisible(bool);

        if(bool){
            Obstacle obstacle = MainController.getObstacleSelected();

            double stripEnd = PhysicalRunway.getStripEnd();
            double resa = PhysicalRunway.getResa();
            double alsTocs = obstacle.getAlsTocs();
            double blastProtection = PhysicalRunway.getBlastProtection();
            double change;
            Line reference;
            if(isTALO){
                if(lower){
                    reference = toraStart1;
                } else{
                    reference = toraStart;
                }
            } else{
                if(lower) {
                    reference = toraEnd1;
                } else{
                    reference = toraEnd;
                }
            }

            if(isTALO && !lower || (!isTALO && lower)){
                label.setText("Blast protection" +
                        "\n"+"    = "+PhysicalRunway.getBlastProtection()+"m");
                change = PhysicalRunway.getBlastProtection();
            } else{
                //ttlt
                if(alsTocs < resa){
                    if(resa + stripEnd < blastProtection){
                        label.setText("Blast protection" +
                                "\n"+"    = "+PhysicalRunway.getBlastProtection()+"m");
                        change = blastProtection;
                    } else{
                        label.setText("   RESA + Strip End" +
                                "\n"+"= "+resa+"m + "+stripEnd+"m");
                        change = resa + stripEnd;
                    }
                } else{
                    if(alsTocs + stripEnd < blastProtection){
                        label.setText("Blast protection" +
                                "\n"+"    = "+PhysicalRunway.getBlastProtection()+"m");
                        change = blastProtection;
                    } else{
                        label.setText("  TOCS + Strip End" +
                                "\n"+"= "+alsTocs+"m + "+stripEnd+"m");
                        change = alsTocs + stripEnd;
                    }
                }
            }

            if(!isTALO){
                startEnd.setLayoutX(reference.getLayoutX()+(change*meterPerPx()));
                length.setLayoutX(reference.getLayoutX());
                length.setEndX(startEnd.getLayoutX()-reference.getLayoutX());
                label.setLayoutX(reference.getLayoutX()-(reference.getLayoutX()-startEnd.getLayoutX())/2-label.getWidth()/2);
            } else{
                startEnd.setLayoutX(reference.getLayoutX()-(change*meterPerPx()));
                length.setLayoutX(startEnd.getLayoutX());
                length.setEndX(reference.getLayoutX()-startEnd.getLayoutX());
                label.setLayoutX(startEnd.getLayoutX()+(reference.getLayoutX()-startEnd.getLayoutX())/2-label.getWidth()/2);
            }

            boolean isTalo = Calculator.getFlightMethod(obstacle,MainController.getPhysRunwaySelected().getLogicalRunways().get(0)).equals(Calculator.talo);
            if(!lower){

                if(isTalo){
                    arrow.setLayoutX(reference.getLayoutX());
                }else {
                    arrow.setLayoutX(startEnd.getLayoutX());
                }

            } else{
                if(isTalo){
                    arrow.setLayoutX(startEnd.getLayoutX());
                }else {
                    arrow.setLayoutX(reference.getLayoutX());
                }

            }
        }
    }

    public void relocateObstacle(){
        obstacleBlock.setVisible(true);
        Obstacle obstacle = MainController.getObstacleSelected();
        LogicalRunway logRunway;
        double runwayStartX = runway.getLayoutX();
        double runwayLength = runway.getWidth();
        double centre = centreLine.getLayoutY();
        double disFromThreshold = obstacle.getDistFThreshold();
        double tora;
        double stripEnd;
        double obsBlockWidth = obstacleBlock.getHeight();

        logRunway = MainController.getPhysRunwaySelected().getLogicalRunways().get(0);
        tora = logRunway.getTora();
        double displacedFromCentre = obstacle.getDirFromCentre().equals("L")? (-obstacle.getDistFCent()*(minCGArea.getHeight()/2)/PhysicalRunway.minCGArea)
                -obsBlockWidth/2: (obstacle.getDistFCent()*(minCGArea.getHeight()/2)/PhysicalRunway.minCGArea)-obsBlockWidth/2;
        if(Calculator.needRedeclare(obstacle, logRunway)){
            if(Calculator.getFlightMethod(obstacle, logRunway).equals("Take-Off Away Landing Over")){
                obstacleBlock.relocate(runwayStartX+((disFromThreshold+logRunway.getDisplacedThreshold())*(runwayLength-logRunway.getClearway())/tora) -obsBlockWidth,
                        centre+displacedFromCentre);
            } else{
                obstacleBlock.relocate(runwayStartX+((disFromThreshold+logRunway.getDisplacedThreshold())*(runwayLength-logRunway.getClearway())/tora),
                        centre+(displacedFromCentre));
            }
        }else{
            obstacleBlock.setVisible(false);
        }
    }

    protected void setUpLogicalRunway(PhysicalRunway physicalRunway){
        Arrow ToraArrow = new Arrow(toraStart,toraLength,toraEnd,toraLabel,toraArrow);
        Arrow LdaArrow = new Arrow(ldaStart,ldaLength,ldaEnd,ldaLabel,ldaArrow);
        Arrow AsdaArrow = new Arrow(asdaStart,asdaLength,asdaEnd,asdaLabel,asdaArrow);
        Arrow TodaArrow = new Arrow(todaStart,todaLength,todaEnd,todaLabel,todaArrow);
        Arrow ToraArrow1 = new Arrow(toraEnd1,toraLength1,toraStart1,toraLabel1,toraArrow1);
        Arrow LdaArrow1 = new Arrow(ldaEnd1,ldaLength1,ldaStart1,ldaLabel1,ldaArrow1);
        Arrow AsdaArrow1 = new Arrow(asdaEnd1,asdaLength1,asdaStart1,asdaLabel1,asdaArrow1);
        Arrow TodaArrow1 = new Arrow(todaEnd1,todaLength1,todaStart1,todaLabel1,todaArrow1);
        //left logical runway
        setUpLine("TORA","Left",physicalRunway,ToraArrow);
        setUpLine("LDA","Left",physicalRunway,LdaArrow);
        setUpLine("ASDA","Left",physicalRunway,AsdaArrow);
        setUpLine("TODA","Left",physicalRunway,TodaArrow);
        //right
        setUpLine("TORA","Right",physicalRunway,ToraArrow1);
        setUpLine("LDA","Right",physicalRunway,LdaArrow1 );
        setUpLine("ASDA","Right",physicalRunway,AsdaArrow1);
        setUpLine("TODA","Right",physicalRunway,TodaArrow1);
    }

    protected void setUpLine(String type,String LeftorRight, PhysicalRunway physicalRunway,Arrow arrow){
        LogicalRunway logicalRunway = null;
        double originalStartX;
        double originalEndX;
        double leftActualThresholdX = thresholdL.getLayoutX();
        double rightActualThresholdX = thresholdR.getLayoutX();
        double displacedThresholdLayoutX;
        Rectangle stopway;
        Rectangle clearway;
        //Arrow variables
        Line start = arrow.getStart();
        Line length = arrow.getLength();
        Line end = arrow.getEnd();
        Label label = arrow.getLabel();
        Polygon arrowHead = arrow.getArrowHead();

        if (LeftorRight.equals("Left")){
            logicalRunway = physicalRunway.getLogicalRunways().get(0);
            displacedThresholdLayoutX = displacedThresholdL.getLayoutX();
            stopway = stopwayR;
            clearway = clearwayR;
            originalStartX = leftActualThresholdX;
            originalEndX = rightActualThresholdX;
        }else {
            logicalRunway = physicalRunway.getLogicalRunways().get(1);
            displacedThresholdLayoutX = displacedThresholdR.getLayoutX();
            stopway = stopwayL;
            clearway = clearwayL;
            originalStartX = rightActualThresholdX;
            originalEndX = leftActualThresholdX;
        }

        //condition for defining start
        if (type.equals("LDA")){
            originalStartX = displacedThresholdLayoutX;
        }

        //condition for defining end
        if (LeftorRight.equals("Left")){
            if (type.equals("ASDA")){
                originalEndX = stopway.getLayoutX() + stopway.getWidth();
            }else if (type.equals("TODA")){
                originalEndX = clearway.getLayoutX() + clearway.getWidth();
            }
        }else {
            if (type.equals("ASDA")){
                originalEndX = stopway.getLayoutX();
            }else if (type.equals("TODA")){
                originalEndX = clearway.getLayoutX() ;
            }
        }

        double originalValue = oldAndNewValue(type,logicalRunway)[0];

        start.setLayoutX(originalStartX);
        end.setLayoutX(originalEndX);
        label.setText(" " + type + " = " + originalValue + "m " );
        if (LeftorRight.equals("Left")){
            length.setLayoutX(originalStartX);
        }else {
            length.setLayoutX(originalEndX);
        }

        double difference = Math.abs(originalEndX-originalStartX);

        length.setEndX(difference);
        double labelLayout = getLabelLayout(LeftorRight.equals("Left")? start: end,length,label);
        label.setLayoutX(labelLayout);
        arrowHead.setLayoutX(end.getLayoutX());
    }

    protected void setUpScale(LogicalRunway logRunway){
        double tora = logRunway.getTora();
        int scaleRange = Utility.getScaleRange(tora);
        //setting up scale proportion
        scaleLength.setLayoutX(scaleLength.getLayoutX());
        scaleLength.setWidth(scaleRange*toraLength.getEndX()/tora);
        scaleStart.setLayoutX(scaleLength.getLayoutX());
        double length = scaleLength.getWidth();
        scaleStart.setWidth(length/3);
        scaleEnd.setLayoutX(scaleLength.getLayoutX()+length*2/3);
        scaleEnd.setWidth(length/3);

        //setting up scale labels
        scale500.setText(""+scaleRange/3);
        scale1000.setText(""+scaleRange*2/3);
        scale1500.setText(""+scaleRange);
        scale0.setLayoutX(scaleStart.getLayoutX()-scale0.getWidth());
        scale500.setLayoutX(scaleStart.getLayoutX() + length/3 - scale500.getWidth());
        scale1000.setLayoutX(scaleEnd.getLayoutX() -scale1000.getWidth());
        scale1500.setLayoutX(scaleEnd.getLayoutX() + scaleEnd.getWidth()-scale1500.getWidth());
        scaleUnit.setLayoutX(scaleEnd.getLayoutX() + scaleEnd.getWidth() + 10);
        scaleLabel.setLayoutX(scaleLength.getLayoutX() + (length - scaleLabel.getWidth())/2);
    }

    private double meterPerPx(){
        return runway.getWidth()/MainController.getPhysRunwaySelected().getLogicalRunways().get(0).getTora();
    }

    protected void setStopClearway(LogicalRunway logicalRunway,String leftOrRightWay){
        //Set Up  Variable
        double stopwayLength = logicalRunway.getStopway();
        double stopwayWidthPx = stopwayLength*runway.getWidth()/logicalRunway.getTora();
        double clearwayLength = logicalRunway.getClearway();
        double clearwayWidthPx = clearwayLength*runway.getWidth()/logicalRunway.getTora();
        double oriStopwayX;
        double oriClearwayX;
        Rectangle stopway;
        Rectangle clearway;

        //Set Up Variable based on left or right
        if (leftOrRightWay.equals("Left")){
            oriStopwayX = thresholdL.getLayoutX() - stopwayWidthPx;
            oriClearwayX = thresholdL.getLayoutX() - clearwayWidthPx;
            stopway = stopwayL;
            clearway = clearwayL;
        } else {
            oriStopwayX = thresholdR.getLayoutX();
            oriClearwayX = thresholdR.getLayoutX();
            stopway = stopwayR;
            clearway = clearwayR;
        }

        // Set Values for both clearway and stopway
        setStopClearwayValue(stopway,stopwayLength,stopwayWidthPx,oriStopwayX);
        setStopClearwayValue(clearway,clearwayLength,clearwayWidthPx,oriClearwayX);
    }

    protected void setStopClearwayValue(Rectangle way,double length, double widthPx, double oriWayX){
        if (length != 0 ){
            way.setWidth(widthPx);
        }else {
            way.setWidth(0);
        }
        way.setLayoutX(oriWayX);
    }

    protected void rotateRunway(){
        //variables
        AnchorPane topDownRunwayPane = getTopDownRunwayPane();
        Pane compass = getCompass();
        Label compassDegree = getCompassDegree();
        LogicalRunway lLogicalRunway = MainController.getPhysRunwaySelected().getLogicalRunways().get(0);
        double designatorInt = Integer.parseInt(lLogicalRunway.getDesignator().trim().replaceAll("[^0-9]",""));
        double runwayDirection = designatorInt * 10 - 90;
        double compassDirection = designatorInt * 10;
        //rotation
        RotateTransition rotate = new RotateTransition(Duration.millis(1500),topDownRunwayPane);
        RotateTransition rotate1 = new RotateTransition(Duration.millis(1500),compass);
        rotate.setToAngle(runwayDirection);
        rotate1.setToAngle(compassDirection);
        rotate.play();
        rotate1.play();
        topDownRunwayPane.setRotate(runwayDirection);
        compass.setRotate(compassDirection);
        compassDegree.setText(compassDirection + "°");
    }



    private void initializeRotate(AnchorPane pane, Pane compass){
        pane.setOnMousePressed(mouseEvent->{
            //mouse position before drag
            double mouseX = mouseEvent.getX();
            double mouseY = mouseEvent.getY();
            if (mouseEvent.isShiftDown()){
                pane.setCursor(Cursor.CLOSED_HAND);
                //mouse position before drag
                pane.setOnMouseDragged(draggedEvent -> {
                    Label compassDegree = getCompassDegree();
                    //set up xs and ys for calculation
                    double x2 = draggedEvent.getX();
                    double y2 = draggedEvent.getY();
                    double x3 = pane.getLayoutX() + pane.getWidth()/2;
                    double y3 = pane.getLayoutY() + pane.getHeight()/2;
                    double angle = Utility.getAngleBetween(mouseX,mouseY,x2,y2,x3,y3);
                    //get angles
                    double runwayAngle = (pane.getRotate() + angle )%360;
                    double compassAngle = (compass.getRotate() + angle) %360;
                    if (compassAngle < 0){
                        compassAngle += 360;
                    }
                    pane.setRotate(runwayAngle);
                    compass.setRotate(compassAngle);
                    compassDegree.setText(Math.round(compassAngle *10)/10.0 + "°");
                    draggedEvent.consume();
                    mouseEvent.consume();
                });
            }
        });
        pane.setOnMouseReleased( releasedEvent -> {
            pane.setCursor(Cursor.DEFAULT);
            pane.setOnMouseDragged(null);
        });
    }

    public void initializeCompass(){
        getCompass().setRotate(90);
        getCompassDegree().setText(90.0 + "°");

    }

    //getter
    public AnchorPane getTopDownRunwayPane() {
        return topDownRunwayPane;
    }

    public Pane getCompass() {
        return compass;
    }

    public Label getCompassDegree() {
        return compassDegree;
    }

    public Pane getDragPane() {
        return dragPane;
    }

    private void initLabelFlip(){
        getTopDownRunwayPane().rotateProperty().addListener((observable, oldValue, newValue) -> {
            double paneRotate = newValue.doubleValue();
            System.out.println(paneRotate);
            if (paneRotate < -275 || paneRotate > 275) {
                setLabelsAngle(0);
            }
            else if (paneRotate >95 || paneRotate < -95){
                setLabelsAngle(180);
            }else {
                setLabelsAngle(0);
            }
        });

    }

    private void setLabelsAngle(double d){
        toraLabel.setRotate(d);
        toraLabel1.setRotate(d);
        todaLabel.setRotate(d);
        todaLabel1.setRotate(d);
        ldaLabel.setRotate(d);
        ldaLabel1.setRotate(d);
        asdaLabel.setRotate(d);
        asdaLabel1.setRotate(d);
        ldaOtherLineLabel.setRotate(d);
        toraOtherLineLabel.setRotate(d);
        ldaOtherLineLabel1.setRotate(d);
        toraOtherLineLabel1.setRotate(d);
        minCGLabel.setRotate(d);
        maxCGLabel.setRotate(d);
    }
}
