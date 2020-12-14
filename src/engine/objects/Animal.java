package engine.objects;

import engine.handlers.IObserverPositionChanged;
import engine.tools.Genome;
import engine.tools.Vector;

import java.util.ArrayList;
import java.util.List;

public class Animal {
    int energy;
    Vector pos;
    Genome genes;
    List<IObserverPositionChanged> observes = new ArrayList<>();

    public Animal(Vector pos) {
        this.pos = pos;
    }

    public void addObserver(IObserverPositionChanged obs) {
        this.observes.add(obs);
    }

    public void move(Vector newPos) {

        for(IObserverPositionChanged o : this.observes) {
            o.positionChanged(this.pos, newPos);
        }
        this.pos = newPos;
    }
}
