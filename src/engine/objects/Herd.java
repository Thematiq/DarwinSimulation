package engine.objects;

import java.util.ArrayList;
import java.util.List;

public class Herd {
    List<Animal> animals = new ArrayList<>();


    public boolean empty() {
        return this.animals.size() == 0;
    }
}
