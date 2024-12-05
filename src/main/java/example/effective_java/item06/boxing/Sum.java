package example.effective_java.item06.boxing;

public class Sum {

    // 오토 박싱과 언박싱은 JVM이 런타임 시점에 처리한다.
    // AutoBoxing :  Primitive -> Wrappper
    // UnBoxing : Wrapper -> Primitive
    private static long sumBoxing() {
        Long sum = 0L;
        for (long i = 0; i < Integer.MAX_VALUE; i++) {
            sum += i; // 불필요한 오토 박싱 발생
        }
        return sum;
    }

    private static long sumNotBoxing() {
        long sum = 0L;
        for (long i = 0; i < Integer.MAX_VALUE; i++) {
            sum += i; // primitive 연산
        }
        return sum;
    }

    public static void main(String[] args) {
        long start2 = System.nanoTime();
        long x2 = sumBoxing(); // 성능 비교를 하려면 sumNotBoxing()으로 변경.
        long end2 = System.nanoTime();
        System.out.println(end2 - start2 + " ns"); // 2277700375 ns
    }
}
