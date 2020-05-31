package algorithm;

import java.util.Random;

public class DataStructment {
    private int a, b, c;

    public DataStructment(Random random) {
        this.a = random.nextInt(20);
        this.b = random.nextInt(20);
        this.c = random.nextInt(20);
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public int getC() {
        return c;
    }
}
