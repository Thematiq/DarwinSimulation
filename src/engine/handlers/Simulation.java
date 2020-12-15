package engine.handlers;

import engine.objects.Animal;
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

    public void nextDay() {
        this.day++;

        this.map.spawnGrass();
        // ...

        for(IObserverNewDay obs : this.newDayObservers) {
            obs.dayChanged(this.day, this);
        }
    }

    public int getDay() {
        return this.day;
    }

    public int getPopulation() {
        return 0;
    }

    public Object objectAt(Vector pos) {
        return this.map.objectAt(pos);
    }

    public boolean isJungle(Vector pos) {
        return this.map.isJungle(pos);
    }

    public Animal animalAt(Vector pos) { return this.map.animalAt(pos); }

    public boolean isGrass(Vector pos) {
        return this.map.isGrass(pos);
    }

    public int[] jungleSize() {
        return this.map.jungleSize();
    }
}
