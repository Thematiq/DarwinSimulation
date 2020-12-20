package engine.observers;

import engine.objects.Animal;

public interface IObserverEnergyChanged {
    void energyChanged(Animal caller, int oldVal);
}
