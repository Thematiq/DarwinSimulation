package engine.objects;

import engine.handlers.IObserverKilled;
import engine.handlers.IObserverPositionChanged;
import engine.tools.Parameters;
import engine.tools.Vector;

import java.util.*;

public class Herd implements IObserverKilled {
    Comparator<Animal> animalComparator = Comparator.comparing(Animal::getEnergy);
    TreeSet<Animal> animals = new TreeSet<Animal>(animalComparator);
    Random rand = new Random();
    final Parameters params;

    public Herd(Parameters params) {
        this.params = params;
    }

    public boolean empty() {
        return this.animals.size() == 0;
    }

    public Animal getAnimal() {
        if (!this.empty()) {
            return animals.last();
        } else {
            return null;
        }
    }

    public void addAnimal(Animal a) {
        a.addKilledObserver(this);
        this.animals.add(a);
    }

    List<Animal> getStrongest() {
        List<Animal> strongest = new ArrayList<>();
        Iterator<Animal> iterator = this.animals.descendingIterator();
        int maxEnergy = this.animals.last().getEnergy();
        Animal a;
        while(iterator.hasNext()) {
            a = iterator.next();
            if(a.getEnergy() < maxEnergy) {
                break;
            }
            strongest.add(a);
        }
        return strongest;
    }

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

    public boolean eatGrass(int energy) {
        if (this.empty()) {
            return false;
        }
        List<Animal> strongest = this.getStrongest();
        int unitPerAnimal = energy / strongest.size();
        for(Animal a : strongest) {
            a.eat(unitPerAnimal);
        }
        return true;
    }

    public Animal makeLove() {
        if (this.animals.size() < 2) {
            return null;
        }
        Animal[] couple = this.getStrongestCouple();
        if(couple[1].getEnergy() < 0.5 * this.params.startEnergy) {
            return null;
        }
        return new Animal(couple[0], couple[1]);
    }

    @Override
    public void killed(Animal a) {
        this.animals.remove(a);
    }

    public void removeAnimal(Animal a) {
        this.animals.remove(a);
    }
}
