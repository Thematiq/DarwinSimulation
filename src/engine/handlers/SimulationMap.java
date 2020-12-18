package engine.handlers;

import engine.objects.Animal;
import engine.objects.Grass;
import engine.objects.Herd;
import engine.observers.IObserverKilled;
import engine.observers.IObserverPositionChanged;
import engine.tools.Generators;
import engine.tools.Parameters;
import engine.tools.Vector;

import java.util.*;

/**
 * Class representing Simulation map
 * @author Mateusz Praski
 */
public class SimulationMap implements IObserverPositionChanged, IObserverKilled {
    private final Parameters params;
    private final Vector jungleBottomLeft;
    private final Vector jungleTopRight;
    private final Vector bottomLeft = new Vector(0, 0);
    private final Vector topRight;
    private final Random rand = new Random();
    private final Map<Vector, Grass> bushes = new HashMap<>();
    private final List<Animal> animalList = new ArrayList<>();
    private final Herd[][] herdsTable;
    private final int maxJungleBushes;
    private final int maxSteppeBushes;

    private int livingAnimals = 0;
    private int deadAnimals = 0;
    private int steppeBushes = 0;
    private int jungleBushes = 0;

    /**
     * Creates new map
     * @param params Simulation params
     */
    public SimulationMap(Parameters params) {
        this.params = params;
        this.topRight = new Vector(params.width-1, params.height-1);
        this.herdsTable = new Herd[params.width][params.height];
        for(int x = 0; x < params.width; ++x) {
            for(int y = 0; y < params.height; ++y) {
                this.herdsTable[x][y] = new Herd(this.params);
            }
        }

        int jungleWidth = (int)(params.width * params.jungleRatio);
        int jungleHeight = (int)(params.height * params.jungleRatio);

        Vector middle = new Vector(params.width / 2, params.height / 2);
        this.jungleBottomLeft = middle.add(new Vector(- (jungleWidth/2), - (jungleHeight/2)));
        this.jungleTopRight = middle.add(new Vector((jungleWidth/2)-1, (jungleHeight/2)-1));

        this.maxJungleBushes = (this.jungleTopRight.x - this.jungleBottomLeft.x + 1) * (this.jungleTopRight.y - this.jungleBottomLeft.y + 1);
        this.maxSteppeBushes = params.width * params.height - this.maxJungleBushes;

        for(int i = 0; i < params.startingAnimals; ++i) {
            this.spawnAnimal();
        }
    }

    /**
     * Adds Grass on a tile if its free and it isn't a null
     * @param a Grass
     */
    private void addGrass(Grass a) {
        if (a == null || this.bushes.get(a.getPos()) != null) {
            return;
        }
        if(isJungle(a.getPos())) {
            this.jungleBushes++;
        } else {
            this.steppeBushes++;
        }
        this.bushes.put(a.getPos(), a);
    }

    /**
     * Steppe grass generator
     * @return Grass on a steppe tile
     */
    private Grass getSteppeGrass() {
        if (this.maxSteppeBushes == this.steppeBushes) {
            return null;
        }
        Vector pos;
        do {
            pos = Generators.randomVector(this.rand, this.bottomLeft, this.topRight);
        } while(this.bushes.get(pos) != null || this.isJungle(pos));
        return new Grass(pos);
    }

    /**
     * Jungle grass generator
     * @return Grass on a jungle tile
     */
    private Grass getJungleGrass() {
        if (this.maxJungleBushes == this.jungleBushes) {
            return null;
        }
        Vector pos;
        do {
            pos = Generators.randomVector(this.rand, this.jungleBottomLeft, this.jungleTopRight);
        } while(this.bushes.get(pos) != null);
        return new Grass(pos);
    }

    /**
     * Sends signal to the herd on a given tile to eat grass
     * @param pos Grass position
     */
    public void eatGrass(Vector pos) {
        if (this.herdsTable[pos.x][pos.y].eatGrass()) {
            if(this.isJungle(pos)) {
                this.jungleBushes--;
            } else {
                this.steppeBushes--;
            }
            this.bushes.remove(pos);
        }
    }

    /**
     * Adds the Animal on a given position
     * @param a Animal
     */
    public void addAnimal(Animal a) {
        if (a == null) {
            return;
        }
        this.livingAnimals++;
        a.addPositionObserver(this);
        a.addKilledObserver(this);
        this.herdsTable[a.getX()][a.getY()].addAnimal(a);
        this.animalList.add(a);
    }

    /**
     * Sends signal to the herd on a given tile to breed, and setups new animal
     * @param pos Position
     */
    public void makeLove(Vector pos) {
        Animal a = this.herdsTable[pos.x][pos.y].makeLove();
        this.addAnimal(a);
        if (a != null) {
            for(int x = Math.max(0, pos.x-1); x <= Math.min(this.getX(), pos.x+1); ++x) {
                for(int y = Math.max(0, pos.y-1); y <= Math.min(this.getY(), pos.y+1); ++y) {
                    if(this.isEmpty(new Vector(x, y))) {
                        a.move(new Vector(x, y));
                        return;
                    }
                }
            }
        }
    }

    /**
     * Spawns new animal on a free tile
     */
    public void spawnAnimal() {
        Vector pos;
        do {
            pos = Generators.randomVector(this.rand, this.bottomLeft, this.topRight);
        } while(this.herdsTable[pos.x][pos.y].getAnimal() != null);
        this.addAnimal(new Animal(pos, this.params));
    }

    /**
     * Spawns one grass in the steppe and in the jungle
     */
    public void spawnGrass() {
        this.addGrass(this.getJungleGrass());
        this.addGrass(this.getSteppeGrass());
    }

    @Override
    public void positionChanged(Vector oldPos, Vector newPos, Animal caller) {
        this.herdsTable[oldPos.x][oldPos.y].removeAnimal(caller);
        this.herdsTable[newPos.x][newPos.y].addAnimal(caller);
    }

    @Override
    public void killed(Animal a) {
        this.animalList.remove(a);
        this.livingAnimals--;
        this.deadAnimals++;
    }

    public Animal animalAt(Vector pos) {
        return this.herdsTable[pos.x][pos.y].getAnimal();
    }

    public boolean isJungle(Vector pos) {
        return this.jungleBottomLeft.precedes(pos) && this.jungleTopRight.follows(pos);
    }

    public boolean withinMap(Vector pos) {
        return this.bottomLeft.precedes(pos) && this.topRight.follows(pos);
    }

    boolean isEmpty(Vector pos) { return this.animalAt(pos) == null; }

    public boolean isGrass(Vector pos) {
        return this.bushes.get(pos) != null;
    }

    /**
     *
     * @return Jungle rectangle location as a 4 elements Array
     */
    public int[] jungleSize() {
        return new int[]{this.jungleBottomLeft.x, this.jungleBottomLeft.y,
                this.jungleTopRight.x - this.jungleBottomLeft.x + 1, this.jungleTopRight.y - this.jungleBottomLeft.y + 1};
    }

    public List<Animal> getAnimalList() {
        return Collections.unmodifiableList(this.animalList);
    }

    public Vector getTopRight() { return this.topRight; }

    public int getLivingAnimals() { return livingAnimals; }

    public int getDeadAnimals() { return this.deadAnimals; }

    public int getSteppeBushes() { return this.steppeBushes; }

    public int getJungleBushes() { return this.jungleBushes; }

    public int getMaxJungleBushes() { return this.maxJungleBushes; }

    public int getMaxSteppeBushes() { return this.maxSteppeBushes; }

    public int getX() { return this.topRight.x; }

    public int getY() { return this.topRight.y; }
}
