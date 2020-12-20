package engine.tools;

public class Stats {
    int from;
    int to;
    int living;
    int dead;
    int vegetation;
    int meanEnergy;
    int meanChildren;
    Genome dominating;
    int lifespan;

    public Stats() { }

    public int getLiving() {
        return living;
    }

    public int getDead() {
        return dead;
    }

    public int getVegetation() {
        return vegetation;
    }

    public int getMeanEnergy() {
        return meanEnergy;
    }

    public int getMeanChildren() {
        return meanChildren;
    }

    public Genome getDominating() {
        return dominating;
    }

    public int getLifespan() {
        return lifespan;
    }
}
