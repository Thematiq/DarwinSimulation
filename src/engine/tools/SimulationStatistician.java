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
    private final Map<Integer, List<Genome>> geneGraveyard = new HashMap<>();

    private List<Animal> living;
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
        this.living = currentAnimals;
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
        return getGenome(pop);
    }

    private Genome evaluateMostPopularFromList(List<Genome> genes) {
        Map<Genome, Integer> pop = new HashMap<>();
        for(Genome g : genes) {
            Integer val = pop.get(g);
            if (val == null) {
                val = 0;
            }
            pop.put(g, val+1);
        }
        return getGenome(pop);
    }

    private Genome getGenome(Map<Genome, Integer> pop) {
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
        this.geneGraveyard.computeIfAbsent(this.currentDay + 1, k -> new ArrayList<>());
        this.geneGraveyard.get(this.currentDay+1).add(a.getGenes());
        this.deadTotalLifespan += a.getLifespan();
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

    public Stats getStats(int from, int to) {
        if (from > to || from > this.getCurrentDay() || to > this.getCurrentDay()) {
            throw new IllegalArgumentException();
        }
        Stats out = new Stats();
        List<Genome> genePool = new ArrayList<>();

        for (int i = from; i <= to; ++i) {
            out.living += this.livingAnimals.get(i);
            out.dead += this.deadAnimals.get(i);
            out.vegetation += this.vegetation.get(i);
            out.meanEnergy += this.meanEnergy.get(i);
            out.meanChildren += this.meanChildren.get(i);
            out.lifespan += this.deadLifespans.get(i);
            if (this.geneGraveyard.get(i) != null) {
                genePool.addAll(this.geneGraveyard.get(i));
            }
        }
        for(Animal a : this.living) {
            genePool.add(a.getGenes());
        }

        int duration = to - from + 1;
        out.living /= duration;
        out.dead /= duration;
        out.vegetation /= duration;
        out.meanEnergy /= duration;
        out.meanChildren /= duration;
        out.lifespan /= duration;
        out.dominating = this.evaluateMostPopularFromList(genePool);
        out.from = from;
        out.to = to;

        return out;
    }
}
