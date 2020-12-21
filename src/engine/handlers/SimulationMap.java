package engine.handlers;

import engine.objects.Animal;
import engine.objects.Grass;
import engine.objects.Herd;
import engine.observers.IObserverKilled;
import engine.observers.IObserverPositionChanged;
import engine.tools.Generators;
import engine.tools.Genome;
import engine.tools.Parameters;
import engine.tools.Vector;

import java.util.*;

/**
 * Class representing Simulation map
 * @author Mateusz Praski
 */
public class SimulationMap implements IObserverPositionChanged, IObserverKilled {
    final Parameters params;
    final Vector jungleBottomLeft;
    final Vector jungleTopRight;
    final Vector bottomLeft = new Vector(0, 0);
    final Vector topRight;
    private final Random rand = new Random();
    private final Map<Vector, Grass> bushes = new HashMap<>();
    private final Map<Vector, Herd> herdsTable = new HashMap<>();
    private final List<Animal> animalList = new ArrayList<>();

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
        double jungleRatio = Math.sqrt(params.jungleRatio);
        for (int x = 0; x < params.width; ++x) {
            for (int y = 0; y < params.height; ++y) {
                this.herdsTable.put(new Vector(x, y), new Herd(params));
            }
        }

        int jungleWidth = (int)(params.width * jungleRatio);
        int jungleHeight = (int)(params.height * jungleRatio);

        Vector middle = new Vector(params.width / 2, params.height / 2);
        this.jungleBottomLeft = middle.add(new Vector(- (jungleWidth/2), - (jungleHeight/2)));
        this.jungleTopRight = middle.add(new Vector((jungleWidth/2)-1, (jungleHeight/2)-1));

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
     * Sends signal to the herd on a given tile to eat grass
     * @param pos Grass position
     */
    public void eatGrass(Vector pos) {
        if (this.herdsTable.get(pos).eatGrass()) {
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
    void addAnimal(Animal a) {
        if (a == null) {
            return;
        }
        this.livingAnimals++;
        a.addPositionObserver(this);
        a.addKilledObserver(this);
        this.herdsTable.get(a.getPos()).addAnimal(a);
        this.animalList.add(a);
    }

    public Animal makeLove(Vector pos) { return this.herdsTable.get(pos).makeLove(); }

    /**
     * Generates list of available tiles, then choose one from each land type
     * @return 2 element array with new grass in the jungle and in the steppes
     */
    Grass[] getGrass() {
        Grass[] g = new Grass[2];
        List<Vector> potentialJungle = new ArrayList<>();
        List<Vector> potentialSteppe = new ArrayList<>();
        for (int x = 0; x <= this.getMaxX(); ++x) {
            for (int y = 0; y <= this.getMaxY(); ++y) {
                if (this.isEmpty(new Vector(x, y))) {
                    if(this.isJungle(new Vector(x, y))) {
                        potentialJungle.add(new Vector(x, y));
                    } else {
                        potentialSteppe.add(new Vector(x, y));
                    }
                }
            }
        }
        if (potentialJungle.size() == 0) {
            g[0] = null;
        } else {
            g[0] = new Grass(potentialJungle.get(this.rand.nextInt(potentialJungle.size())));
        }
        if (potentialSteppe.size() == 0) {
            g[1] = null;
        } else {
            g[1] = new Grass(potentialSteppe.get(this.rand.nextInt(potentialSteppe.size())));
        }
        return g;
    }

    /**
     * Spawns random animal on free tile
     * @return new Animal
     */
    private Animal getAnimal() {
        Vector pos;
        do {
            pos = Generators.randomVector(this.rand, this.bottomLeft, this.topRight);
        } while(this.animalAt(pos) != null);
        return new Animal(pos, this.params);
    }

    /**
     * Spawns new animal on a free tile
     */
    public void spawnAnimal() {
        this.addAnimal(this.getAnimal());
    }

    /**
     * Spawns one grass in the steppe and in the jungle
     */
    public void spawnGrass() {
        /*
        this.addGrass(this.getJungleGrass());
        this.addGrass(this.getSteppeGrass());
        */
        Grass[] g = this.getGrass();
        this.addGrass(g[0]);
        this.addGrass(g[1]);
    }

    @Override
    public void positionChanged(Vector oldPos, Vector newPos, Animal caller) {
        this.herdsTable.get(oldPos).removeAnimal(caller);
        this.herdsTable.get(newPos).addAnimal(caller);
    }

    @Override
    public void killed(Animal a) {
        this.animalList.remove(a);
        this.livingAnimals--;
        this.deadAnimals++;
    }

    public Animal animalAt(Vector pos) {
        return this.herdsTable.get(pos).getAnimal();
    }

    public boolean isJungle(Vector pos) {
        return this.jungleBottomLeft.precedes(pos) && this.jungleTopRight.follows(pos);
    }

    public boolean withinMap(Vector pos) {
        return this.bottomLeft.precedes(pos) && this.topRight.follows(pos);
    }

    public boolean hasAnimal(Vector pos) { return this.animalAt(pos) != null; }

    public boolean isGrass(Vector pos) {
        return this.bushes.get(pos) != null;
    }

    public boolean isEmpty(Vector pos) { return !this.isGrass(pos) && !this.hasAnimal(pos); }

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

    public Parameters getParams() { return this.params; }

    public int getLivingAnimals() { return livingAnimals; }

    public int getDeadAnimals() { return this.deadAnimals; }

    public int getSteppeBushes() { return this.steppeBushes; }

    public int getJungleBushes() { return this.jungleBushes; }

    public int getMaxX() { return this.topRight.x; }

    public int getMaxY() { return this.topRight.y; }

    public boolean hasGenome(Vector pos, Genome genome) { return this.herdsTable.get(pos).hasGenome(genome); }
}
