package engine.tools;

import engine.handlers.Simulation;
import engine.objects.Animal;
import engine.observers.IObserverKilled;
import engine.observers.IObserverNewDay;
import engine.observers.IObserverStatistics;

import java.util.ArrayList;
import java.util.List;

public class Statistician implements IObserverNewDay, IObserverKilled {

    private List<Integer> livingAnimals = new ArrayList<>();
    private List<Integer> deadAnimals = new ArrayList<>();
    private List<Integer> vegetation = new ArrayList<>();
    private List<Integer> meanEnergy = new ArrayList<>();
    private List<Integer> meanChildren = new ArrayList<>();
    private List<Integer> meanLifespan = new ArrayList<>();
    private List<Genome> dominatingGenome = new ArrayList<>();
    private List<Integer> deadLifespans = new ArrayList<>();
    private List<IObserverStatistics> observerStatistics = new ArrayList<>();

    private long deadTotalLifespan;
    private int currentDay;


    public Statistician(Simulation sim) {
        sim.addNewDayObserver(this);
    }

    public void addIObserverStatistics(IObserverStatistics observer) {
        this.observerStatistics.add(observer);
    }

    void callObservers() {
        for (IObserverStatistics o : this.observerStatistics) {
            o.update();
        }
    }

    @Override
    public void dayChanged(int day, Simulation caller) {
        this.currentDay = caller.getDay();
        this.livingAnimals.add(caller.getPopulation());
        this.deadAnimals.add(caller.getGraveyard());
        this.vegetation.add(caller.getVegetation());
        List<Animal> currentAnimals = caller.getAnimalList();
        long totalEnergy = 0;
        long lifespan = 0;
        for(Animal a : currentAnimals) {
            totalEnergy += a.getEnergy();
            lifespan += a.getLifespan();
        }

        this.meanLifespan.add((int) (lifespan + this.deadTotalLifespan)/(this.deadAnimals.size() + currentAnimals.size()));
        this.meanEnergy.add((int) (totalEnergy / currentAnimals.size()));

        this.callObservers();
    }

    @Override
    public void killed(Animal a) {
        this.deadLifespans.add(a.getLifespan());
        this.deadTotalLifespan = 0;
        for(int i : this.deadLifespans) {
            this.deadTotalLifespan += i;
        }
    }

    public int getCurrentAnimals() {
        return this.livingAnimals.get(this.livingAnimals.size() - 1);
    }

    public int getCurrentVegetation() {
        return this.vegetation.get(this.vegetation.size() - 1);
    }

    public int getCurrentGraveyard() {
        return this.deadAnimals.get(this.deadAnimals.size() - 1);
    }

    public int getCurrentEnergy() {
        return this.meanEnergy.get(this.meanEnergy.size() - 1);
    }

    public int getCurrentChildren() {
        return this.meanChildren.get(this.meanChildren.size() - 1);
    }

    // TODO
    // Lifespan works strange...
    public int getCurrentLifespan() {
        return this.meanLifespan.get(this.meanLifespan.size() - 1);
    }

    public Genome getCurrentDominant() {
        return this.dominatingGenome.get(this.dominatingGenome.size() - 1);
    }

    public int getCurrentDay() {
        return this.currentDay;
    }
}
