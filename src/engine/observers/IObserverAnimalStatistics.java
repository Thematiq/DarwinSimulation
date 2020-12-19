package engine.observers;

import engine.tools.AnimalStatistician;

public interface IObserverAnimalStatistics {
    void newData(AnimalStatistician caller);
}
