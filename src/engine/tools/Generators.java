package engine.tools;

import engine.handlers.SimulationMap;
import engine.objects.Animal;
import engine.objects.Grass;

import java.util.Random;

/**
 * Various generators used in a program
 * @author Mateusz Praski
 */
public class Generators {

    /**
     * Creates new Vector inside given rectangle
     * @param rn Java Random object
     * @param bottomLeft Bottom left corner of the rectangle
     * @param topRight Top right corner of the rectangle
     * @return new Vector from (0,0) to position inside the rectangle
     */
    public static Vector randomVector(Random rn, Vector bottomLeft, Vector topRight) {
        return new Vector(rn.nextInt(topRight.x - bottomLeft.x + 1) + bottomLeft.x,
                          rn.nextInt(topRight.y - bottomLeft.y + 1) + bottomLeft.y);
    }

    /**
     * Jungle grass generator
     * @return Grass on a jungle tile
     */
    public static Grass getJungleGrass(SimulationMap map, Random rand) {
        if (map.getMaxJungleBushes() == map.getJungleBushes()) {
            return null;
        }
        Vector pos;
        do {
            pos = Generators.randomVector(rand, map.getJungleBottomLeft(), map.getJungleTopRight());
        } while(map.isGrass(pos));
        return new Grass(pos);
    }

    /**
     * Steppe grass generator
     * @return Grass on a steppe tile
     */
    public static Grass getSteppeGrass(SimulationMap map, Random rand) {
        if (map.getMaxSteppeBushes() == map.getSteppeBushes()) {
            return null;
        }
        Vector pos;
        do {
            pos = Generators.randomVector(rand, map.getBottomLeft(), map.getTopRight());
        } while(map.isGrass(pos) || map.isJungle(pos));
        return new Grass(pos);
    }

    public static Animal getAnimal(SimulationMap map, Random rand) {
        Vector pos;
        do {
            pos = Generators.randomVector(rand, map.getBottomLeft(), map.getTopRight());
        } while(map.animalAt(pos) != null);
        return new Animal(pos, map.getParams());
    }
}
