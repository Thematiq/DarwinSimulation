package engine.observers;

import engine.handlers.Simulation;

/**
 * Mock object to test objects having newDay observers
 * @author Mateusz Praski
 */
public class MockIObserverNewDay implements IObserverNewDay {
    private int value = 0;

    public MockIObserverNewDay() { }

    @Override
    public void dayChanged(int day, Simulation caller) {
        this.value++;
    }

    public int getValue() { return this.value; }
}
