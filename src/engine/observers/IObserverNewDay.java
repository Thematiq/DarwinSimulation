package engine.observers;

import engine.handlers.Simulation;

/**
 * Observer for a Simulation.nextDay() method
 * @author Mateusz Praski
 */
public interface IObserverNewDay {
    void dayChanged(int day, Simulation caller);
}
