import java.util.Random;

public class TriangleData {
    private int a, b, c;

    public TriangleData(Random random) {
        a = random.nextInt(20);
        b = random.nextInt(20);
        c = random.nextInt(20);
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
