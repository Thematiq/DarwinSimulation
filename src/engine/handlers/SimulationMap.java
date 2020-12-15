package engine.handlers;

import engine.objects.Animal;
import engine.objects.Grass;
import engine.objects.Herd;
import engine.tools.Parameters;
import engine.tools.Vector;

import java.util.*;

public class SimulationMap implements IObserverPositionChanged, IObserverKilled {
    final Vector jungleBottomLeft;
    final Vector jungleTopRight;
    final Vector bottomLeft = new Vector(0, 0);
    final Vector topRight;
    final int maxJungleBushes;
    final int maxSteppeBushes;
    final float bushThreshold = (float) 0.9;
    final Parameters params;

    private Random rand = new Random();
    private Map<Vector, Grass> bushes = new HashMap<>();
    private List<Animal> animalList = new ArrayList<>();
    private Herd[][] herdsTable;

    private int livingAnimals = 0;
    private int deadAnimals = 0;
    private int steppeBushes = 0;
    private int jungleBushes = 0;

    /** Creates a new map with animals
     *
     * @param params Param structure containing paremeters for a Map
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
     *
     * @return Current number of living animals
     */
    public int getLivingAnimals() {
        return livingAnimals;
    }

    /** Add grass at a given position if it's free
     *
     * @param a Grass object
     */
    public void addGrass(Grass a) {
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

    /** Add animal at a given position and links IObserverPositionChanged
     *
     * @param a Animal object
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

    /** Generates new Grass object at a free position inside the steppes
     *
     * @return new Grass object or null if steppes are full
     */
    Grass getSteppeGrass() {
        if (this.maxSteppeBushes == this.steppeBushes) {
            return null;
        }
        Vector pos;
        do {
            pos = Vector.randomVector(this.rand, this.bottomLeft, this.topRight);
        } while(this.bushes.get(pos) != null || this.isJungle(pos));
        return new Grass(pos);
    }

    /** Generates new Grass object at a free position inside the jungle
     *
     * @return new Grass object or null if jungle is full
     */
    Grass getJungleGrass() {
        if (this.maxJungleBushes == this.jungleBushes) {
            return null;
        }
        Vector pos;
        do {
            pos = Vector.randomVector(this.rand, this.jungleBottomLeft, this.jungleTopRight);
        } while(this.bushes.get(pos) != null);
        return new Grass(pos);
    }

    /** Spawn new grass inside jungle and the steppes
     *  Used by Simulation object
     */
    public void spawnGrass() {
        this.addGrass(this.getJungleGrass());
        this.addGrass(this.getSteppeGrass());
    }

    public void kill(Animal a) {
        this.animalList.remove(a);
        this.livingAnimals--;
        this.deadAnimals++;
    }

    public void eatGrass(Vector pos) {
        if (this.herdsTable[pos.x][pos.y].eatGrass(this.params.plantEnergy)) {
            if(this.isJungle(pos)) {
                this.jungleBushes--;
            } else {
                this.steppeBushes--;
            }
            this.bushes.remove(pos);
        }
    }

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
     * Spawn new animal at a random free position, then adds it to the map
     */
    public void spawnAnimal() {
        Vector pos;
        do {
            pos = Vector.randomVector(this.rand, this.bottomLeft, this.topRight);
        } while(this.herdsTable[pos.x][pos.y].getAnimal() != null);
        this.addAnimal(new Animal(pos, this.params));
    }

    /**
     * @param pos Desired position
     * @return True if position is jungle tile
     */
    public boolean isJungle(Vector pos) {
        return this.jungleBottomLeft.precedes(pos) && this.jungleTopRight.follows(pos);
    }

    public boolean withinMap(Vector pos) {
        return this.bottomLeft.precedes(pos) && this.topRight.follows(pos);
    }

    public int getGrassAmount() {
        return this.jungleBushes + this.steppeBushes;
    }

    /**
     * @param pos Desired position
     * @return Strongest animal in the herd or null if hers is empty
     */
    public Animal animalAt(Vector pos) {
        return this.herdsTable[pos.x][pos.y].getAnimal();
    }

    boolean isEmpty(Vector pos) { return this.animalAt(pos) == null; }
    /**
     * @param pos Desired position
     * @return Object at a given position or null if tile is empty
     */
    public Object objectAt(Vector pos) {
        Object ret = animalAt(pos);
        if (ret != null) {
            return ret;
        } else {
            return this.bushes.get(pos);
        }
    }

    /**
     * @param pos Desired position
     * @return True if there is a grass at the tile
     */
    public boolean isGrass(Vector pos) {
        return this.bushes.get(pos) != null;
    }

    /**
     * @return 4 element array describing Jungle rectangle. Used to draw jungle
     */
    public int[] jungleSize() {
        return new int[]{this.jungleBottomLeft.x, this.jungleBottomLeft.y,
                         this.jungleTopRight.x - this.jungleBottomLeft.x + 1, this.jungleTopRight.y - this.jungleBottomLeft.y + 1};
    }

    public List<Animal> getAnimalList() {
        return Collections.unmodifiableList(this.animalList);
    }

    @Override
    public void positionChanged(Vector oldPos, Vector newPos, Animal caller) {
        this.herdsTable[oldPos.x][oldPos.y].removeAnimal(caller);
        this.herdsTable[newPos.x][newPos.y].addAnimal(caller);
    }

    public int getX() {
        return this.topRight.x;
    }

    public int getY() {
        return this.topRight.y;
    }

    @Override
    public void killed(Animal a) {
        this.animalList.remove(a);
        this.livingAnimals--;
        this.deadAnimals++;
    }
}
