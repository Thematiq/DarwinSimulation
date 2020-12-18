package engine.objects;

import engine.tools.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Grass class
 * @author Mateusz Praski
 */
public class GrassTest {
    private final Vector[] testVectors = {
            new Vector(1, 2),
            new Vector(2, 3),
            new Vector(3, 4),
            new Vector(4, 4)
    };

    @Test
    public void grassTest() {
        for (Vector v : testVectors) {
            assertEquals(new Grass(v).getPos(), v);
        }
    }
}
