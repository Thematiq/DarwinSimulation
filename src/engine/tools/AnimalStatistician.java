package engine.tools;

import engine.handlers.Simulation;
import engine.objects.Animal;
import engine.observers.IObserverAnimalStatistics;
import engine.observers.IObserverBreed;
import engine.observers.IObserverKilled;
import engine.observers.IObserverNewDay;

import java.util.ArrayList;
import java.util.List;

public class AnimalStatistician implements IObserverBreed, IObserverKilled, IObserverNewDay {
    private final Animal watchedAnimal;
    private final List<IObserverAnimalStatistics> observerAnimalStatisticsList = new ArrayList<>();
    private final int endDate;
    private final int startDate;

    private boolean running = true;
    private int totalChildren = 0;
    private int totalDescendants = 0;
    private int deathDay = 0;
    private boolean died = false;

    public AnimalStatistician(Simulation sim, Animal a, int duration) {
        this.startDate = sim.getDay();
        this.endDate = sim.getDay() + duration;
        this.watchedAnimal = a;
        this.watchedAnimal.addBreedObserver(this);
        this.watchedAnimal.addKilledObserver(this);
        sim.addNewDayObserver(this);
    }

    @Override
    public void breed(Animal a, Animal child) {
        if (running) {
            if (!child.hasBreedObserver(this)) {
                if (a == watchedAnimal) {
                    this.totalChildren++;
                }
                this.totalDescendants++;
                child.addBreedObserver(this);
            }
            this.callObservers();
        }
    }

    @Override
    public void killed(Animal a) {
        if (running) {
            this.died = true;
            this.deathDay = a.getLifespan();
            this.callObservers();
        }
    }

    @Override
    public void dayChanged(int day, Simulation caller) {
        if (running) {
            if (this.endDate <= day) {
                this.running = false;
            }
            this.callObservers();
        }
    }

    private void callObservers() {
        for(IObserverAnimalStatistics obs : this.observerAnimalStatisticsList) {
            obs.newData(this);
        }
    }

    public void addIObserverAnimalStatistics(IObserverAnimalStatistics obs) { this.observerAnimalStatisticsList.add(obs); }

    public int getTotalChildren() { return this.totalChildren; }

    public int getTotalDescendants() { return this.totalDescendants; }

    public int getDeathDay() { return this.deathDay + this.startDate; }

    public boolean hasDied() { return this.died; }

    public boolean isRunning() {return this.running; }
}
