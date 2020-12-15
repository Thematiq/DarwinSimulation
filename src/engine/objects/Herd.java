package engine.objects;

import java.util.ArrayList;
import java.util.List;

public class Herd {
    List<Animal> animals = new ArrayList<>();


    public boolean empty() {
        return this.animals.size() == 0;
    }

    public Animal getAnimal() {
        if (!this.empty()) {
            return animals.get(0);
        } else {
            return null;
        }
    }

    public void addAnimal(Animal a) {
        this.animals.add(a);
    }
}
