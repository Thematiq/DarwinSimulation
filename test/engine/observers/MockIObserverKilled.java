package engine.observers;

import engine.objects.Animal;

/**
 * Mock object to test objects having killed observers
 * @author Mateusz Praski
 */
public class MockIObserverKilled implements IObserverKilled {
    private int value = 0;

    public MockIObserverKilled() { }

    @Override
    public void killed(Animal a) {
        this.value++;
    }

    public int getValue() {
        return this.value;
    }
}
