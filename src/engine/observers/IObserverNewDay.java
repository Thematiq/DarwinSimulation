package engine.observers;

import engine.handlers.Simulation;

public interface IObserverNewDay {
    void dayChanged(int day, Simulation caller);
}
