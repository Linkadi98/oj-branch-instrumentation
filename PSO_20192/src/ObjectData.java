import java.util.Random;

public class ObjectData {
    private FooData fooData;

    public ObjectData () {
        fooData = new FooData(new Random());
    }

    public FooData getFooData() {
        return fooData;
    }
}
