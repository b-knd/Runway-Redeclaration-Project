package Controller;

import Model.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class SimultaneousViewController implements Initializable {
    public Rectangle minCGArea;
    public Rectangle clearwayR1;
    public Rectangle clearwayL1;
    public Rectangle stopwayR1;
    public Rectangle stopwayL1;
    public Rectangle runway1;
    public Line centreLine;
    
    
    public Label leftDesignator;  
    public Label rightDesignator;
    public Line displacedThresholdL1;
    public Line displacedThresholdR1;

    public Line gradedAreaLine;
    public Label asdaLabel1;
    public Label todaLabel1;
    public Label toraLabel1;
    public Label ldaLabel1;
    public Line toraOtherLineLength1;
    public Line toraOtherLineEnd;
    public Label toraOtherLineLabel1;
    public Line toraEnd1;
    public Line ldaStart1;
    public Line asdaStart1;
    public Line ldaEnd1;
    public Line asdaEnd1;
    public Line toraStart1;
    public Line todaStart1;
    public Line todaEnd1;
    public Line toraLength1;
    public Line ldaLength1;
    public Line asdaLength1;
    public Line todaLength1;
    public Line ldaOtherLineLength1;
    public Line ldaOtherLineEnd;
    public Label ldaOtherLineLabel1;
    public Line thresholdL1;
    public Line thresholdR1;
    public Rectangle obstacleBlock;
    public Polygon toraArrow1;
    public Polygon ldaArrow1;
    public Polygon asdaArrow1;
    public Polygon todaArrow1;
    public Polygon toraOtherArrow1;
    public Polygon ldaOtherArrow1;
    public AnchorPane sideOnPane1;
    public Rectangle runway;
    public Label designatorR;
    public Label designatorL;
    public Rectangle clearwayR;
    public Rectangle clearwayL;
    public Line displacedThresholdR;
    public Line displacedThresholdL;
    public Rectangle stopwayR;
    public Rectangle stopwayL;
    public Line thresholdL;
    public Line toraEnd;
    public Line thresholdR;
    public Line ldaStart;
    public Line asdaStart;
    public Line ldaEnd;
    public Line asdaEnd;
    public Line toraStart;
    public Line todaStart;
    public Line todaEnd;
    public Line toraLength;
    public Line ldaLength;
    public Line asdaLength;
    public Line todaLength;
    public Label ldaLabel;
    public Label toraLabel;
    public Label asdaLabel;
    public Label todaLabel;
    public Polygon tocsSlope;
    public Polygon alsSlope;
    public Polygon todaArrow;
    public Polygon asdaArrow;
    public Polygon ldaArrow;
    public Polygon toraArrow;
    public Label ldaOtherLabel;
    public Line ldaOtherLength;
    public Line ldaOtherStart;
    public Line toraOtherLength;
    public Line toraOtherStart;
    public Label toraOtherLabel;
    public Polygon ldaOtherArrow;
    public Polygon toraOtherArrow;
    public Rectangle scaleLength;
    public Rectangle scaleStart;
    public Rectangle scaleEnd;
    public Label scaleLabel;
    public Label scale0;
    public Label scale500;
    public Label scale1000;
    public Label scale1500;
    public Label scaleUnit;

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
            resetValues(MainController.getPhysRunwaySelected());
            if(MainController.getObstacleSelected() != null){
                relocateObstacle();
            }
        });
        //listener for different obstacle selection
        MainController.obstacleProperty.addListener((observable, oldValue, newValue) -> {
            if(oldValue != null){
                oldValue.setDistFThreshold(0);
                oldValue.setDistFCent(0);
            }
            newValue.setDistFThreshold(MainController.disFromThreshold.get());
            relocateObstacle();
        });
        MainController.obstacleProperty.addListener(((ObservableValue<? extends Obstacle> observable, Obstacle oldValue, Obstacle newValue) -> {
            if(oldValue != null){
                oldValue.setDistFThreshold(0);
                oldValue.setDistFCent(0);
            }
            newValue.setDistFThreshold(MainController.disFromThreshold.get());
            setUpAlsTocs(newValue,MainController.getPhysRunwaySelected().getLogicalRunways().get(0));
        }));
        MainController.disFromThreshold.addListener((observable, oldValue, newValue) -> {
            setUpAlsTocs(MainController.getObstacleSelected(),MainController.getPhysRunwaySelected().getLogicalRunways().get(0));
        });
        //listener for result change to update with the revised parameters
        MainController.dirFromCentre.addListener((observable, oldValue, newValue) -> relocateObstacle());
        MainController.disFromThreshold.addListener((observable, oldValue, newValue) -> relocateObstacle());
        MainController.disFromCentre.addListener((observable, oldValue, newValue) -> relocateObstacle());
        MainController.valueChanged.addListener((observable, oldValue, newValue) -> updateLabel());
        MainController.obstacleHeight.addListener((observable, oldValue, newValue) -> {setUpAlsTocs(MainController.getObstacleSelected(), MainController.getPhysRunwaySelected().getLogicalRunways().get(0));});
    }

    //function to update labels and line in top view
    public void updateLabel(){
        relocateObstacle();
        PhysicalRunway selectedPhyRunway = MainController.getPhysRunwaySelected();
        LogicalRunway llogRunway = selectedPhyRunway.getLogicalRunways().get(0);
        Obstacle obstacle = MainController.getObstacleSelected();
        if(Calculator.needRedeclare(obstacle, llogRunway)){
            Calculator.performCalc(obstacle,selectedPhyRunway);
            resetValues(selectedPhyRunway);
            //Setting up arrows for lines
            Arrow ToraArrow = new Arrow(toraStart,toraLength,toraEnd,toraLabel,toraArrow);
            Arrow LdaArrow = new Arrow(ldaStart,ldaLength,ldaEnd,ldaLabel,ldaArrow);
            Arrow AsdaArrow = new Arrow(asdaStart,asdaLength,asdaEnd,asdaLabel,asdaArrow);
            Arrow TodaArrow = new Arrow(todaStart,todaLength,todaEnd,todaLabel,todaArrow);
            Arrow ToraArrow1 = new Arrow(toraEnd1,toraLength1,toraStart1,toraLabel1,toraArrow1);
            Arrow LdaArrow1 = new Arrow(ldaEnd1,ldaLength1,ldaStart1,ldaLabel1,ldaArrow1);
            Arrow AsdaArrow1 = new Arrow(asdaEnd1,asdaLength1,asdaStart1,asdaLabel1,asdaArrow1);
            Arrow TodaArrow1 = new Arrow(todaEnd1,todaLength1,todaStart1,todaLabel1,todaArrow1);
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
            boolean needRedeclare = Calculator.needRedeclare(obstacle, llogRunway);
            boolean isTalo = Calculator.getFlightMethod(obstacle,llogRunway).equals(Calculator.talo);
            setToraOtherLine(needRedeclare,isTalo, false, toraOtherLabel, toraOtherLength, toraOtherStart, toraOtherArrow);
            setLdaOtherLine(needRedeclare,isTalo, false, ldaOtherLabel, ldaOtherLength, ldaOtherStart, ldaOtherArrow);
            setToraOtherLine(needRedeclare,isTalo, true, toraOtherLineLabel1, toraOtherLineLength1, toraOtherLineEnd, toraOtherArrow1);
            setLdaOtherLine(needRedeclare,isTalo, true, ldaOtherLineLabel1, ldaOtherLineLength1, ldaOtherLineEnd, ldaOtherArrow1);
        } else{
            //if not runway redeclaration is needed, simply reset the value
            resetValues(selectedPhyRunway);
        }
    }

    protected void resetValues(PhysicalRunway physicalRunway){
        //hide labelling for additional lines for reference
        toraOtherLineEnd.setVisible(false);
        toraOtherStart.setVisible(false);
        toraOtherLabel.setVisible(false);
        toraOtherLineLabel1.setVisible(false);
        toraOtherLineLength1.setVisible(false);
        toraOtherLength.setVisible(false);
        toraOtherArrow.setVisible(false);
        toraOtherArrow1.setVisible(false);

        ldaOtherLineEnd.setVisible(false);
        ldaOtherStart.setVisible(false);
        ldaOtherLabel.setVisible(false);
        ldaOtherLineLabel1.setVisible(false);
        ldaOtherLength.setVisible(false);
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
        thresholdR.setLayoutX(runway.getLayoutX() + runway.getWidth());
        thresholdL1.setLayoutX(runway1.getLayoutX());
        thresholdR1.setLayoutX(runway1.getLayoutX() + runway1.getWidth());

        //Set Up Designator
        String lDesignator = lLogicalRunway.getDesignator();
        String rDesignator = rLogicalRunway.getDesignator();
        //Set Up DisplacedThreshold
        double lDisplacedThreshold = lLogicalRunway.getDisplacedThreshold();
        double rDisplacedThreshold = rLogicalRunway.getDisplacedThreshold();
        double lDisplacedThresholdX;
        double rDisplacedThresholdX;


        leftDesignator.setText(lDesignator);
        rightDesignator.setText(rDesignator);
        designatorL.setText(lDesignator);
        designatorR.setText(rDesignator);
        lDisplacedThresholdX = thresholdL.getLayoutX() + getNumberOfPx(lDisplacedThreshold,lLogicalRunway);
        rDisplacedThresholdX = thresholdR.getLayoutX() - getNumberOfPx(rDisplacedThreshold,rLogicalRunway);

        displacedThresholdL.setLayoutX(lDisplacedThresholdX);
        displacedThresholdR.setLayoutX(rDisplacedThresholdX);
        displacedThresholdL1.setLayoutX(lDisplacedThresholdX);
        displacedThresholdR1.setLayoutX(rDisplacedThresholdX);
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
        labelLayout = getLabelLayout(LeftorRight.equals("Left")? start: end,length,label);
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
        int scaleRange = Miscellaneous.getScaleRange(tora);
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
        Rectangle stopway1;
        Rectangle clearway1;

        //Set Up Variable based on left or right
        if (leftOrRightWay.equals("Left")){
            oriStopwayX = thresholdL.getLayoutX() - stopwayWidthPx;
            oriClearwayX = thresholdL.getLayoutX() - clearwayWidthPx;
            stopway = stopwayL;
            clearway = clearwayL;
            stopway1 = stopwayL1;
            clearway1 = clearwayL1;
        } else {
            oriStopwayX = thresholdR.getLayoutX();
            oriClearwayX = thresholdR.getLayoutX();
            stopway = stopwayR;
            clearway = clearwayR;
            stopway1 = stopwayR1;
            clearway1 = clearwayR1;
        }

        // Set Values for both clearway and stopway
        setStopClearwayValue(stopway,stopwayLength,stopwayWidthPx,oriStopwayX);
        setStopClearwayValue(clearway,clearwayLength,clearwayWidthPx,oriClearwayX);
        setStopClearwayValue(stopway1 ,stopwayLength,stopwayWidthPx,oriStopwayX);
        setStopClearwayValue(clearway1,clearwayLength,clearwayWidthPx,oriClearwayX);
    }

    protected void setStopClearwayValue(Rectangle way,double length, double widthPx, double oriWayX){
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

}
