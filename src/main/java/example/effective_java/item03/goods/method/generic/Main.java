package example.effective_java.item03.goods.method.generic;

public class Main {

    public static void main(String[] args) {
        Elvis<String> elvis1 = Elvis.getInstance();
        Elvis<Integer> elvis2 = Elvis.getInstance();

        // 제네릭 타입이 다르더라도 해시 코드가 동일하다.
        System.out.println(elvis1); // Elvis@41a4555e
        System.out.println(elvis2); // Elvis@41a4555e

        // 해시 코드가 같고, 같은 인스턴스임에도 불구하고
        // 제네릭 타입이 다르므로, 동일성 비교가 불가능하다.
//        System.out.println(elvis1 == elvis2);

        System.out.println(elvis1.equals(elvis2)); // true
    }
}
