package example.effective_java.item06.patterns;

import java.util.regex.Pattern;

public class RomanNumerals {

    static final int COUNT = 100_000;

    static boolean isRomanNumeralSlow(String s) {
        return s.matches("^(?=.)M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[VX]|V?I{0,3})$");
    }

    private static final Pattern ROMAN = Pattern.compile(
            "^(?=.)M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[VX]|V?I{0,3})$"
    );

    static boolean isRomanNumeralFast(String s) {
        return ROMAN.matcher(s).matches();
    }

    public static void main(String[] args) {
        boolean result = false;
        long start = System.nanoTime();
        for (int j = 0; j < COUNT; j++) {
            // 성능 차이를 확인하려면 xxxSlow 메서드로 변경
            result = isRomanNumeralFast("MCMLXXVI");
        }
        long end = System.nanoTime();
        System.out.println(end - start + " ns"); // 31443833 ns
        System.out.println(result);
    }
}
