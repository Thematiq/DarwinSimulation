package engine.handlers;

import engine.objects.Grass;
import engine.objects.Herd;
import engine.tools.Vector;

import java.util.HashMap;
import java.util.Map;

public class SimulationMap {
    public final int width;
    public final int height;

    private Map<Vector, Herd> animals = new HashMap<>();
    private Map<Vector, Grass> bushes = new HashMap<>();

    public SimulationMap(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
