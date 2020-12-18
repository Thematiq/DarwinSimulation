package engine.observers;

import engine.objects.Animal;
import engine.tools.Vector;

/**
 * Observer for a Animal.move() method
 * @author Mateusz Praski
 */
public interface IObserverPositionChanged {
    void positionChanged(Vector oldPos, Vector newPos, Animal caller);
}
