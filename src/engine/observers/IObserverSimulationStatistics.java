package engine.observers;

import engine.tools.SimulationStatistician;

/**
 * Observer for a Statistician.dayChanged() method
 * @author Mateusz Praski
 */
public interface IObserverSimulationStatistics {
    void update(SimulationStatistician caller);
}
