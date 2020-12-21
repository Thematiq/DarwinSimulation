package engine.objects;

import engine.observers.IObserverEnergyChanged;
import engine.observers.IObserverKilled;
import engine.tools.Parameters;

import java.util.*;

/**
 * Class representing Herd (a group of animals on a single tile) and handling events on a tile
 * @author Mateusz Praski
 */
public class Herd implements IObserverKilled, IObserverEnergyChanged {
    final Parameters params;
    final Random rand = new Random();
    final TreeSet<Integer> energies = new TreeSet<>();
    final Map<Integer, List<Animal>> animals = new HashMap<>();

    int herdSize = 0;

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
        List<Animal> strongest = new ArrayList<>(this.getStrongest());
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
        return this.herdSize == 0;
    }

    /**
     * Removes Animal from the tile
     * @param a Animal
     */
    public void removeAnimal(Animal a) {
        this.animals.get(a.getEnergy()).remove(a);
        if (this.animals.get(a.getEnergy()).size() == 0) {
            this.animals.remove(a.getEnergy());
            this.energies.remove(a.getEnergy());
        }
        this.herdSize--;
        a.removeEnergyObserver(this);
        a.removeKilledObserver(this);
    }

    /**
     * Adds Animal to the tile
     * @param a Animal
     */
    public void addAnimal(Animal a) {
        this.animals.computeIfAbsent(a.getEnergy(), t -> new ArrayList<>());
        this.energies.add(a.getEnergy());
        this.herdSize++;
        this.animals.get(a.getEnergy()).add(a);
        a.addKilledObserver(this);
        a.addEnergyObserver(this);
    }

    /**
     * @return Strongest Animal on a tile
     */
    public Animal getAnimal() {
        if (this.herdSize == 0) {
            return null;
        }
        List<Animal> a = this.animals.get(this.energies.last());
        return a.get(this.rand.nextInt(a.size()));
    }

    /**
     * @return List of strongest Animals on the tile
     */
    List<Animal> getStrongest() {
        return this.animals.get(this.energies.last());
    }

    /**
     * @return 2 elements Array of Animals containing potential parents
     */
    Animal[] getStrongestCouple() {
        Animal father;
        Animal mother;
        if (herdSize < 2) {
            return null;
        }
        List<Animal> a = getStrongest();
        if (a.size() > 2) {
            father = a.get(this.rand.nextInt(a.size()));
            do {
                mother = a.get(this.rand.nextInt(a.size()));
            } while(mother == father);
            return new Animal[] {father, mother};
        } else if (a.size() == 2) {
            return new Animal[] {a.get(0), a.get(1) };
        } else {
            father = a.get(0);
            Iterator<Integer> i = this.energies.descendingIterator();
            i.next(); // drop strongest
            a = this.animals.get(i.next());
            mother = a.get(this.rand.nextInt(a.size()));
            return new Animal[] {father, mother};
        }
    }

    @Override
    public void killed(Animal a) {
        this.removeAnimal(a);
    }

    @Override
    public void energyChanged(Animal caller, int oldVal) {
        this.animals.get(oldVal).remove(caller);
        if (this.animals.get(oldVal).size() == 0) {
            this.animals.remove(oldVal);
            this.energies.remove(oldVal);
        }
        this.animals.computeIfAbsent(caller.getEnergy(), t -> new ArrayList<>());
        this.animals.get(caller.getEnergy()).add(caller);
        this.energies.add(caller.getEnergy());
    }
}
