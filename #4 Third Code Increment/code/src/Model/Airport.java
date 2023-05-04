package Model;

import javafx.collections.ObservableList;

public class Airport {
    private String name;
    private String manager;
    private String ID;
    private ObservableList<PhysicalRunway> physicalRunways;

    public Airport(String ID, String name, ObservableList<PhysicalRunway> physicalRunways, String manager) {
        this.ID = ID;
        this.name = name;
        this.physicalRunways = physicalRunways;
        this.manager = manager;
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

    public String getManager() {
        return manager;
    }

    public String getID() {
        return ID;
    }

    public void setManager(String name){
        this.manager = name;
    }
}
