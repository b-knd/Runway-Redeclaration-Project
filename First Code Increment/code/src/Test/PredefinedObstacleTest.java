import Controller.MainController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PredefinedObstacleTest {
    @Test
    public void predefinedObstacleLoaded(){
        String s = "src/Data/obstacles.xml";
        MainController controller = new MainController();
        assertDoesNotThrow(() -> controller.loadObstacles(s));
        int expected = 5;
        assertEquals(expected, controller.getObstacles().size());
        System.out.println("Successfully loaded predefined obstacles from XML file.");
        System.out.println("Expected number of obstacles matches system output: "+expected+"");
    }
}
