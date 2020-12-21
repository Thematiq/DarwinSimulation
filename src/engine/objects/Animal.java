package engine.objects;

import engine.observers.IObserverBreed;
import engine.observers.IObserverEnergyChanged;
import engine.observers.IObserverKilled;
import engine.observers.IObserverPositionChanged;
import engine.tools.Genome;
import engine.tools.Orientation;
import engine.tools.Parameters;
import engine.tools.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Class describing Animal object
 * @author Mateusz Praski
 */
public class Animal {
    final Parameters params;
    final Genome genes;
    private final List<IObserverPositionChanged> positionObservers = new ArrayList<>();
    private final List<IObserverKilled> killedObservers = new ArrayList<>();
    private final List<IObserverBreed> breedObservers = new ArrayList<>();
    private final List<IObserverEnergyChanged> energyObservers = new ArrayList<>();

    private int energy = 0;
    private int days = 0;
    private int children = 0;
    private Vector pos;
    private Orientation orient;

    /**
     * Creates new Animal object
     * @param pos Starting position
     * @param params Simulation parameters
     */
    public Animal(Vector pos, Parameters params) {
        this.params = params;
        this.genes = new Genome();
        this.pos = pos;
        this.orient = this.genes.getRotation();
        this.energy = this.params.startEnergy;
    }

    /**
     * Creates new Animal object from its parents
     * @param father Dominating genome
     * @param mother Inferior genome
     */
    public Animal(Animal father, Animal mother) {
        this.params = father.params;
        int parentEnergy = father.getEnergy() / 4;
        father.loseEnergy(parentEnergy);
        this.energy += parentEnergy;
        parentEnergy = mother.getEnergy() / 4;
        mother.loseEnergy(parentEnergy);
        this.energy += parentEnergy;
        this.genes = new Genome(mother.genes, father.genes);
        this.orient = this.genes.getRotation();
        this.pos = new Vector(0,0);
    }

    /**
     * Moves animal to the new position, calling observers
     * This method decreases energy by move cost
     * @param newPos desired position
     */
    public void move(Vector newPos) {
        this.loseEnergy(this.params.moveEnergy);
        for(IObserverPositionChanged o : this.positionObservers) {
            o.positionChanged(this.pos, newPos, this);
        }
        this.pos = newPos;
        this.days++;
    }

    /**
     * Calls all observers that animal have reproduced
     * @param child Animal's child
     */
    void breed(Animal child) {
        this.children++;
        for (IObserverBreed obs : this.breedObservers) {
            obs.breed(this, child);
        }
    }

    /**
     * Calls all observers, that animal have been killed
     */
    public void kill() {
        for(IObserverKilled o : new ArrayList<>(this.killedObservers)) {
            o.killed(this);
        }
    }

    private void energyChanged(int oldVal) {
        for(IObserverEnergyChanged obs : this.energyObservers) {
            obs.energyChanged(this, oldVal);
        }
    }

    /**
     * Increases animal energy
     * @param energy eaten energy
     */
    public void eat(int energy) {
        this.energy += energy;
        this.energyChanged(this.energy - energy);
    }

    /**
     * Decreases animal energy
     * @param energy lost energy
     */
    public void loseEnergy(int energy) {
        this.energy -= energy;
        this.energyChanged(this.energy + energy);
    }

    /**
     * Sets energy value
     * @param energy new energy value
     */
    public void setEnergy(int energy) {
        int oldVal = this.energy;
        this.energy = energy;
        this.energyChanged(oldVal);
    }

    /**
     * Changes animal rotation based on a Genome
     */
    public void Rotate() {
        this.orient = Orientation.translateOrient(this.orient, this.genes.getRotation());
    }

    /**
     * Adds new move() observer
     * @param obs new observer
     */
    public void addPositionObserver(IObserverPositionChanged obs) {
        this.positionObservers.add(obs);
    }

    /**
     * Adds new kill() observer
     * @param obs new observer
     */
    public void addKilledObserver(IObserverKilled obs) { this.killedObservers.add(obs); }

    public void removeKilledObserver(IObserverKilled obs) { this.killedObservers.remove(obs); }

    /**
     * Adds new breed() observer
     * @param obs new observer
     */
    public void addBreedObserver(IObserverBreed obs) { this.breedObservers.add(obs); }

    public void addEnergyObserver(IObserverEnergyChanged obs) { this.energyObservers.add(obs); }

    public void removeEnergyObserver(IObserverEnergyChanged obs) { this.energyObservers.remove(obs); }

    public boolean hasBreedObserver(IObserverBreed obs) { return this.breedObservers.contains(obs); }

    public Orientation getOrient() {
        return this.orient;
    }

    public Vector getPos() {
        return this.pos;
    }

    public int getX() {
        return this.pos.x;
    }

    public int getY() {
        return this.pos.y;
    }

    public int getEnergy() {
        return this.energy;
    }

    public Genome getGenes() { return this.genes; }

    public int getLifespan() { return this.days; }

    public int getChildren() { return this.children; }
}
