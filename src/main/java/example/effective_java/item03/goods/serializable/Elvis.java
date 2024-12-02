package example.effective_java.item03.goods.serializable;

import java.io.Serializable;

public class Elvis implements Serializable {

    // 싱글톤 오브젝트
    public static final Elvis INSTANCE = new Elvis();
    private static final long serialVersionUID = 1L;

    private Elvis() {}

    public void leaveTheBuilding() {
        System.out.println("Whoa Baby, I'm outta here!");
    }

    public void sing() {
        System.out.println("I'll have a blue~ Christmas without you~");
    }

    // 역직렬화할 때, 항상 동일한 인스턴스를 반환하도록 할 수 있다.
    private Object readResolve() {
        return INSTANCE; // 기존의 싱글톤 인스턴스를 반환한다.
    }
}
