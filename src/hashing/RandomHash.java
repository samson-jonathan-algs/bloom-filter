package hashing;

import java.util.Random;

public class RandomHash {

    private int range;
    private int a;
    private int b;
    private double decider;

    public RandomHash(int m, int n, int b) {
        this.range = m*n*b;

        Random rand = new Random();

        this.a = rand.nextInt(100);
        this.b = rand.nextInt(range);

        this.decider = rand.nextDouble();

    }

    @Override
    public String toString() {
        if (decider < 1) {
            return String.format("h(x) = (%dx + %d) %% %d", a, b, range);
        }
        return "";
    }

    public int hash(int num) {
        if (decider < 1) {
            return ((num*a)+b)%range;
        }
        return 0;
    }
}
