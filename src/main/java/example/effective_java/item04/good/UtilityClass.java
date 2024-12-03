package example.effective_java.item04.good;

public class UtilityClass {

    private UtilityClass() {
        // 클래스 내부에서 인스턴스화를 막는다.
        throw new AssertionError();
    }

    public static String hello() {
        return "hello";
    }

//    public static void main(String[] args) {
//        new UtilityClass();
//    }
}
