package engine.observers;

import engine.objects.Animal;
import engine.tools.Vector;

/**
 * Mock object to test objects having positionChanged observers
 * @author Mateusz Praski
 */
public class MockIObserverPositionChanged implements IObserverPositionChanged {
    private int value = 0;

    public MockIObserverPositionChanged() { }

    @Override
    public void positionChanged(Vector oldPos, Vector newPos, Animal caller) {
        this.value++;
    }

    public int getValue() {
        return this.value;
    }
}
