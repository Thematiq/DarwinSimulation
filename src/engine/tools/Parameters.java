package engine.tools;

import java.util.Objects;

/**
 * Data structure containing simulation parameters
 * @author Mateusz Praski
 */
public class Parameters {
    public final int width;
    public final int height;
    public final int startEnergy;
    public final int moveEnergy;
    public final int plantEnergy;
    public final float jungleRatio;
    public final int startingAnimals;

    public Parameters() {
        this(10, 10, 100, 1, 30, (float)0.5, 10);
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

    @Override
    public String toString() {
        return "Map: " + this.width + "x" + this.height + "\n" +
                "Energy: " + this.startEnergy + "/" + this.moveEnergy + "/" + this.plantEnergy + "\n" +
                "Jungle ratio: " + this.jungleRatio + ", Starting animals: " + this.startingAnimals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parameters that = (Parameters) o;
        return width == that.width &&
                height == that.height &&
                startEnergy == that.startEnergy &&
                moveEnergy == that.moveEnergy &&
                plantEnergy == that.plantEnergy &&
                Float.compare(that.jungleRatio, jungleRatio) == 0 &&
                startingAnimals == that.startingAnimals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height, startEnergy, moveEnergy, plantEnergy, jungleRatio, startingAnimals);
    }
}
