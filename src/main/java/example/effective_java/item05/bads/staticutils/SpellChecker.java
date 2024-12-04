package example.effective_java.item05.bads.staticutils;

import example.effective_java.item05.bads.Dictionary;
import java.util.List;

public class SpellChecker {

    // new 키워드를 사용하여 자원을 직접 명시
    private static final Dictionary dictionary = new Dictionary();

    private SpellChecker() {
        throw new AssertionError();
    }

    public static boolean isValid(String word) {
        return dictionary.contains(word);
    }

    public static List<String> suggestions(String typo) {
        return dictionary.closeWordsTo(typo);
    }

}
