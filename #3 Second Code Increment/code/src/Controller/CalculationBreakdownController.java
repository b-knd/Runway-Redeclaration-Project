package Controller;

import Model.Calculator;
import Model.LogicalRunway;
import Model.Obstacle;
import Model.PhysicalRunway;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CalculationBreakdownController implements Initializable {
    @FXML
    private Label leftDesignator;
    @FXML
    private Label rightDesignator;
    @FXML
    private Label resa;
    @FXML
    private Label stripEnd;
    @FXML
    private Label blastProtection;
    @FXML
    private Label ldaBreakdown;
    @FXML
    private Label toraBreakdown;
    @FXML
    private Label todaBreakdown;
    @FXML
    private Label asdaBreakdown;
    @FXML
    private Label ldaBreakdown1;
    @FXML
    private Label toraBreakdown1;
    @FXML
    private Label todaBreakdown1;
    @FXML
    private Label asdaBreakdown1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PhysicalRunway runway = MainController.getPhysRunwaySelected();
        Obstacle obstacle = MainController.getObstacleSelected();

        leftDesignator.setText(runway.getLogicalRunways().get(0).getDesignator());
        rightDesignator.setText(runway.getLogicalRunways().get(1).getDesignator());

        resa.setText("=  "+PhysicalRunway.getResa()+"  m");
        stripEnd.setText("=  "+ PhysicalRunway.getStripEnd()+"  m");
        blastProtection.setText("=  "+PhysicalRunway.getBlastProtection()+"  m");


        if(MainController.needRedeclare()){
            LogicalRunway logRunway1 = runway.getLogicalRunways().get(0);
            LogicalRunway logRunway2 = runway.getLogicalRunways().get(1);
            toraBreakdown.setText(Calculator.toraBreakdown(obstacle, logRunway1));
            todaBreakdown.setText(Calculator.todaBreakdown(obstacle, logRunway1));
            ldaBreakdown.setText(Calculator.ldaBreakdown(obstacle, logRunway1));
            asdaBreakdown.setText(Calculator.asdaBreakdown(obstacle, logRunway1));

            Obstacle obstacle1 = MainController.getObstacleSelected();
            obstacle1.setDistFThreshold(obstacle.getDistFThreshold());
            obstacle1.setDistFThreshold(Calculator.getOppositeDistFThrehold(obstacle,runway));
            toraBreakdown1.setText(Calculator.toraBreakdown(obstacle1, logRunway2));
            todaBreakdown1.setText(Calculator.todaBreakdown(obstacle1, logRunway2));
            ldaBreakdown1.setText(Calculator.ldaBreakdown(obstacle1, logRunway2));
            asdaBreakdown1.setText(Calculator.asdaBreakdown(obstacle1, logRunway2));
        } else{
            String s = """
                    No redeclaration needed, no calculation performed.

                         Original runway parameters can be used.""";
            ldaBreakdown.setText(s);
            toraBreakdown.setText(s);
            todaBreakdown.setText(s);
            asdaBreakdown.setText(s);
            ldaBreakdown1.setText(s);
            toraBreakdown1.setText(s);
            todaBreakdown1.setText(s);
            asdaBreakdown1.setText(s);
        }
    }

}
