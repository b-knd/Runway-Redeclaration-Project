package Model;

import javafx.collections.ObservableList;

public class PhysicalRunway{
    public static double blastProtection = 300;
    public static double resa = 240;
    public static double stripEnd = 60;
    public static final double minCGArea = 75;
    public static final double maxCGArea = 150;
    private String name;
    private ObservableList<LogicalRunway> logicalRunways;

    @Override
    public String toString() {
        return "PhysicalRunway{" +
                "name='" + name + '\'' +
                "\nlogicalRunways=" + logicalRunways + '}';
    }

    public PhysicalRunway(){};

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

    public static double getBlastProtection() {
        return blastProtection;
    }

    public static double getResa() {
        return resa;
    }

    public static double getStripEnd() {
        return stripEnd;
    }

    public void setLogicalRunways(ObservableList<LogicalRunway> logicalRunways) {
        this.logicalRunways = logicalRunways;
    }

    public static void setBlastProtection(double blastProtection) {
        PhysicalRunway.blastProtection = blastProtection;
    }

    public static void setResa(double resa) {
        PhysicalRunway.resa = resa;
    }

    public static void setStripEnd(double stripEnd) {
        PhysicalRunway.stripEnd = stripEnd;
    }
}