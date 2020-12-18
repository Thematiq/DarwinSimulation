package engine.handlers;

import engine.objects.Animal;
import engine.tools.Parameters;
import engine.tools.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SimulationMap class
 * @author Mateusz Praski
 */
public class SimulationMapTest {
    private final Parameters params = new Parameters(10, 10, 10, 1, 10, (float) 0.2, 0);
    private SimulationMap testMap;

    @BeforeEach
    public void mapSpawner() {
        testMap = new SimulationMap(params);
    }

    @Test
    public void spawnGrassTest() {
        int rounds = Math.max(testMap.getMaxSteppeBushes(), testMap.getMaxJungleBushes());
        for (int i = 0; i < rounds; ++i) {
            testMap.spawnGrass();
        }
        assertEquals(testMap.getMaxJungleBushes(), testMap.getJungleBushes());
        assertEquals(testMap.getMaxSteppeBushes(), testMap.getSteppeBushes());
    }

    @Test
    public void addAnimalTest() {
        boolean contains;
        Vector[] testVectors = {
                new Vector(1, 1),
                new Vector(2, 2),
                new Vector(3, 3)
        };
        for (Vector v : testVectors) {
           testMap.addAnimal(new Animal(v, params));
        }
        assertEquals(testMap.getLivingAnimals(), 3);
        for (int x = 0; x < params.width; ++x) {
            for (int y = 0; y < params.height; ++y) {
                contains = false;
                for (Vector v : testVectors) {
                    if (v.equals(new Vector(x, y))) {
                        contains = true;
                        break;
                    }
                }
                if (contains) {
                    assertNotNull(testMap.animalAt(new Vector(x, y)));
                } else {
                    assertNull(testMap.animalAt(new Vector(x, y)));
                }
            }
        }
    }

    @Test
    public void eatGrassBasicTest() {

    }

    @Test
    public void eatGrassStrongestTest() {

    }

    @Test
    public void eatGrassMultipleStrongestTest() {

    }

    @Test
    public void makeLoveTest() {

    }
}
