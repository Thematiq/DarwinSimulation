package engine.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Orientation enum
 * @author Mateusz Praski
 */
public class OrientationTest {
    private final Orientation[] testOrients = {
            Orientation.NORTH,
            Orientation.NORTHEAST,
            Orientation.EAST,
            Orientation.SOUTHEAST,
            Orientation.SOUTH,
            Orientation.SOUTHWEST,
            Orientation.WEST,
            Orientation.NORTHWEST
    };

    @Test
    public void getIntTest() {
        for (int i = 0; i < testOrients.length; ++i) {
            assertEquals(testOrients[i].getInt(), i);
        }
    }

    @Test
    public void getDegreeTest() {
        for (int i = 0; i < testOrients.length; ++i) {
            assertEquals(testOrients[i].getDegree(), i * 45);
        }
    }

    @Test
    public void getUnitVector() {
        Vector[] answers = {
                new Vector(0, -1),
                new Vector(1, -1),
                new Vector(1, 0),
                new Vector(1, 1),
                new Vector(0, 1),
                new Vector(-1, 1),
                new Vector(-1, 0),
                new Vector(-1,-1)
        };
        for (int i = 0; i < testOrients.length; ++i) {
            assertEquals(testOrients[i].getUnitVector(), answers[i]);
        }
    }

    @Test
    public void getOrientTest() {
        for (int i = 0; i < 100; ++i) {
            assertEquals(Orientation.getOrient(i), testOrients[i % 8]);
        }
    }

    @Test
    public void translateOrientTestInt() {
        for (int i = 0; i < 100; ++i) {
            for (int j = 0; j < 100; ++j) {
                assertEquals(Orientation.translateOrient(testOrients[i % 8], j).getInt(), (i + j) % 8);
            }
        }
    }

    @Test
    public void translateOrientTestOrient() {
        for (int i = 0; i < 100; ++i) {
            for (int j = 0; j < 100; ++j) {
                assertEquals(Orientation.translateOrient(testOrients[i % 8], testOrients[j % 8]).getInt(),
                             (i + j) % 8);
            }
        }
    }
}
