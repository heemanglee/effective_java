package example.effective_java.item05.bads.staticutils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import example.effective_java.item05.bads.staticutils.SpellChecker;
import org.junit.jupiter.api.Test;

class SpellCheckerTest {

    @Test
    void isValid() {
        assertTrue(SpellChecker.isValid("test"));
    }

}