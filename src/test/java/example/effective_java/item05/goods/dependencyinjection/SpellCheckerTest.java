package example.effective_java.item05.goods.dependencyinjection;

import static org.junit.jupiter.api.Assertions.assertTrue;

import example.effective_java.item05.goods.DefaultDictionary;
import example.effective_java.item05.goods.Dictionary;
import org.junit.jupiter.api.Test;

class SpellCheckerTest {

    @Test
    void isValid() {
        Dictionary dictionary = new DefaultDictionary();
        SpellChecker spellChecker = new SpellChecker(dictionary); // 의존성 주입
        assertTrue(spellChecker.isValid("test"));
    }

}