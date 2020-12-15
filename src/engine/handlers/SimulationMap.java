package engine.handlers;

import engine.objects.Animal;
import engine.objects.Grass;
import engine.objects.Herd;
import engine.tools.Parameters;
import engine.tools.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SimulationMap implements IObserverPositionChanged {
    final Vector jungleBottomLeft;
    final Vector jungleTopRight;
    final Vector bottomLeft = new Vector(0, 0);
    final Vector topRight;

    private Random rand;
    private Map<Vector, Grass> bushes = new HashMap<>();
    private Herd[][] animals;

    public SimulationMap(Parameters params) {
        this.rand = new Random();
        this.topRight = new Vector(params.width-1, params.height-1);
        this.animals = new Herd[params.width][params.height];
        for(int x = 0; x < params.width; ++x) {
            for(int y = 0; y < params.height; ++y) {
                this.animals[x][y] = new Herd();
            }
        }

        int jungleWidth = (int)(params.width * params.jungleRatio);
        int jungleHeight = (int)(params.height * params.jungleRatio);

        Vector middle = new Vector(params.width / 2, params.height / 2);
        this.jungleBottomLeft = middle.add(new Vector(- jungleWidth, - jungleHeight));
        this.jungleTopRight = middle.add(new Vector(jungleWidth, jungleHeight));

        for(int i = 0; i < params.startingAnimals; ++i) {
            this.spawnAnimal();
        }
    }

    public void spawnGrass() {
        // Spawn inside jungle
        Vector pos;
        do {
            pos = Vector.randomVector(this.rand, this.jungleBottomLeft, this.jungleTopRight);
        } while(this.bushes.get(pos) != null);
        this.bushes.put(pos, new Grass(pos));
        // Spawn outside jungle
        do {
            pos = Vector.randomVector(this.rand, this.bottomLeft, this.topRight);
        } while(this.bushes.get(pos) != null && !this.isJungle(pos));
        this.bushes.put(pos, new Grass(pos));
    }

    public void spawnAnimal() {
        Vector pos;
        do {
            pos = Vector.randomVector(this.rand, this.bottomLeft, this.topRight);
        } while(this.animals[pos.x][pos.y].getAnimal() != null);
        this.animals[pos.x][pos.y].addAnimal(new Animal(pos));
    }

    public boolean isJungle(Vector pos) {
        return this.jungleBottomLeft.precedes(pos) && this.jungleTopRight.follows(pos);
    }

    public Animal animalAt(Vector pos) {
        return this.animals[pos.x][pos.y].getAnimal();
    }

    public Object objectAt(Vector pos) {
        Object ret = animalAt(pos);
        if (ret != null) {
            return ret;
        } else {
            return this.bushes.get(pos);
        }
    }

    public boolean isGrass(Vector pos) {
        return this.bushes.get(pos) != null;
    }

    public int[] jungleSize() {
        return new int[]{this.jungleBottomLeft.x, this.jungleBottomLeft.y,
                         this.jungleTopRight.x - this.jungleBottomLeft.x, this.jungleTopRight.y - this.jungleBottomLeft.y};
    }

    @Override
    public void positionChanged(Vector oldPos, Vector newPos, Animal caller) {

    }
}
