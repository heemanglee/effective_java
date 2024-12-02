package example.effective_java.item03.goods.method.supplier;

public class Elvis implements Singer {

    private static final Elvis INSTANCE = new Elvis();

    private Elvis() {
    }

    public static Elvis getInstance() {
        return INSTANCE;
    }

    @Override
    public void sing() {
        System.out.println("my sing~~");
    }
}
