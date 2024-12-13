package example.effective_java.item09.custom;

public class UseResourceMain {

    public static void main(String[] args) {
        try (CustomResource resource = new CustomResource()) {
            resource.doSomething();
        }
    }
}
