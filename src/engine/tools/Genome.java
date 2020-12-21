package engine.tools;

import java.util.*;

/**
 * Genome implementation
 * @author Mateusz Praski
 */
public class Genome {
    public static final int GENOME_SIZE = 32;
    private final Random r = new Random();
    private final int[] code;

    private int[] codePopularity;

    /**
     * Generates new random Genome
     */
    public Genome() {
        this.code = new int[GENOME_SIZE];
        for (int i = 0; i < 8; ++i) {
            this.code[i] = i;
        }
        for (int i = 8; i < Genome.GENOME_SIZE; ++i) {
            this.code[i] = r.nextInt(8);
        }
        this.repairGenome();
        Arrays.sort(this.code);
        this.calculatePopularity();
    }

    /**
     * Generates genome of parents code
     * @param father First genome
     * @param mother Second genome (order does not matter)
     */
    public Genome(Genome father, Genome mother) {
        List<Integer> fatherCode = new ArrayList<>();
        List<Integer> motherCode = new ArrayList<>();
        int motherSequence = r.nextInt(3);
        this.code = new int[GENOME_SIZE];
        int[] cuts = { 0, 0, 0, GENOME_SIZE };

        if (this.r.nextBoolean()) {
            Genome buffer = mother;
            mother = father;
            father = buffer;
        }

        for(int i = 0; i < GENOME_SIZE; ++i) {
            fatherCode.add(father.code[i]);
            motherCode.add(mother.code[i]);
        }

        Collections.shuffle(fatherCode);
        Collections.shuffle(motherCode);

        cuts[1] = 1 + r.nextInt(GENOME_SIZE - 2);
        do {
            cuts[2] = 1 + r.nextInt(GENOME_SIZE - 2);
        } while(cuts[2] == cuts[1]);

        if (cuts[2] < cuts[1]) {
            int buffer = cuts[1];
            cuts[1] = cuts[2];
            cuts[2] = buffer;
        }

        for (int i = 0; i < 3; ++i) {
            for (int pos = cuts[i]; pos < cuts[i+1]; ++pos) {
                if (motherSequence == i) {
                    this.code[pos] = motherCode.get(pos);
                } else {
                    this.code[pos] = fatherCode.get(pos);
                }
            }
        }

        this.repairGenome();
        Arrays.sort(this.code);
        this.calculatePopularity();
    }

    /**
     * Updates popularity array
     */
    private void calculatePopularity() {
        this.codePopularity = new int[8];
        for(int i = 0; i < GENOME_SIZE; ++i) {
            this.codePopularity[this.code[i]]++;
        }
    }

    /**
     * Repairs Genome if missing orientation
     */
    private void repairGenome() {
        if (checkGenome(this.code)) {
            return;
        }
        int[] counters = new int[8];
        for (int c : code) { counters[c]++; }
        for (int i = 0; i < 8; ++i) {
            if (counters[i] == 0) {
                while (true) {
                    int newPos = r.nextInt(GENOME_SIZE);
                    if (counters[this.code[newPos]] > 1) {
                        counters[this.code[newPos]]--;
                        this.code[newPos] = i;
                        break;
                    }
                }
            }
        }
    }

    /**
     * Checks whether given array misses some orientation
     * @param code Array of orientations int (does not need to be sorted)
     * @return True if code is correct
     */
    public static boolean checkGenome(int[] code) {
        boolean[] numbers = new boolean[8];
        for (int c : code) { numbers[c] = true; }
        for (boolean b : numbers) { if (!b) return false; }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder("/");
        for(int i = 0; i < 8; ++i) {
            ans.append("[").append(this.codePopularity[i]).append("]/");
        }
        return ans.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genome genome = (Genome) o;
        return Arrays.equals(code, genome.code);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(code);
    }

    public int[] getCodePopularity() {
        return this.codePopularity;
    }

    public Orientation getRotation() {
        return Orientation.getOrient(this.code[r.nextInt(GENOME_SIZE)]);
    }
}
