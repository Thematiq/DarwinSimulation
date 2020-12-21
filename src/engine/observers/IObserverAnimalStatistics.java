package engine.observers;

import engine.tools.AnimalStatistician;

/**
 * Observer for various method changing Animal statistics
 * @author Mateusz Praski
 */
public interface IObserverAnimalStatistics {
    void newData(AnimalStatistician caller);
}
