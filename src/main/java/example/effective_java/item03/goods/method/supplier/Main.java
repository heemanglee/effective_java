package example.effective_java.item03.goods.method.supplier;

public class Main {
    public static void main(String[] args) {
        Concert concert = new Concert();
        concert.start(Elvis::getInstance);
    }
}
