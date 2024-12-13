package example.effective_java.item09.custom;

public class CustomResource implements AutoCloseable {

    public void doSomething() {
        System.out.println("작업 ing...");
    }

    @Override
    public void close() {
        System.out.println("CustomResource 자원 반납");
    }
}
