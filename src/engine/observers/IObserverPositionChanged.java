package engine.observers;

import engine.objects.Animal;
import engine.tools.Vector;

public interface IObserverPositionChanged {
    void positionChanged(Vector oldPos, Vector newPos, Animal caller);
}
