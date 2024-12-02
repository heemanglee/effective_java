package example.effective_java.item03.bads.field;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ConcertTest {

    @Test
    void perform() {
        // 실제 객체를 가지고 테스트를 수행해야 한다.
        Concert concert = new Concert(Elvis.INSTANCE);
        concert.perform(); // 리허설

        assertTrue(concert.isLightsOn());
        assertTrue(concert.isMainStateOpen());
    }

}