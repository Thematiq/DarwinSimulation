package engine.handlers;

import engine.objects.Animal;
import engine.observers.IObserverKilled;
import engine.observers.IObserverNewDay;
import engine.tools.Parameters;
import engine.tools.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing Simulation
 * @author Mateusz Praski
 */
public class Simulation {
    private final List<IObserverNewDay> newDayObservers = new ArrayList<>();
    private final List<IObserverKilled> observerKilled = new ArrayList<>();
    final SimulationMap map;

    private int day = 0;

    /**
     * Creates new simulation
     * @param params Simulation params
     */
    public Simulation(Parameters params) { this.map = new SimulationMap(params); }

    /**
     * Calls all observer during 0-th day
     * Separated from constructor allowing adding observers
     */
    public void zeroDay() { this.callObservers(); }

    /**
     * Handles new day
     */
    public void nextDay() {
        this.day++;
        this.handleKilling();
        this.handleMovement();
        this.handleEating();
        this.handleLove();
        this.map.spawnGrass();
        this.callObservers();
    }

    /**
     * Kills all animals having energy smaller than zero
     */
    private void handleKilling() {
        List<Animal> reaperPuppiesList = new ArrayList<>();
        for (Animal a : this.map.getAnimalList()) {
            if (a.getEnergy() <= 0) {
                reaperPuppiesList.add(a);
            }
        }
        for(Animal a : reaperPuppiesList) {
            a.kill();
            for(IObserverKilled obs : this.observerKilled) {
                obs.killed(a);
            }
        }
    }

    /**
     * Rotates all animals and moves them
     */
    private void handleMovement() {
        Vector newPos;
        for(Animal a : this.map.getAnimalList()) {
            a.Rotate();
            newPos = a.getPos().add(a.getOrient().getUnitVector());
            if(!this.map.withinMap(newPos)) {
                newPos = newPos.wrap(this.map.getTopRight());
            }
            a.move(newPos);
        }
    }

    /**
     * Send signal to the all tiles containing grass to eat
     */
    private void handleEating() {
        for(int x = 0; x <= this.map.getMaxX(); ++x) {
            for(int y = 0; y <= this.map.getMaxY(); ++y) {
                if (this.map.isGrass(new Vector(x, y))) {
                    this.map.eatGrass(new Vector(x, y));
                }
            }
        }
    }

    /**
     * Send signal to all tiles to breed
     */
    private void handleLove() {
        for(int x = 0; x <= this.map.getMaxX(); ++x) {
            for(int y = 0; y <= this.map.getMaxY(); ++y) {
                this.map.makeLove(new Vector(x, y));
            }
        }
    }

    /**
     * Calls all observers
     */
    private void callObservers() {
        for(IObserverNewDay obs : this.newDayObservers) {
            obs.dayChanged(this.day, this);
        }
    }

    public void addNewDayObserver(IObserverNewDay observer) {
        this.newDayObservers.add(observer);
    }

    public void addNewAllKillsObserver(IObserverKilled observer) { this.observerKilled.add(observer); }

    public Animal animalAt(Vector pos) { return this.map.animalAt(pos); }

    public boolean isGrass(Vector pos) {
        return this.map.isGrass(pos);
    }

    public List<Animal> getAnimalList() { return this.map.getAnimalList(); }

    public int getDay() {
        return this.day;
    }

    public int getPopulation() {
        return this.map.getLivingAnimals();
    }

    public int getVegetation() {
        return this.map.getSteppeBushes() + this.map.getJungleBushes();
    }

    public int getGraveyard() { return this.map.getDeadAnimals(); }

    public int[] jungleSize() {
        return this.map.jungleSize();
    }
}
