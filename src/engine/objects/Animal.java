package engine.objects;

import engine.handlers.IObserverPositionChanged;
import engine.tools.Genome;
import engine.tools.Orientation;
import engine.tools.Parameters;
import engine.tools.Vector;

import java.util.ArrayList;
import java.util.List;

public class Animal {
    int energy;
    Vector pos;
    Genome genes;
    Orientation orient;
    List<IObserverPositionChanged> observes = new ArrayList<>();

    public Animal(Vector pos) {
        this.genes = new Genome();
        this.pos = pos;
        this.orient = this.genes.getRotation();
    }

    public void addObserver(IObserverPositionChanged obs) {
        this.observes.add(obs);
    }

    public void move(Vector newPos) {

        for(IObserverPositionChanged o : this.observes) {
            o.positionChanged(this.pos, newPos, this);
        }
        this.pos = newPos;
    }

    public Orientation getOrient() {
        return this.orient;
    }

    public Vector getPos() {
        return this.pos;
    }
}
