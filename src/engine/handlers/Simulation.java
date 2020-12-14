package engine.handlers;

import java.util.ArrayList;
import java.util.List;

public class Simulation {

    private List<IObserverNewDay> newDayObservers = new ArrayList<>();
    private int day = 0;

    public Simulation() {

    }

    public void addNewDayObserver(IObserverNewDay observer) {
        this.newDayObservers.add(observer);
    }

    public void nextDay() {
        this.day++;

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
}
