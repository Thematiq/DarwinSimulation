package engine.observers;

import engine.objects.Animal;
import engine.tools.Vector;

public class MockIObserverPositionChanged implements IObserverPositionChanged {
    private int value;

    public MockIObserverPositionChanged() { }

    @Override
    public void positionChanged(Vector oldPos, Vector newPos, Animal caller) {
        this.value++;
    }

    public int getValue() {
        return this.value;
    }
}
