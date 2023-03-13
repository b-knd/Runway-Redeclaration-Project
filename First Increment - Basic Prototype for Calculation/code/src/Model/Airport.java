package Model;

import javafx.collections.ObservableList;

public class Airport {
    private String name;
    private ObservableList<PhysicalRunway> physicalRunways;

    public Airport(String name, ObservableList<PhysicalRunway> physicalRunways) {
        this.name = name;
        this.physicalRunways = physicalRunways;
    }


    @Override
    public String toString() {
        return "Airport{" +
                "name='" + name + '\'' +
                "\n physicalRunways=" + physicalRunways +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObservableList<PhysicalRunway> getPhysicalRunways() {
        return physicalRunways;
    }

    public void setPhysicalRunways(ObservableList<PhysicalRunway> physicalRunways) {
        this.physicalRunways = physicalRunways;
    }
}
