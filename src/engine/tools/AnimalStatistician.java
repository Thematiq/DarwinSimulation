package engine.tools;

import engine.objects.Animal;
import engine.observers.IObserverBreed;
import engine.observers.IObserverKilled;

public class AnimalStatistician implements IObserverBreed, IObserverKilled {
    private final Animal watchedAnimal;

    private int totalChildren;
    private int totalDescendants;
    private int deathDay;

    public AnimalStatistician(Animal a) {
        this.watchedAnimal = a;
        this.watchedAnimal.addBreedObserver(this);
        this.watchedAnimal.addKilledObserver(this);
    }

    @Override
    public void breed(Animal a, Animal child) {
        if (a == watchedAnimal) {
            this.totalChildren++;
        }
        this.totalDescendants++;
        child.addBreedObserver(this);
    }

    @Override
    public void killed(Animal a) {
        this.deathDay = a.getLifespan();
    }

    public int getTotalChildren() { return this.totalChildren; }

    public int getTotalDescendants() { return this.totalDescendants; }

    public int getDeathDay() { return this.deathDay; }
}
