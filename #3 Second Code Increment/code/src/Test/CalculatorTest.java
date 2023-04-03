import Model.Calculator;
import Model.LogicalRunway;
import Model.Obstacle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    LogicalRunway lR09L = new LogicalRunway("09L",3902,3902,3902,3595);
    LogicalRunway lR27R = new LogicalRunway("27R",3884,3962,3884,3884);
    LogicalRunway lR27L = new LogicalRunway("27L", 3660, 3660, 3660, 3660);
    Obstacle obstacle1 = new Obstacle("obstacle1",12,0,0,-50);
    Obstacle obstacle2 = new Obstacle("obstacle2",12,0,0,3646);

    @Test
    @DisplayName("Test calculation of Tora for Takeoff Away")
    void calcTora_TA() {
        assertEquals(3345, Calculator.calcTora(obstacle1,lR09L));
    }

    @Test
    @DisplayName("Test calculation of Lda for Landing Over")
    void calcLda_LO() {
        assertEquals(2985,Calculator.calcLda(obstacle1,lR09L));
    }

    @Test
    @DisplayName("Test calculation of Tora for Takeoff Towards")
    void calcTora_TT() {
        assertEquals(2986,Calculator.calcTora(obstacle2,lR27R));
    }

    @Test
    @DisplayName("Test calculation of Lda for Landing Towards")
    void calcLda_LT() {
        assertEquals(2986,Calculator.calcTora(obstacle2,lR27R));
    }

    @Test
    @DisplayName("Test calculation of Asda for Takeoff Away Landing Over")
    void calcAsda_TALO() {
        Calculator.calcTora(obstacle1,lR09L);
        assertEquals(3345,Calculator.calcAsda(obstacle1,lR09L));
    }

    @Test
    @DisplayName("Test calculation of Toda for Takeoff Away Landing Over")
    void calcToda_TALO() {
        Calculator.calcTora(obstacle1,lR09L);
        assertEquals(3345,Calculator.calcToda(obstacle1,lR09L));
    }

    @Test
    @DisplayName("Test calculation of Asda for Takeoff Towards Landing Towards")
    void calcAsda_TTLT() {
        Calculator.calcTora(obstacle2,lR27R);
        assertEquals(2986,Calculator.calcAsda(obstacle2,lR27R));
    }

    @Test
    @DisplayName("Test calculation of Toda for Takeoff Towards Landing Towards")
    void calcToda_TTLT() {
        Calculator.calcTora(obstacle2,lR27R);
        assertEquals(2986,Calculator.calcToda(obstacle2,lR27R));
    }

    //GT = greater than
    //LT = less than
    @Test
    @DisplayName("Test if we needs to redeclare given obstacle and logicalRunway")
    void needRedeclare(){
        Obstacle obstacleGTCentreline = new Obstacle("obstacle1",10,10,200,50);
        Obstacle obstacleLTCentreline = new Obstacle("obstacle2",10,10,-200,50);
        Obstacle obstacleGTStripEnd = new Obstacle("obstacle3",10,10,50,10000);
        Obstacle obstacleLTStripEnd = new Obstacle("obstacle4",10,10,50,-370);
        assertFalse(Calculator.needRedeclare(obstacleGTStripEnd, lR09L));
        assertFalse(Calculator.needRedeclare(obstacleLTStripEnd, lR09L));
        assertFalse(Calculator.needRedeclare(obstacleGTCentreline, lR09L));
        assertFalse(Calculator.needRedeclare(obstacleLTCentreline, lR09L));
        assertTrue(Calculator.needRedeclare(obstacle1, lR09L));
    }

    @Test
    void ldaBreakdownChoice(){
        assertEquals(3, Calculator.ldaBreakdownChoice(obstacle1));
    }

    @Test
    void toraBreakdownChoice(){
        assertEquals(2, Calculator.toraBreakdownChoice(obstacle2));
        Obstacle obstacle3 = new Obstacle("obs3", 20, 0, 20, 3546);
        assertEquals(2, Calculator.toraBreakdownChoice(obstacle3));
    }

    @Test
    void getFlightMethod(){
        assertEquals(Calculator.talo,Calculator.getFlightMethod(obstacle1,lR09L));
        assertEquals(Calculator.ttlt,Calculator.getFlightMethod(obstacle2,lR27R));
    }

    @Test
    void getDisplacedLandingThreshold(){
        Obstacle alsTocsLTResa      = new Obstacle("alsTocsLTResa",2,50,0,50); //alstocs = 2*50=100 < 240 (resa)
        Obstacle alsTocsGTResa      = new Obstacle("alsTocsGTResa",5,50,0,50); //alstocs = 5*50=250 > 240 (resa)
        assertEquals(600,Calculator.getDisplacedLandingThreshold(alsTocsLTResa.getAlsTocs(),Calculator.talo));
        assertEquals(310,Calculator.getDisplacedLandingThreshold(alsTocsGTResa.getAlsTocs(),Calculator.talo));
    }
}
