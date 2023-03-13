package Model;

public class Obstacle {
    private String name;
    private double height;
    private double width;
    private double distFCent;
    private double distFThreshold;
    public final static double slopeRatio = 50;

    public Obstacle(String name, double height, double width, double distFCent, double distFThreshold){
        this.name = name;
        this.height = height;
        this.width = width;
        this.distFCent = distFCent;
        //from the point of obstacle closest to the threshold we are calculating
        this.distFThreshold = distFThreshold;
    }

    @Override
    public String toString() {
        return "Obstacle{" +
                "name='" + name + '\'' +
                ", height=" + height +
                ", width=" + width +
                ", distFCent=" + distFCent +
                ", distFThreshold=" + distFThreshold +
                '}';
    }

    public double getAlsTocs() {
        return getHeight() * slopeRatio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistFCent() {
        return distFCent;
    }

    public void setDistFCent(double distFCent) {
        this.distFCent = distFCent;
    }

    public double getDistFThreshold() {
        return distFThreshold;
    }

    public void setDistFThreshold(double distFThreshold) {
        this.distFThreshold = distFThreshold;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }
}
