package engine.objects;

import engine.tools.Parameters;
import engine.tools.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Herd class
 * @author Mateusz Praski
 */
public class HerdTest {
    private final Vector herdVector = new Vector(0,0);
    private final Parameters herdParams = new Parameters(
            10, 10, 100, 1,
            30, (float)0.5, 10
    );

    private Herd testHerd;
    private Animal[] testAnimals;


    @BeforeEach
    public void spawnHerd() {
        testAnimals = new Animal[5];
        testHerd = new Herd(herdParams);
        for (int i = 0; i < 5; ++i) {
            testAnimals[i] = new Animal(herdVector, herdParams);
            testHerd.addAnimal(testAnimals[i]);
        }
    }

    @Test
    public void eatGrassTest() {
        testAnimals[2].setEnergy(200);
        assertTrue(testHerd.eatGrass());
        assertEquals(testAnimals[0].getEnergy() , 100);
        assertEquals(testAnimals[1].getEnergy() , 100);
        assertEquals(testAnimals[2].getEnergy(), 200 + 30);
        assertEquals(testAnimals[3].getEnergy() , 100);
        assertEquals(testAnimals[4].getEnergy() , 100);

        testAnimals[2].setEnergy(200);
        testAnimals[3].setEnergy(200);
        assertTrue(testHerd.eatGrass());
        assertEquals(testAnimals[0].getEnergy() , 100);
        assertEquals(testAnimals[1].getEnergy() , 100);
        assertEquals(testAnimals[2].getEnergy(), 200 + 15);
        assertEquals(testAnimals[3].getEnergy(), 200 + 15);
        assertEquals(testAnimals[4].getEnergy() , 100);

        testHerd = new Herd(herdParams);
        assertFalse(testHerd.eatGrass());
    }

    @Test
    public void makeLoveTest() {

    }

    @Test
    public void getStrongestTest() {
        for (Animal a : testAnimals) {
            System.out.println(a.getEnergy());
        }
        System.out.println(testHerd.getStrongest().get(0).getEnergy());;
        assertEquals(testHerd.getStrongest().size(), 5);
        testAnimals[2].setEnergy(200);
        assertEquals(testHerd.getStrongest().size(), 1);
        assertEquals(testHerd.getStrongest().get(0), testAnimals[2]);
        testAnimals[3].setEnergy(200);
        assertEquals(testHerd.getStrongest().size(), 2);
        assertTrue(testHerd.getStrongest().contains(testAnimals[2]));
        assertTrue(testHerd.getStrongest().contains(testAnimals[3]));
    }

    @Test
    public void getStrongestCoupleTest() {

    }
}
