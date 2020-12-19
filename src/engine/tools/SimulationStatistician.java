package engine.tools;

import engine.handlers.Simulation;
import engine.objects.Animal;
import engine.observers.IObserverKilled;
import engine.observers.IObserverNewDay;
import engine.observers.IObserverSimulationStatistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulationStatistician implements IObserverNewDay, IObserverKilled {

    private final List<Integer> livingAnimals = new ArrayList<>();
    private final List<Integer> deadAnimals = new ArrayList<>();
    private final List<Integer> vegetation = new ArrayList<>();
    private final List<Integer> meanEnergy = new ArrayList<>();
    private final List<Integer> meanChildren = new ArrayList<>();
    private final List<Genome> dominatingGenome = new ArrayList<>();
    private final List<Integer> deadLifespans = new ArrayList<>();
    private final List<IObserverSimulationStatistics> observerStatistics = new ArrayList<>();
    private final List<Integer[]> genesPopularity = new ArrayList<>();
    private final List<Genome> deadGenes = new ArrayList<>();

    private long deadTotalLifespan = 0;
    private int currentDay;


    public SimulationStatistician(Simulation sim) {
        sim.addNewDayObserver(this);
    }

    public void addIObserverStatistics(IObserverSimulationStatistics observer) {
        this.observerStatistics.add(observer);
    }

    private void callObservers() {
        for (IObserverSimulationStatistics o : this.observerStatistics) {
            o.update(this);
        }
    }

    @Override
    public void dayChanged(int day, Simulation caller) {
        this.currentDay = caller.getDay();
        this.livingAnimals.add(caller.getPopulation());
        this.deadAnimals.add(caller.getGraveyard());
        this.vegetation.add(caller.getVegetation());
        List<Animal> currentAnimals = caller.getAnimalList();
        long totalEnergy = 0;
        long totalChildren = 0;
        for(Animal a : currentAnimals) {
            totalEnergy += a.getEnergy();
            totalChildren += a.getChildren();
        }

        if (caller.getGraveyard() != 0) {
            this.deadLifespans.add((int) (this.deadTotalLifespan / caller.getGraveyard()));
        } else {
            this.deadLifespans.add(0);
        }

        if (currentAnimals.size() != 0) {
            this.meanEnergy.add((int) (totalEnergy / currentAnimals.size()));
            this.meanChildren.add((int) (totalChildren / currentAnimals.size()));
        } else {
            this.meanEnergy.add(0);
            this.meanChildren.add(0);
        }

        this.dominatingGenome.add(this.evaluateMostPopularGenome(currentAnimals));
        this.genesPopularity.add(this.evaluateGenePopularity(currentAnimals));

        this.callObservers();
    }

    private Integer[] evaluateGenePopularity(List<Animal> animals) {
        int[] popularity = new int[8];
        int[] singlePop;
        for (Animal a : animals) {
            singlePop = a.getGenes().getCodePopularity();
            for (int i = 0; i < singlePop.length; ++i) {
                popularity[i] += singlePop[i];
            }
        }
        Integer[] ret = new Integer[8];
        for (int i = 0; i < 8; ++i) {
            ret[i] = popularity[i];
        }
        return ret;
    }

    private Genome evaluateMostPopularGenome(List<Animal> animals) {
        Map<Genome, Integer> pop = new HashMap<>();
        for(Animal a : animals) {
            Integer val = pop.get(a.getGenes());
            if (val == null) {
                val = 0;
            }
            pop.put(a.getGenes(), val+1);
        }
        Genome candidate = new Genome();
        int votes = -1;
        for (Genome a : pop.keySet()) {
            if (pop.get(a) > votes) {
                votes = pop.get(a);
                candidate = a;
            }
        }
        return candidate;
    }

    @Override
    public void killed(Animal a) {
        this.deadTotalLifespan += a.getLifespan();
        this.deadGenes.add(a.getGenes());
    }

    public int getCurrentAnimals() {
        return this.livingAnimals.get(this.livingAnimals.size() - 1);
    }

    public int getCurrentVegetation() {
        return this.vegetation.get(this.vegetation.size() - 1);
    }

    public int getCurrentGraveyard() {
        return this.deadAnimals.get(this.deadAnimals.size() - 1);
    }

    public int getCurrentEnergy() {
        return this.meanEnergy.get(this.meanEnergy.size() - 1);
    }

    public int getCurrentChildren() {
        return this.meanChildren.get(this.meanChildren.size() - 1);
    }

    // TODO
    // Lifespan works strange...
    public int getCurrentLifespan() {
        return this.deadLifespans.get(this.deadLifespans.size() - 1);
    }

    public Genome getCurrentDominant() {
        return this.dominatingGenome.get(this.dominatingGenome.size() - 1);
    }

    public Integer[] getGenePopularity() {
        return this.genesPopularity.get(this.genesPopularity.size() - 1);
    }

    public int getCurrentDay() { return this.currentDay; }
}
