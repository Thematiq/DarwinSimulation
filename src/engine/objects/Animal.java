package engine.objects;

import engine.observers.IObserverKilled;
import engine.observers.IObserverPositionChanged;
import engine.tools.Genome;
import engine.tools.Orientation;
import engine.tools.Parameters;
import engine.tools.Vector;

import java.util.ArrayList;
import java.util.List;

public class Animal {
    final Parameters params;
    int energy = 0;
    int days = 0;
    Vector pos;
    Genome genes;
    Orientation orient;
    List<IObserverPositionChanged> positionObservers = new ArrayList<>();
    List<IObserverKilled> killedObservers = new ArrayList<>();


    /** Spawns new Animal at a desired position
     *
     * @param pos Desired position
     */
    public Animal(Vector pos, Parameters params) {
        this.params = params;
        this.genes = new Genome();
        this.pos = pos;
        this.orient = this.genes.getRotation();
        this.energy = this.params.startEnergy;
    }

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

    /** Moves animal to the new tile
     *
     * @param newPos desired tile
     */
    public void move(Vector newPos) {
        this.loseEnergy(this.params.moveEnergy);
        for(IObserverPositionChanged o : this.positionObservers) {
            o.positionChanged(this.pos, newPos, this);
        }
        this.pos = newPos;
        this.days++;
    }

    public void kill() {
        for(IObserverKilled o : this.killedObservers) {
            o.killed(this);
        }
    }

    public void eat(int energy) {
        this.energy += energy;
    }

    public void loseEnergy(int energy) {
        this.energy -= energy;
    }

    public void Rotate() {
        this.orient = this.genes.getRotation();
    }

    /** Links IObserverPositionChanged
     *
     * @param obs observer
     */
    public void addPositionObserver(IObserverPositionChanged obs) {
        this.positionObservers.add(obs);
    }

    public void addKilledObserver(IObserverKilled obs) { this.killedObservers.add(obs); }

    /**
     * @return current Animal orientation
     */
    public Orientation getOrient() {
        return this.orient;
    }

    /**
     * @return current Animal position
     */
    public Vector getPos() {
        return this.pos;
    }

    /**
     * @return current Animal X coordinate
     */
    public int getX() {
        return this.pos.x;
    }

    /**
     * @return current Animal Y coordinate
     */
    public int getY() {
        return this.pos.y;
    }

    /**
     * @return current Animal energy
     */
    public int getEnergy() {
        return this.energy;
    }

    public Genome getGenes() { return this.genes; }

    public int getLifespan() { return this.days; }
}
