package engine.observers;

import engine.tools.Statistician;

/**
 * Observer for a Statistician.dayChanged() method
 * @author Mateusz Praski
 */
public interface IObserverStatistics {
    void update(Statistician caller);
}
