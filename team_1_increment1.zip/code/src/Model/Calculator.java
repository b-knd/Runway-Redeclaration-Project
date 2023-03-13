package Model;

public class Calculator {
    public static final double resa = 240;
    public static final double blastProtection = 300;
    public static final double stripEnd = 60;
    public static final double minCGArea = 75;
    //empty constructor
    public Calculator() {}
    public static final double maxCGArea = 150;
    public static final String talo = "Take-Off Away Landing Over";
    public static final String ttlt = "Take-Off Towards Landing Towards";

    //testing only
    public static void main(String[] args) {
        LogicalRunway runway = new LogicalRunway("09l", 3902, 3902, 3902, 3595);
        Obstacle obstacle = new Obstacle("obs1", 12, 0, 0, 50);
        System.out.println(getCalculationBreakdownT(obstacle, runway));
    }

    public static double calcTora(Obstacle obstacle,LogicalRunway runway){
        String flightMethod = getFlightMethod(obstacle,runway);
        double originalTora = runway.getTora();
        double newTora;
        double distanceFromThreshold = obstacle.getDistFThreshold();
        double displacedThreshold = runway.getDisplacedThreshold();
        double displacedLandingThreshold = getDisplacedLandingThreshold(obstacle.getAlsTocs(),ttlt);

        // Calculate TORA
        if(flightMethod.equals(talo)){
            newTora = originalTora - blastProtection - distanceFromThreshold - displacedThreshold;
        }else {
            newTora = distanceFromThreshold + displacedThreshold - displacedLandingThreshold;
        }
        runway.setNewTora(newTora);
        return newTora;
    }

    public static double calcLda(Obstacle obstacle,LogicalRunway runway){
        String flightMethod = getFlightMethod(obstacle,runway);
        double originalLda = runway.getLda();
        double distanceFromThreshold = obstacle.getDistFThreshold();
        double newLda;
        double displacedLandingThreshold = getDisplacedLandingThreshold(obstacle.getAlsTocs(),talo);

        // Calculate LDA
        if (flightMethod.equals(talo)){
            newLda = originalLda - distanceFromThreshold - displacedLandingThreshold;
        }else {
            newLda = distanceFromThreshold - stripEnd - resa;
        }
        runway.setNewLda(newLda);
        return newLda;
    }

    public static double calcAsda(Obstacle obstacle, LogicalRunway runway){
        String flightMethod = getFlightMethod(obstacle,runway);
        double newAsda;
        double newTora = runway.getNewTora();
        double stopway = runway.getStopway();

        //Calculate ASDA
        if (flightMethod.equals(talo)){
            newAsda =  newTora + stopway;
        }else {
            newAsda = newTora;
        }
        runway.setNewAsda(newAsda);
        return newAsda;
    }

    public static double calcToda(Obstacle obstacle, LogicalRunway runway){
        String flightMethod = getFlightMethod(obstacle,runway);
        double newToda;
        double newTora = runway.getNewTora();
        double clearway = runway.getClearway();

        // Calculate TODA
        if (flightMethod.equals(talo)){
            newToda =  newTora + clearway;
        }else {
            newToda = newTora;
        }
        runway.setNewToda(newToda);
        return newToda;
    }

    // For both flight method TTLT(Takeoff Towards Landing Towards) and TALO(Takeoff Away Landing Over),
    // If alsTocs(height of obstacle * 50) is less than RESA, displaced threshold will be (resa + strip) end otherwise it will be (alsTocs + strip end).
    // Only if the flight method is TALO(Takeoff Away Landing Over),
    // When either (resa + strip) or (alsTocs + strip end) is less than blast protection value, we need to add blast protection value into Displaced Landing Threshold.
    public static double getDisplacedLandingThreshold(double alsTocs, String flightMethod){
        double displacedLandingThreshold;
        if (alsTocs < resa){
            displacedLandingThreshold = resa + stripEnd;
        }else {
            displacedLandingThreshold = alsTocs + stripEnd;
        }
        if (displacedLandingThreshold <= blastProtection && flightMethod.equals(talo)){
            displacedLandingThreshold += blastProtection;
        }
        return displacedLandingThreshold;
    }

    //check if the obstacle is within the strip end, or minimum clear graded area
    //the declaration needs to be redeclare if true.
    public static boolean needRedeclare(Obstacle obstacle, LogicalRunway logicalRunway){
        boolean withinStripEnd = obstacle.getDistFThreshold() <= logicalRunway.getTora() + stripEnd && obstacle.getDistFThreshold() >= -stripEnd;
        boolean withinCentreline = obstacle.getDistFCent() <= minCGArea && obstacle.getDistFCent() >= -minCGArea;
        return withinStripEnd && withinCentreline;
    }

    public static String getFlightMethod(Obstacle obstacle, LogicalRunway logicalRunway){
        String flightMethod;
        if (obstacle.getDistFThreshold() < logicalRunway.getTora()/2){
            flightMethod = talo;
        }else {
            flightMethod = ttlt;
        }
        return flightMethod;
    }

    public static int ldaBreakdownChoice(Obstacle obstacle){
        int choice;
        double alsTocs = obstacle.getAlsTocs();
        if (resa > obstacle.getAlsTocs()){
            choice = 1;
        } else if (blastProtection >= alsTocs + stripEnd) {
            choice = 2;
        } else {
            choice = 3;
        }
        return choice;
    }

    public static int toraBreakdownChoice(Obstacle obstacle){
        int choice;
        if (resa > obstacle.getAlsTocs()){
            choice = 1;
        }else {
            choice = 2;
        }
        return choice;
    }

    public static String getCalculationBreakdownT(Obstacle obstacle,LogicalRunway runway){
        int ldaOrToraChoice;
        String result = "";
        String flightMethod = getFlightMethod(obstacle,runway);
        performCalc(obstacle,runway);

        //Runway variables
        double originalTora = runway.getTora();
        double newTora = runway.getNewTora();
        double originalLda = runway.getLda();
        double newLda = runway.getNewLda();
        double newAsda = runway.getNewAsda();
        double newToda = runway.getNewToda();
        double displacedThreshold = runway.getDisplacedThreshold();
        double stopway = runway.getStopway();
        double clearway = runway.getClearway();

        //Obstacle variables
        double distanceFromThreshold = obstacle.getDistFThreshold();
        double obstacleHeight = obstacle.getHeight();
        double slopeRaito = Obstacle.slopeRatio;

        //other variables
        String slopeCalculation =  obstacleHeight + "*" + slopeRaito;

        //Calculation Breakdown
        if (flightMethod.equals("Take-Off Away Landing Over")){
            ldaOrToraChoice = ldaBreakdownChoice(obstacle);
            result += "TORA = Original TORA - Blast Protection - Distance from Threshold - Displaced Threshold\n";
            result += "         = " + originalTora + " - " + blastProtection + " - " + distanceFromThreshold + " - " + displacedThreshold + "\n         = " + newTora + "\n\n";
            result += "ASDA = (R) TORA + STOPWAY\n";
            result += "         = " + newTora + " + " + stopway + "\n         = " + newAsda + "\n\n";
            result += "TODA = (R) TORA + CLEARWAY\n";
            result += "         = " + newTora + " + " + clearway + "\n         = " + newToda + "\n\n";
            if (ldaOrToraChoice == 1){
                result += "LDA  = Original LDA - Distance from threshold - Strip End - RESA\n";
                result += "        = " + originalLda + " - " + distanceFromThreshold + " - " + stripEnd + " - " + resa + "\n        = " + newLda + "\n\n";
            }
            else if (ldaOrToraChoice == 2){
                result += "LDA  = Original LDA - Distance from threshold -Strip End - Slope Calculation - Blast Protection\n";
                result += "        = " + originalLda + " - " + distanceFromThreshold + " - " + stripEnd + " - " + slopeCalculation + " - " + blastProtection +"\n        = " + newLda + "\n\n";
            }
            else {
                result += "LDA  = Original LDA - Distance from threshold - Strip End - Slope Calculation\n";
                result += "        = " + originalLda + " - " + distanceFromThreshold + " - " + stripEnd + " - " + slopeCalculation + "\n        = " + newLda + "\n\n";
            }
        }
        else {
            ldaOrToraChoice = toraBreakdownChoice(obstacle);
            if (ldaOrToraChoice == 1){
                result += "TORA = Distance from threshold + Displaced Threshold - RESA - Strip End\n";
                result += "         = " + distanceFromThreshold + " + " + displacedThreshold + " - " + resa + " - " + " - " + stripEnd + "\n         = " + newTora + "\n";
            }
            else{
                result += "TORA =  Distance from threshold + Displaced Threshold - Slope Calculation - Strip End\n";
                result += "         = " + distanceFromThreshold + " + " + displacedThreshold + " - " + slopeCalculation + " - " + stripEnd + "\n         = " + newTora + "\n\n";
            }
            result += "ASDA = (R) TORA\n";
            result += "         = " + newAsda + "\n\n";
            result += "TODA = (R) TORA\n";
            result += "         = " + newToda + "\n\n";
            result += "LDA   = Distance from Threshold - Strip End - RESA\n";
            result += "         = " + distanceFromThreshold + " - " + stripEnd + " - " + resa + "\n         = " + newLda + "\n\n";
        }
        result += "\n";
        return result;
    }

    public static void performCalc(Obstacle obstacle, LogicalRunway runway){
        calcTora(obstacle, runway);
        calcLda(obstacle, runway);
        calcToda(obstacle, runway);
        calcAsda(obstacle, runway);
    }

}