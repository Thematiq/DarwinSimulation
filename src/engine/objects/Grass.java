package engine.objects;


import engine.tools.Vector;

public class Grass {
    public final Vector pos;

    /** Spawns new Grass at a given tile
     *
     * @param pos Desired tile
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
