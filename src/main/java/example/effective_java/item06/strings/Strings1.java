package example.effective_java.item06.strings;

public class Strings1 {

    public static void main(String[] args) {
        String s1 = "hello"; // String Constant Pool
        String s2 = new String("hello"); // JVM Heap
        String s3 = "hello"; // String Constant Pool

        // JVM은 문자열 값을 문자열 상수 풀에 저장하여, 참조하는 방식으로 재사용한다.
        // 문자열 리터털로 문자열을 생성한 경우 문자열 상수 풀에 저장되지만,
        // new 키워드로 생성한 경우 매 번 새로운 객체를 생성하여 JVM Heap 메모리에 저장한다.

        // s1은 문자열 상수 풀에 저장된 문자열을 참조하고, s2는 힙 메모리에 할당된 주소를 가리킨다.
        System.out.println(s1 == s2); // false

        // s1과 s3 둘 다 문자열 상수 풀에 저장된 문자열을 참조한다.
        System.out.println(s1 == s3); // true

        // String 클래스는 Object 클래스의 equals를 문자열 값을 비교하도록 재정의하였다.
        System.out.println(s1.equals(s2)); // true
        System.out.println(s1.equals(s3)); // true
    }
}

