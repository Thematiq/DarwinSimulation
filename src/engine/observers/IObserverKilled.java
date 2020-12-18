package engine.observers;

import engine.objects.Animal;

/**
 * Observer for a Animal.killed() method
 * @author Mateusz Praski
 */
public interface IObserverKilled {
    void killed(Animal a);
}
