package engine.handlers;

import engine.tools.Vector;

public interface IObserverPositionChanged {
    void positionChanged(Vector oldPos, Vector newPos);
}
