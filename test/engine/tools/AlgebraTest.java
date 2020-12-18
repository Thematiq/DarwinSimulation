package engine.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Algebra class
 * @author Mateusz Praski
 */
@SuppressWarnings("SuspiciousNameCombination")
public class AlgebraTest {
    private final Vector nullVector = new Vector(0, 0);
    private final Vector[] testVectors = {
            new Vector(10, 20),
            new Vector(20, 30),
            new Vector(30, 40),
            new Vector(40, 40)
    };

    @Test
    public void rotateVectorTest() {
        for (Vector test : testVectors) {
            assertEquals(Algebra.rotateVector(test, 0, nullVector), test);
            assertEquals(Algebra.rotateVector(test, 90, nullVector), new Vector(test.y, -test.x));
            assertEquals(Algebra.rotateVector(test, 180, nullVector), test.opposite());
            assertEquals(Algebra.rotateVector(test, 270, nullVector), new Vector(-test.y, test.x));
        }
    }
}
