package engine.objects;

import engine.tools.Vector;

/**
 * Class describing Grass object
 * @author Mateusz Praski
 */
public class Grass {
    final Vector pos;

    /**
     * Creates new Grass with a given position
     * @param pos desired position
     */
    public Grass(Vector pos) {
        this.pos = pos;
    }

    /**
     * @return Grass position
     */
    public Vector getPos() {
        return this.pos;
    }
}
