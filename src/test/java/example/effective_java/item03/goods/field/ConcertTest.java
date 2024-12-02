package example.effective_java.item03.goods.field;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ConcertTest {

    @Test
    void perform() {
        // 실제 객체가 아닌 대역(모킹)을 사용할 수 있다.
        // 대역(목 객체)를 사용하여 실제 객체를 사용할 때의 비용을 줄일 수 있다.
        Concert concert = new Concert(new MockElvis());
        concert.perform(); // 대역이 리ㅎ

        assertTrue(concert.isLightsOn());
        assertTrue(concert.isMainStateOpen());
    }

}