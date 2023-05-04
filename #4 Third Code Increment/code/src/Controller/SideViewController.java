package Controller;

import Model.*;
import Model.Helper.Utility;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class SideViewController {


    @FXML
    private Pane dragPane;
    //physical runway
    @FXML
    private AnchorPane sideOnPane;
    @FXML
    private Rectangle phyRunway;
    @FXML
    private Label designatorL;
    @FXML
    private Label designatorR;
    @FXML
    private Label designatorL1;
    @FXML
    private Label designatorR1;
    @FXML
    private Line thresholdL;
    @FXML
    private Line thresholdR;
    @FXML
    private Line displacedThresholdL;
    @FXML
    private Line displacedThresholdR;
    //tora
    @FXML
    private Line toraStart;
    @FXML
    private Line toraEnd;
    @FXML
    private Line toraLength;
    @FXML
    private Label toraLabel;
    @FXML
    private Polygon toraArrow;
    @FXML
    private Line rToraStart;
    @FXML
    private Line rToraEnd;
    @FXML
    private Line rToraLength;
    @FXML
    private Label rToraLabel;
    @FXML
    private Polygon rToraArrow;

    //lda
    @FXML
    private Line ldaStart;
    @FXML
    private Line ldaEnd;
    @FXML
    private Line ldaLength;
    @FXML
    private Label ldaLabel;
    @FXML
    private Polygon ldaArrow;
    @FXML
    private Line rLdaStart;
    @FXML
    private Line rLdaEnd;
    @FXML
    private Line rLdaLength;
    @FXML
    private Label rLdaLabel;
    @FXML
    private Polygon rLdaArrow;

    //asda
    @FXML
    private Line asdaStart;
    @FXML
    private Line asdaEnd;
    @FXML
    private Line asdaLength;
    @FXML
    private Label asdaLabel;
    @FXML
    private Polygon asdaArrow;
    @FXML
    private Line rAsdaStart;
    @FXML
    private Line rAsdaEnd;
    @FXML
    private Line rAsdaLength;
    @FXML
    private Label rAsdaLabel;
    @FXML
    private Polygon rAsdaArrow;

    //toda
    @FXML
    private Line todaStart;
    @FXML
    private Line todaEnd;
    @FXML
    private Line todaLength;
    @FXML
    private Label todaLabel;
    @FXML
    private Polygon todaArrow;
    @FXML
    private Line rTodaStart;
    @FXML
    private Line rTodaEnd;
    @FXML
    private Line rTodaLength;
    @FXML
    private Label rTodaLabel;
    @FXML
    private Polygon rTodaArrow;

    //others
    @FXML
    private Line toraOtherLength;
    @FXML
    private Line toraOtherLength1;
    @FXML
    private Line ldaOtherLength;
    @FXML
    private Line ldaOtherLength1;
    @FXML
    private Label toraOtherLabel;
    @FXML
    private Label toraOtherLabel1;
    @FXML
    private Label ldaOtherLabel;
    @FXML
    private Label ldaOtherLabel1;
    @FXML
    private Line toraOtherStart;
    @FXML
    private Line ldaOtherStart;
    @FXML
    private Line ldaOtherEnd;
    @FXML
    private Line toraOtherEnd;
    @FXML
    private Polygon toraOtherArrow;
    @FXML
    private Polygon toraOtherArrow1;
    @FXML
    private Polygon ldaOtherArrow;
    @FXML
    private Polygon ldaOtherArrow1;

    //clearway and stopway
    @FXML
    private Rectangle clearwayL;
    @FXML
    private Rectangle clearwayR;
    @FXML
    private Rectangle stopwayL;
    @FXML
    private Rectangle stopwayR;

    //obstacles
    @FXML
    private Polygon tocsSlope;
    @FXML
    private Polygon alsSlope;

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
    private Rectangle stopwayLegend;
    @FXML
    private Rectangle clearwayLegend;
    @FXML
    private Rectangle obstacleLegend;
    @FXML
    private Rectangle displacedThresholdLegend;
    @FXML
    private Rectangle minCGArea;


    @FXML
    private void initialize() {
        initialiseVisibility();
        MainController.physRunwayItem.addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                PhysicalRunway runway = MainController.getPhysRunwaySelected();
                resetValues(runway);
            }

        });
        MainController.obstacleProperty.addListener(((ObservableValue<? extends Obstacle> observable, Obstacle oldValue, Obstacle newValue) -> {
            if(newValue != null){
                if(oldValue != null){
                    oldValue.setDistFThreshold(0);
                    oldValue.setDistFCent(0);
                }
                newValue.setDistFThreshold(MainController.disFromThreshold.get());
                setUpAlsTocs(newValue,MainController.getPhysRunwaySelected().getLogicalRunways().get(0));
            }
        }));
        MainController.disFromThreshold.addListener((observable, oldValue, newValue) -> {
            setUpAlsTocs(MainController.getObstacleSelected(),MainController.getPhysRunwaySelected().getLogicalRunways().get(0));
        });
        MainController.themeProperty.addListener((observable, oldValue, newValue) -> setVisualColor(newValue.intValue()));

        MainController.obstacleHeight.addListener((observable, oldValue, newValue) -> {setUpAlsTocs(MainController.getObstacleSelected(), MainController.getPhysRunwaySelected().getLogicalRunways().get(0));});
        MainController.valueChanged.addListener((observable, oldValue, newValue) -> {updateLabel(new ActionEvent());});
        Utility.initializeZoom(sideOnPane);
        Utility.initializeDrag(dragPane,getSideOnPane());
        if(MainController.beforeCalculation){
            if(MainController.getPhysRunwaySelected() != null){
                resetValues(MainController.getPhysRunwaySelected());
            }
            if(MainController.getObstacleSelected() != null){
                setUpAlsTocs(MainController.getObstacleSelected(),MainController.getPhysRunwaySelected().getLogicalRunways().get(0));
            }
        } else{
            updateLabel(new ActionEvent());
        }
        setVisualColor(1);
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
        stopwayLegend.setFill(stopway);
        clearwayLegend.setFill(clearway);
        obstacleLegend.setFill(obstacle);
        displacedThresholdLegend.setFill(displacedThreshold);

        minCGArea.setFill(gradedArea);

        stopwayL.setFill(stopway);
        stopwayR.setFill(stopway);

        clearwayL.setFill(clearway);
        clearwayR.setFill(clearway);

        alsSlope.setFill(obstacle);
        tocsSlope.setFill(obstacle);

        displacedThresholdL.setFill(displacedThreshold);
        displacedThresholdL.setStroke(displacedThreshold);
        displacedThresholdR.setFill(displacedThreshold);
        displacedThresholdR.setStroke(displacedThreshold);
    }

    @FXML
    private void initialiseVisibility(){
        alsSlope.setVisible(false);
        tocsSlope.setVisible(false);
        toraOtherLength.setVisible(false);
        toraOtherLength1.setVisible(false);
        ldaOtherLength.setVisible(false);
        ldaOtherLength1.setVisible(false);
        toraOtherLabel.setVisible(false);
        toraOtherLabel1.setVisible(false);
        ldaOtherLabel.setVisible(false);
        ldaOtherLabel1.setVisible(false);
        toraOtherStart.setVisible(false);
        ldaOtherStart.setVisible(false);
        ldaOtherEnd.setVisible(false);
        toraOtherEnd.setVisible(false);
        toraOtherArrow.setVisible(false);
        toraOtherArrow1.setVisible(false);
        ldaOtherArrow.setVisible(false);
        ldaOtherArrow1.setVisible(false);
    }

    @FXML
    private void updateLabel(ActionEvent event){
        //Selected variables in MainController
        PhysicalRunway selectedPhyRunway = MainController.getPhysRunwaySelected();
        LogicalRunway lLogicalRunway = selectedPhyRunway.getLogicalRunways().get(0);
        Obstacle selectedObstacle = MainController.getObstacleSelected();
        boolean needRedeclare = Calculator.needRedeclare(selectedObstacle, lLogicalRunway);

        //Set Up Arrows for the logical runway
        Arrow ToraArrow = new Arrow(toraStart,toraLength,toraEnd,toraLabel,toraArrow);
        Arrow LdaArrow = new Arrow(ldaStart,ldaLength,ldaEnd,ldaLabel,ldaArrow);
        Arrow AsdaArrow = new Arrow(asdaStart,asdaLength,asdaEnd,asdaLabel,asdaArrow);
        Arrow TodaArrow = new Arrow(todaStart,todaLength,todaEnd,todaLabel,todaArrow);
        Arrow RToraArrow = new Arrow(rToraStart,rToraLength,rToraEnd,rToraLabel,rToraArrow);
        Arrow RLdaArrow = new Arrow(rLdaStart,rLdaLength,rLdaEnd,rLdaLabel,rLdaArrow);
        Arrow RAsdaArrow = new Arrow(rAsdaStart,rAsdaLength,rAsdaEnd,rAsdaLabel,rAsdaArrow);
        Arrow RTodaArrow = new Arrow(rTodaStart,rTodaLength,rTodaEnd,rTodaLabel,rTodaArrow);

        if(needRedeclare){
            Calculator.performCalc(selectedObstacle,selectedPhyRunway);
            resetValues(selectedPhyRunway);
            setUpAlsTocs(selectedObstacle,lLogicalRunway);

            //Set Up the new Lines
            setNewLine("TORA","Left",selectedPhyRunway,selectedObstacle,ToraArrow);
            setNewLine("LDA","Left",selectedPhyRunway,selectedObstacle,LdaArrow);
            setNewLine("ASDA","Left",selectedPhyRunway,selectedObstacle,AsdaArrow);
            setNewLine("TODA","Left",selectedPhyRunway,selectedObstacle,TodaArrow);
            setNewLine("TORA","Right",selectedPhyRunway,selectedObstacle,RToraArrow);
            setNewLine("LDA","Right",selectedPhyRunway,selectedObstacle,RLdaArrow);
            setNewLine("ASDA","Right",selectedPhyRunway,selectedObstacle,RAsdaArrow);
            setNewLine("TODA","Right",selectedPhyRunway,selectedObstacle,RTodaArrow);
            //Set up OtherLines (Resa,BlastProtection...)
            boolean isTalo = Calculator.getFlightMethod(selectedObstacle,lLogicalRunway).equals(Calculator.talo);
            setToraOtherLine(true,isTalo, false, toraOtherLabel, toraOtherLength, toraOtherStart, toraOtherArrow);
            setLdaOtherLine(true,isTalo, false, ldaOtherLabel, ldaOtherLength, ldaOtherStart, ldaOtherArrow);
            setToraOtherLine(true,isTalo, true, toraOtherLabel1, toraOtherLength1, toraOtherEnd, toraOtherArrow1);
            setLdaOtherLine(true,isTalo, true, ldaOtherLabel1, ldaOtherLength1, ldaOtherEnd, ldaOtherArrow1);
        } else{
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
        changeColor(colour, RToraArrow);
        changeColor(colour, RTodaArrow);
        changeColor(colour, RAsdaArrow);
        changeColor(colour, RLdaArrow);
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

    //type is TORA, LDA ,TODA or ASDA
    private void setNewLine(String type,String LeftorRight, PhysicalRunway physicalRunway,Obstacle obstacle,Arrow arrow){
        LogicalRunway logicalRunway = null;
        if (LeftorRight.equals("Left")){
            logicalRunway = physicalRunway.getLogicalRunways().get(0);
        }else {
            logicalRunway = physicalRunway.getLogicalRunways().get(1);
        }
        //variables for calculation later
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
        //variables for flightMethod
        String flightMethod = Calculator.getFlightMethod(obstacle,logicalRunway);
        String talo = Calculator.talo;
        String ttlt = Calculator.ttlt;
        //Start setting the lines based on it is either left or right side of the runway
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
                //when flightMethod for right side is TALO
                start.setLayoutX(start.getLayoutX() - differenceInPx);
                length.setLayoutX(end.getLayoutX());
                length.setEndX(length.getEndX() - differenceInPx);
            } else if (flightMethod.equals(talo)) {
                //when flightMethod for right side is TTLT
                end.setLayoutX(end.getLayoutX() + differenceInPx);
                length.setLayoutX(end.getLayoutX());
                length.setEndX(length.getEndX() - differenceInPx);
            }
        }
        label.setText(" " + type +" = " + newValue + "m ");
        labelLayout = getLabelLayout(LeftorRight.equals("Left")? start: end,length,label);
        label.setLayoutX(labelLayout);
        arrowHead.setLayoutX(end.getLayoutX());
    }

    private void setLdaOtherLine(boolean bool, boolean isTALO, boolean lower, Label label, Line length, Line startEnd, Polygon arrow){
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
            //some tweak here
            if(isTALO){
                if(lower){
                    reference = rLdaEnd;
                } else{
                    reference = ldaStart;
                }
            } else {
                if(lower){
                    reference = rLdaStart;
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
            //some tweak here
            double changePx = getNumberOfPx(change,MainController.getPhysRunwaySelected().getLogicalRunways().get(0));
            if(!isTALO){
                startEnd.setLayoutX(reference.getLayoutX()+(changePx));
                length.setLayoutX(reference.getLayoutX());
                length.setEndX(startEnd.getLayoutX()-reference.getLayoutX());
                label.setLayoutX(reference.getLayoutX()-(reference.getLayoutX()-startEnd.getLayoutX())/2-label.getWidth()/2);
            } else{
                startEnd.setLayoutX(reference.getLayoutX()-(changePx));
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

    private void setToraOtherLine(boolean bool, boolean isTALO, boolean lower, Label label, Line length, Line startEnd, Polygon arrow){
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
                    reference = rToraEnd;
                } else{
                    reference = toraStart;
                }
            } else{
                if(lower) {
                    reference = rTodaStart;
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
            double changePx = getNumberOfPx(change,MainController.getPhysRunwaySelected().getLogicalRunways().get(0));
            if(!isTALO){
                startEnd.setLayoutX(reference.getLayoutX()+(changePx));
                length.setLayoutX(reference.getLayoutX());
                length.setEndX(startEnd.getLayoutX()-reference.getLayoutX());
                label.setLayoutX(reference.getLayoutX()-(reference.getLayoutX()-startEnd.getLayoutX())/2-label.getWidth()/2);
            } else{
                startEnd.setLayoutX(reference.getLayoutX()-(changePx));
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
    private void resetValues(PhysicalRunway physicalRunway){
        LogicalRunway lLogicalRunway = physicalRunway.getLogicalRunways().get(0);
        setUpPhyRunway(physicalRunway,lLogicalRunway);
        setUpStopwayAndClearway(physicalRunway,lLogicalRunway);
        setUpLogicalRunway(physicalRunway);
        initialiseVisibility();
        setUpScale(lLogicalRunway);
    }
    private void setUpLogicalRunway(PhysicalRunway physicalRunway){
        //left
        Arrow ToraArrow = new Arrow(toraStart,toraLength,toraEnd,toraLabel,toraArrow);
        Arrow LdaArrow = new Arrow(ldaStart,ldaLength,ldaEnd,ldaLabel,ldaArrow);
        Arrow AsdaArrow = new Arrow(asdaStart,asdaLength,asdaEnd,asdaLabel,asdaArrow);
        Arrow TodaArrow = new Arrow(todaStart,todaLength,todaEnd,todaLabel,todaArrow);
        setUpLine("TORA","Left",physicalRunway,ToraArrow);
        setUpLine("LDA","Left",physicalRunway,LdaArrow);
        setUpLine("ASDA","Left",physicalRunway,AsdaArrow);
        setUpLine("TODA","Left",physicalRunway,TodaArrow);
        //right
        Arrow RToraArrow = new Arrow(rToraStart,rToraLength,rToraEnd,rToraLabel,rToraArrow);
        Arrow RLdaArrow = new Arrow(rLdaStart,rLdaLength,rLdaEnd,rLdaLabel,rLdaArrow);
        Arrow RAsdaArrow = new Arrow(rAsdaStart,rAsdaLength,rAsdaEnd,rAsdaLabel,rAsdaArrow);
        Arrow RTodaArrow = new Arrow(rTodaStart,rTodaLength,rTodaEnd,rTodaLabel,rTodaArrow);
        setUpLine("TORA","Right",physicalRunway,RToraArrow);
        setUpLine("LDA","Right",physicalRunway,RLdaArrow);
        setUpLine("ASDA","Right",physicalRunway,RAsdaArrow);
        setUpLine("TODA","Right",physicalRunway,RTodaArrow);
    }

    private void setUpLine(String type,String LeftorRight, PhysicalRunway physicalRunway,Arrow arrow){
        //initiate variables
        double originalStartX;
        double originalEndX;
        double leftActualThresholdX = thresholdL.getLayoutX();
        double rightActualThresholdX = thresholdR.getLayoutX();
        double displacedThresholdLayoutX;
        Rectangle stopway;
        Rectangle clearway;
        LogicalRunway logicalRunway = null;

        //Arrow variables
        Line start = arrow.getStart();
        Line length = arrow.getLength();
        Line end = arrow.getEnd();
        Label label = arrow.getLabel();
        Polygon arrowHead = arrow.getArrowHead();

        //default condition for all arrow
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

        // extra conditions for defining start
        if (type.equals("LDA")){
            originalStartX = displacedThresholdLayoutX;
        }

        //extra conditions for defining end
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

        //get which value we are getting (TORA/LDA/ASDA/TODA)
        double originalValue = oldAndNewValue(type,logicalRunway)[0];

        start.setLayoutX(originalStartX);
        end.setLayoutX(originalEndX);
        label.setText(" " + type +" = " + originalValue + "m ");
        if (LeftorRight.equals("Left")){
            length.setLayoutX(originalStartX);
        }else {
            length.setLayoutX(originalEndX);
        }
        double difference = Math.abs(originalEndX-originalStartX);
        length.setEndX(difference);
        arrowHead.setLayoutX(end.getLayoutX());
        double labelLayout = getLabelLayout(LeftorRight.equals("Left")? start: end,length,label);
        label.setLayoutX(labelLayout);
    }

    private double[] oldAndNewValue(String type, LogicalRunway logicalRunway){
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

    private void setUpPhyRunway(PhysicalRunway physicalRunway, LogicalRunway selectedLogRunway){
        LogicalRunway lLogicalRunway = physicalRunway.getLogicalRunways().get(0);
        LogicalRunway rLogicalRunway = physicalRunway.getLogicalRunways().get(1);

        //Set Up Threshold
        thresholdL.setLayoutX(phyRunway.getLayoutX());
        thresholdR.setLayoutX(phyRunway.getLayoutX()+phyRunway.getWidth());

        //Set Up Designator
        String lDesignator = lLogicalRunway.getDesignator();
        String rDesignator = rLogicalRunway.getDesignator();
        //Set Up DisplacedThreshold
        double lDisplacedThreshold = lLogicalRunway.getDisplacedThreshold();
        double rDisplacedThreshold = rLogicalRunway.getDisplacedThreshold();
        double lDisplacedThresholdX;
        double rDisplacedThresholdX;

        //change in designator and displacedThreshold as logical runway changes
         designatorL.setText(lDesignator.substring(0, lDesignator.length()-1));
         designatorR.setText(rDesignator.substring(0, rDesignator.length()-1));
         designatorL1.setText(lDesignator.substring(lDesignator.length()-1));
         designatorR1.setText(rDesignator.substring(rDesignator.length()-1));
         lDisplacedThresholdX = thresholdL.getLayoutX() + getNumberOfPx(lDisplacedThreshold,lLogicalRunway);
         rDisplacedThresholdX = thresholdR.getLayoutX() - getNumberOfPx(rDisplacedThreshold,rLogicalRunway);

        displacedThresholdL.setLayoutX(lDisplacedThresholdX);
        displacedThresholdR.setLayoutX(rDisplacedThresholdX);
    }

    protected void setUpStopwayAndClearway(PhysicalRunway physicalRunway, LogicalRunway selectedLogRunway){
        LogicalRunway lLogicalRunway = physicalRunway.getLogicalRunways().get(0);
        LogicalRunway rLogicalRunway = physicalRunway.getLogicalRunways().get(1);
        setStopClearway(lLogicalRunway,"Right");
        setStopClearway(rLogicalRunway,"Left");
    }

    private void setStopClearway(LogicalRunway logicalRunway,String leftOrRightWay){
        //Set Up  Variable
        double stopwayLength = logicalRunway.getStopway();
        double stopwayWidthPx = getNumberOfPx(stopwayLength,logicalRunway);
        double clearwayLength = logicalRunway.getClearway();
        double clearwayWidthPx = getNumberOfPx(clearwayLength,logicalRunway);
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

    private void setStopClearwayValue(Rectangle way,double length, double widthPx, double oriWayX){
        if (length != 0 ){
            way.setWidth(widthPx);
        }else {
            way.setWidth(0);
        }
        way.setLayoutX(oriWayX);
    }

    private void setUpAlsTocs(Obstacle obstacle, LogicalRunway logicalRunway){
        double obsHeight = obstacle.getHeight();
        double slopeWidth = obstacle.getAlsTocs();
        double distanceFromThreshold = obstacle.getDistFThreshold();

        //Get Number of Pixel
        double slopeWidthPx = getNumberOfPx(slopeWidth,logicalRunway);
        double obsHeightPx = getNumberOfPx(obsHeight,logicalRunway);
        double distanceFromThresholdPx = getNumberOfPx(distanceFromThreshold,logicalRunway);

        Polygon slope;
        boolean usingAls = Calculator.getFlightMethod(obstacle,logicalRunway).equals(Calculator.talo);
        if (usingAls){
            tocsSlope.setVisible(false);
            alsSlope.setVisible(true);
            slope = alsSlope;
            slope.setLayoutX(displacedThresholdL.getLayoutX()+distanceFromThresholdPx);
            ObservableList<Double> points = slope.getPoints();
            points.set(3,-obsHeightPx*10);
            //points.set(3,-20d);
            points.set(0, points.get(4)+slopeWidthPx);
        }else {
            tocsSlope.setVisible(true);
            alsSlope.setVisible(false);
            slope = tocsSlope;
            slope.setLayoutX(displacedThresholdL.getLayoutX()+distanceFromThresholdPx);
            ObservableList<Double> points = slope.getPoints();
            points.set(3,-obsHeightPx*10);
            //points.set(3,-20d);
            points.set(0, points.get(4)-slopeWidthPx);
        }
    }

    private double getMeterPerPx(LogicalRunway logicalRunway){
        return logicalRunway.getTora()/ phyRunway.getWidth();
    }

    private double getNumberOfPx(double length,LogicalRunway logicalRunway){
        return length/getMeterPerPx(logicalRunway);
    }

    private double getLabelLayout(Line start,Line length,Label label){
        return start.getLayoutX() + (length.getEndX()/2 - label.getWidth()/2);
    }

    public AnchorPane getSideOnPane() {
        return sideOnPane;
    }

    public Pane getDragPane() {
        return dragPane;
    }
}
