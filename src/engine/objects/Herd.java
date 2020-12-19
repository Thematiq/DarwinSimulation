package engine.objects;

import engine.observers.IObserverKilled;
import engine.tools.Parameters;

import java.util.*;

/**
 * Class representing Herd (a group of animals on a single tile) and handling events on a tile
 * @author Mateusz Praski
 */
public class Herd implements IObserverKilled {
    private final Parameters params;
    private final Random rand = new Random();
    private final Comparator<Animal> animalComparator = Comparator.comparing(Animal::getEnergy);
    private final TreeSet<Animal> animals = new TreeSet<>(animalComparator);

    /**
     * Creates new herd
     * @param params Simulation parameters
     */
    public Herd(Parameters params) {
        this.params = params;
    }

    /**
     * Handles eating grass on a tile
     * [Note] This method assumes that tile has grass
     * @return True if there are animals that could eat grass
     */
    public boolean eatGrass() {
        if (this.empty()) {
            return false;
        }
        List<Animal> strongest = this.getStrongest();
        int unitPerAnimal = this.params.plantEnergy / strongest.size();
        for(Animal a : strongest) {
            a.eat(unitPerAnimal);
        }
        return true;
    }

    /**
     * Handles breeding on a tile
     * @return New animal or null if there are no available parents on a tile
     */
    public Animal makeLove() {
        if (this.animals.size() < 2) {
            return null;
        }
        Animal[] couple = this.getStrongestCouple();
        if(couple[1].getEnergy() < 0.5 * this.params.startEnergy) {
            return null;
        }
        Animal child =  new Animal(couple[0], couple[1]);
        couple[0].breed(child);
        couple[1].breed(child);
        return child;
    }

    /**
     *
     * @return True if herd is empty
     */
    public boolean empty() {
        return this.animals.size() == 0;
    }

    /**
     * Removes Animal from the tile
     * @param a Animal
     */
    public void removeAnimal(Animal a) {
        this.animals.remove(a);
    }

    /**
     * Adds Animal to the tile
     * @param a Animal
     */
    public void addAnimal(Animal a) {
        a.addKilledObserver(this);
        this.animals.add(a);
    }

    /**
     *
     * @return Strongest Animal on a tile
     */
    public Animal getAnimal() {
        if (!this.empty()) {
            return animals.last();
        } else {
            return null;
        }
    }

    /**
     *
     * @return List of strongest Animals on the tile
     */
    List<Animal> getStrongest() {
        List<Animal> strongest = new ArrayList<>();
        Iterator<Animal> iterator = this.animals.descendingIterator();
        int maxEnergy = this.animals.last().getEnergy();
        Animal a;
        while(iterator.hasNext()) {
            a = iterator.next();
            if (a.getEnergy() < maxEnergy) {
                break;
            }
            strongest.add(a);
        }
        return strongest;
    }

    /**
     *
     * @return 2 elements Array of Animals containing potential parents
     */
    Animal[] getStrongestCouple() {
        List<Animal> strongest = this.getStrongest();
        Animal first;
        Animal second;
        if(strongest.size() > 2) {
            first = strongest.get(this.rand.nextInt(strongest.size()));
            strongest.remove(first);
            second = strongest.get(this.rand.nextInt(strongest.size()));
        } else if (strongest.size() == 2) {
            first = strongest.get(0);
            second = strongest.get(1);
        } else {
            Iterator<Animal> iterator = this.animals.descendingIterator();
            first = iterator.next();
            second = iterator.next();
        }
        return new Animal[]{first, second};
    }

    @Override
    public void killed(Animal a) {
        this.animals.remove(a);
    }
}
