package example.effective_java.item03.goods.field;

// 1. public static final 필드 방식의 싱글톤
public class Elvis implements IElvis {

    // 싱글톤 오브젝트
    public static final Elvis INSTANCE = new Elvis();

    private Elvis() {}

    @Override
    public void leaveTheBuilding() {
        System.out.println("Whoa Baby, I'm outta here!");
    }

    @Override
    public void sing() {
        System.out.println("I'll have a blue~ Christmas without you~");
    }
}
