package example.effective_java.item03.goods.method.factory;

public class Main {

    public static void main(String[] args) {
        // 메서드(getInstance)로 싱글톤 객체를 받게 되면,
        // 메서드 동작을 바꾸더라도 클라이언트 코드는 수정하지 않아도 된다.
        Elvis elvis = Elvis.getInstance();
        elvis.leaveTheBuilding();
    }
}
