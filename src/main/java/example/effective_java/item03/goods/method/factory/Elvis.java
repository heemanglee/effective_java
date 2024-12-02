package example.effective_java.item03.goods.method.factory;

public class Elvis {

    // 싱글톤 오브젝트
    private static final Elvis INSTANCE = new Elvis();
    public static Elvis getInstance() {
        return INSTANCE;
    }

    private Elvis() {}

    public void leaveTheBuilding() {
        System.out.println("Whoa Baby, I'm outta here!");
    }

    public void sing() {
        System.out.println("I'll have a blue~ Christmas without you~");
    }
}
