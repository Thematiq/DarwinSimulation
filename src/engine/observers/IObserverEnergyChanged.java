package engine.observers;

import engine.objects.Animal;

/**
 * Observer for various animal methods, which change energy level
 * @author Maetusz Praski
 */
public interface IObserverEnergyChanged {
    void energyChanged(Animal caller, int oldVal);
}
