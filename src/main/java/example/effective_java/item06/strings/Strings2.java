package example.effective_java.item06.strings;

public class Strings2 {

    static final int COUNT = 100_000;

    public static void main(String[] args) {
        // new 키워드로 문자열을 생성한다.
        String s = "hello";
        long start = System.nanoTime();
        for (int i = 0; i < COUNT; i++) {
            String tmp = new String("hello");
        }
        long end = System.nanoTime();
        System.out.println(end - start + " ns"); // 1647750 ns

        // 문자열 리터럴 방식으로 캐싱된 문자열을 참조한다.
        String s2 = "hello";
        long start2 = System.nanoTime();
        for (int i = 0; i < COUNT; i++) {
            String tmp = "hello";
        }
        long end2 = System.nanoTime();
        System.out.println(end2 - start2 + " ns"); // 549042 ns
    }
}

