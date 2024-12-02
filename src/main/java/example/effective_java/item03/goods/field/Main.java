package example.effective_java.item03.goods.field;

public class Main {

    public static void main(String[] args) {
        IElvis elvis = Elvis.INSTANCE;
        elvis.leaveTheBuilding();
    }
}
