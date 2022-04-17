package hashing;

import java.util.Random;

public class RandomHash {

    private int range;
    private int a;
    private int b;
    private int prime;
    private double decider;

    public RandomHash(int m, int n, int b, int prime) {
        this.range = m*n;//*b;
        this.prime = prime;

        Random rand = new Random();

        this.a = (rand.nextInt((prime - 1))) + 1;
        // 1 <= a <= p-1
        this.b = rand.nextInt(prime);
        // 0 <= b <= p-1

        this.decider = rand.nextDouble();

    }

    @Override
    public String toString() {
        if (decider < 1) {
            return String.format("h(k) = ((%dk + %d) %% %d) %% %d", a, b, prime, range);
        }
        return "";
    }

    public int hash(int num) {
        if (decider < 1) {
            return (((num*a)+b)%prime)%range;
        }
        return 0;
    }
}
