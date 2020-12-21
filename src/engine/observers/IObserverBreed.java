package engine.observers;

import engine.objects.Animal;

/**
 * Animal.breed() method observer
 * @author Mateusz Praski
 */
public interface IObserverBreed {
    void breed(Animal a, Animal child);
}
