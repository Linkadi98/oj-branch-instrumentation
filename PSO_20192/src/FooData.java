import java.util.Random;

public class FooData {

    private int a;

    public FooData(Random random) {
        this.a = random.nextInt(20);
    }

    public int getA() {
        return a;
    }
}
