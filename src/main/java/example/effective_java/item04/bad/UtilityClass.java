package example.effective_java.item04.bad;

public abstract class UtilityClass {

    public UtilityClass() {
        System.out.println("UtilityClass Default Constructor Call");
    }

    public static String hello() {
        return "hello";
    }
}
