package engine.observers;

import engine.tools.SimulationStatistician;

/**
 * Mock object to test objects having statistician observers
 * @author Mateusz Praski
 */
public class MockIObserverSimulationStatistics implements IObserverSimulationStatistics {
    private int value = 0;

    public MockIObserverSimulationStatistics() { }

    @Override
    public void update(SimulationStatistician caller) {
        this.value++;
    }

    public int getValue() { return this.value; }
}
