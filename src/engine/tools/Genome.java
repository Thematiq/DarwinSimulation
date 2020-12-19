package engine.tools;

import java.util.*;

public class Genome {
    private static final Genome ERROR = new Genome(new int[]{25, 1, 1, 1, 1, 1, 1, 1});
    public static final int GENOME_SIZE = 32;
    private final Random r = new Random();
    private final int[] code;

    private int[] codePopularity;

    public Genome(int[] popularity) {
        if (popularity.length != 8) {
            throw new IllegalArgumentException("Popularity length should be 8!");
        }
        int sum = 0;
        for (int pop : popularity) {
            sum += pop;
            if (pop == 0) {
                throw new IllegalArgumentException("None popularity can be 0!");
            }
        }
        if (sum != GENOME_SIZE) {
            throw new IllegalArgumentException("Sum of popularities should be 32!");
        }
        this.codePopularity = popularity;
        this.code = new int[GENOME_SIZE];
        int j = 0;
        for(int i = 0; i < 8; ++i) {
            for(int u = 0; u < this.codePopularity[i]; ++u, ++j) {
                this.code[j] = i;
            }
        }
        Arrays.sort(this.code);
        this.calculatePopularity();
    }

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

    void calculatePopularity() {
        this.codePopularity = new int[8];
        for(int i = 0; i < GENOME_SIZE; ++i) {
            this.codePopularity[this.code[i]]++;
        }
    }

    void repairGenome() {
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

    static boolean checkGenome(int[] code) {
        boolean[] numbers = new boolean[8];
        for (int c : code) { numbers[c] = true; }
        for (boolean b : numbers) { if (!b) return false; }
        return true;
    }

    @Override
    public String toString() {
        String ans = "/";
        for(int i = 0; i < 8; ++i) {
            ans += "[" + this.codePopularity[i] + "] / ";
        }
        return ans;
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
