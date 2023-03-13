package Model;

import javafx.collections.ObservableList;

public class PhysicalRunway{
    private String name;
    private ObservableList<LogicalRunway> logicalRunways;

    @Override
    public String toString() {
        return "PhysicalRunway{" +
                "name='" + name + '\'' +
                "\nlogicalRunways=" + logicalRunways + '}';
    }

    public PhysicalRunway(String name, ObservableList<LogicalRunway> logicalRunways){
        this.name = name;
        this.logicalRunways = logicalRunways;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObservableList<LogicalRunway> getLogicalRunways() {
        return logicalRunways;
    }

    public void setLogicalRunways(ObservableList<LogicalRunway> logicalRunways) {
        this.logicalRunways = logicalRunways;
    }
}