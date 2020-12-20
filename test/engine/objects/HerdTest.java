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
        testHerd = new Herd(herdParams);
        assertNull(testHerd.makeLove());
        testHerd.addAnimal(testAnimals[0]);
        assertNull(testHerd.makeLove());
        testHerd.addAnimal(testAnimals[1]);
        testAnimals[0].setEnergy(1);
        assertNull(testHerd.makeLove());
        testAnimals[0].setEnergy(200);
        assertNotNull(testHerd.makeLove());
    }

    @Test
    public void getStrongestTest() {
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
        testAnimals[0].setEnergy(300);
        testAnimals[1].setEnergy(300);
        Animal[] couple = testHerd.getStrongestCouple();
        if (couple[0] == testAnimals[0]) {
            assertEquals(testHerd.getStrongestCouple()[0], testAnimals[0]);
            assertEquals(testHerd.getStrongestCouple()[1], testAnimals[1]);
        } else {
            assertEquals(testHerd.getStrongestCouple()[0], testAnimals[1]);
            assertEquals(testHerd.getStrongestCouple()[1], testAnimals[0]);
        }
        testAnimals[1].setEnergy(400);
        testAnimals[2].setEnergy(500);
        assertEquals(testHerd.getStrongestCouple()[0], testAnimals[2]);
        assertEquals(testHerd.getStrongestCouple()[1], testAnimals[1]);
        testHerd = new Herd(herdParams);
        assertNull(testHerd.getStrongestCouple());
    }

    @Test
    public void getAnimalTest() {
        testHerd = new Herd(herdParams);
        assertNull(testHerd.getAnimal());
        testHerd.addAnimal(testAnimals[0]);
        assertEquals(testHerd.getAnimal(), testAnimals[0]);
    }
}
