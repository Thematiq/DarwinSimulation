package engine.tools;

public class Parameters {
    public final int width;
    public final int height;
    public final int startEnergy;
    public final int moveEnergy;
    public final int plantEnergy;
    public final float jungleRatio;
    public final int startingAnimals;

    public Parameters() {
        this(10, 10, 50, 2, 10, (float)0.5, 10);
    }

    public Parameters(int width, int height, int startEnergy, int moveEnergy, int plantEnergy, float jungleRatio, int startingAnimals) {
        this.width = width;
        this.height = height;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;
        this.startingAnimals = startingAnimals;
    }

    /*
    public Parameters(String filepath) {

    }
    */
}
