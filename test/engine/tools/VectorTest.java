package engine.tools;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Vector class
 * @author Mateusz Praski
 */
public class VectorTest {
    private final Vector[] testVectors = {
            new Vector(1, 2),
            new Vector(2, 3),
            new Vector(3, 4),
            new Vector(4, 4)
    };

    @Test
    public void oppositeTest() {
        Vector[] opposites = {
                new Vector(-1, -2),
                new Vector(-2, -3),
                new Vector(-3, -4),
                new Vector(-4, -4)
        };
        for (int i = 0; i < testVectors.length; ++i) {
            assertEquals(testVectors[i].opposite(), opposites[i]);
        }
    }

    @Test
    public void addTest() {
        Vector[] sums = {
                new Vector(3, 5),
                new Vector(5, 7),
                new Vector(7, 8)
        };
        for (int i = 0; i < testVectors.length-1; ++i) {
            assertEquals(testVectors[i].add(testVectors[i+1]), sums[i]);
        }
    }

    @Test
    public void multTest() {
        Vector[] mult = {
                new Vector(1, 2),
                new Vector(4, 6),
                new Vector(9, 12),
                new Vector(16, 16)
        };
        for (int i = 0; i < testVectors.length; ++i) {
            assertEquals(testVectors[i].mult(i+1), mult[i]);
        }
    }

    @Test
    public void precedesTest() {
        for (int i = 0; i < testVectors.length; ++i) {
            for (int j = i; j < testVectors.length; ++j) {
                assertTrue(testVectors[i].precedes(testVectors[j]));
            }
        }
        for (int i = testVectors.length-1; i > 0; --i) {
            for (int j = i-1; j >= 0; --j) {
                assertFalse(testVectors[i].precedes((testVectors[j])));
            }
        }
    }

    @Test
    public void followsTest() {
        for (int i = 0; i < testVectors.length-1; ++i) {
            for (int j = i+1; j < testVectors.length; ++j) {
                assertFalse(testVectors[i].follows(testVectors[j]));
            }
        }
        for (int i = testVectors.length-1; i >= 0; --i) {
            for (int j = i; j >= 0; --j) {
                assertTrue(testVectors[i].follows((testVectors[j])));
            }
        }
    }

    @Test
    public void wrapTest() {
        Vector boundaryVector = new Vector(2,2);
        Vector[] wrappedVector = {
                new Vector(1,2),
                new Vector(2, 0),
                new Vector(0, 1),
                new Vector(1, 1)
        };
        for (int i = 0; i < testVectors.length; ++i) {
            assertEquals(testVectors[i].wrap(boundaryVector),
                         wrappedVector[i]);
        }
    }

    @Test
    public void getNeighboursTest() {
        List<Vector> testList;
        for (Vector v : testVectors) {
            assertEquals(new ArrayList<Vector>(), v.getNeighbours(v, v));
        }
        for (Vector v : testVectors) {
            testList = v.getNeighbours(new Vector(0, 0), new Vector(10, 10));
            assertEquals(testList.size(), 8);
        }
        for (Vector v : testVectors) {
            testList = v.getNeighbours();
            assertEquals(testList.size(), 8);
        }
    }

    @Test
    public void toStringTest() {
        String[] answers = {
                "(1, 2)",
                "(2, 3)",
                "(3, 4)",
                "(4, 4)"
        };
        for (int i = 0; i < testVectors.length; ++i) {
            assertEquals(testVectors[i].toString(), answers[i]);
        }
    }
}
