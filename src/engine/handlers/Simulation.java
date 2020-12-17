package engine.handlers;

import engine.objects.Animal;
import engine.observers.IObserverNewDay;
import engine.tools.Parameters;
import engine.tools.Vector;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private List<IObserverNewDay> newDayObservers = new ArrayList<>();
    private int day = 0;
    private SimulationMap map;

    public Simulation(Parameters params) {
        this.map = new SimulationMap(params);
    }

    public void addNewDayObserver(IObserverNewDay observer) {
        this.newDayObservers.add(observer);
    }

    /**
     * Ticks current simulation by a dayW
     */
    public void nextDay() {
        this.day++;
        Vector newpos;
        List<Animal> reaperPuppiesList = new ArrayList<>();
        // Kill
        for (Animal a : this.map.getAnimalList()) {
            if (a.getEnergy() <= 0) {
                reaperPuppiesList.add(a);
            }
        }
        for(Animal a : reaperPuppiesList) {
            a.kill();
        }

        // Rotate and move
        for(Animal a : this.map.getAnimalList()) {
            a.Rotate();
            newpos = a.getPos().add(a.getOrient().getUnitVector());
            if(!this.map.withinMap(newpos)) {
                newpos = newpos.wrap(this.map.topRight);
            }
            a.move(newpos);
        }

        // Eat
        for(int x = 0; x < this.map.getX(); ++x) {
            for(int y = 0; y < this.map.getY(); ++y) {
                if (this.map.isGrass(new Vector(x, y))) {
                    this.map.eatGrass(new Vector(x, y));
                }
            }
        }

        // Love
        for(int x = 0; x < this.map.getX(); ++x) {
            for(int y = 0; y < this.map.getY(); ++y) {
                this.map.makeLove(new Vector(x, y));
            }
        }

        // New grass
        this.map.spawnGrass();

        // Call observers
        for(IObserverNewDay obs : this.newDayObservers) {
            obs.dayChanged(this.day, this);
        }
    }

    /**
      * @return Current simulation day
      */
    public int getDay() {
        return this.day;
    }

    /**
     * @return current simulation population
     */
    public int getPopulation() {
        return this.map.getLivingAnimals();
    }

    public int getVegetation() {
        return this.map.getGrassAmount();
    }

    public int getGraveyard() { return this.map.getDeadAnimals(); }

    public List<Animal> getAnimalList() { return this.map.getAnimalList(); }

    /**
     * @param pos Position at the map
     * @return Animal at a given position or null if there are none
     */
    public Animal animalAt(Vector pos) { return this.map.animalAt(pos); }

    /**
     *
     * @param pos Position at the map
     * @return True if there is a grass at a given pos
     */
    public boolean isGrass(Vector pos) {
        return this.map.isGrass(pos);
    }

    /**
     *
     * @return 4 element array describing Jungle rectangle. Used to draw jungle
     */
    public int[] jungleSize() {
        return this.map.jungleSize();
    }
}
