package Model;

public class
LogicalRunway {
    private String designator;
    private double tora;
    private double toda;
    private double asda;
    private double lda;
    private double newTora;
    private double newToda;
    private double newAsda;
    private double newLda;

    //not sure if will be use


    public LogicalRunway(String designator, double tora, double toda, double asda, double lda) {
        this.designator = designator;
        this.tora = tora;
        this.toda = toda;
        this.asda = asda;
        this.lda = lda;
    }

    @Override
    public String toString() {
        return "\nLogicalRunway{" +
                "designator= '" + designator + '\'' +
                "\n tora=" + tora +
                ", toda=" + toda +
                ", asda=" + asda +
                ", lda=" + lda +
                "\n newTora=" + newTora +
                ", newToda=" + newToda +
                ", newAsda=" + newAsda +
                ", newLda=" + newLda +
                "}\n";
    }

    public String getDesignator() {
        return designator;
    }

    public void setDesignator(String designator) {
        this.designator = designator;
    }

    public double getTora() {
        return tora;
    }

    public void setTora(double tora) {
        this.tora = tora;
    }

    public double getToda() {
        return toda;
    }

    public void setToda(double toda) {
        this.toda = toda;
    }

    public double getAsda() {
        return asda;
    }

    public void setAsda(double asda) {
        this.asda = asda;
    }

    public double getLda() {
        return lda;
    }

    public void setLda(double lda) {
        this.lda = lda;
    }

    public double getClearway() {
        return getToda() - getTora();
    }

    public double getStopway() {
        return getAsda() - getTora();
    }

    public double getDisplacedThreshold() {
        return getTora() - getLda();
    }

    public double getNewTora() {
        return newTora;
    }

    public void setNewTora(double newTora) {
        this.newTora = newTora;
    }

    public double getNewToda() {
        return newToda;
    }

    public void setNewToda(double newToda) {
        this.newToda = newToda;
    }

    public double getNewAsda() {
        return newAsda;
    }

    public void setNewAsda(double newAsda) {
        this.newAsda = newAsda;
    }

    public double getNewLda() {
        return newLda;
    }

    public void setNewLda(double newLda) {
        this.newLda = newLda;
    }

}