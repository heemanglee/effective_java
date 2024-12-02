package example.effective_java.item03.goods.method.generic;

public class Elvis<T> {

    // 싱글톤 오브젝트
    private static final Elvis<Object> INSTANCE = new Elvis<>();

    public static <T> Elvis<T> getInstance() {
        return (Elvis<T>) INSTANCE;
    }

    private Elvis() {
    }

    public void leaveTheBuilding() {
        System.out.println("Whoa Baby, I'm outta here!");
    }

    public void sing() {
        System.out.println("I'll have a blue~ Christmas without you~");
    }
}
