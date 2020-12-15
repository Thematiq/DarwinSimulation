package engine.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Genome {
    public static final int GENOME_SIZE = 32;
    Random r = new Random();
    int[] code;

    public Genome(int[] code) {
        if (code.length != GENOME_SIZE) {
            throw new IllegalArgumentException("Genome length should be 32!");
        }
        if (!checkGenome(code)) {
            throw new IllegalArgumentException("Genome doesn't have every value!");
        }
        this.code = code;
        Arrays.sort(this.code);
    }

    public Genome() {
        this.code = new int[GENOME_SIZE];
        for (int i = 0; i < 8; ++i) {
            this.code[i] = i;
        }
        for (int i = 8; i < Genome.GENOME_SIZE; ++i) {
            this.code[i] = r.nextInt(8);
        }
        Arrays.sort(this.code);
    }

    public Genome(Genome father, Genome mother) {
        this.code = new int[GENOME_SIZE];
        int[] cuts = {
                0, 0, 0, GENOME_SIZE
        };

        cuts[1] = r.nextInt(GENOME_SIZE);
        cuts[2] = 1 + cuts[1] + r.nextInt(GENOME_SIZE - cuts[1] - 1);
        // Take 2 sequences from father
        int motherSequence = r.nextInt(3);
        for (int i = 0; i < 4; ++i) {
            for (int pos = cuts[i]; pos < cuts[i+1]; ++i) {
                if (motherSequence == i) {
                    this.code[pos] = mother.code[pos];
                } else {
                    this.code[pos] = father.code[pos];
                }
            }
        }
        Arrays.sort(this.code);
    }

    static boolean checkGenome(int[] code) {
        boolean[] numbers = new boolean[8];
        for (int c : code) { numbers[c] = true; }
        for (boolean b : numbers) { if (!b) return false; }
        return true;
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
                        this.code[newPos] = i;
                        counters[this.code[newPos]]--;
                        break;
                    }
                }
            }
        }
    }

    public Orientation getRotation() {
        return Orientation.getOrient(this.code[r.nextInt(GENOME_SIZE)]);
    }
}
