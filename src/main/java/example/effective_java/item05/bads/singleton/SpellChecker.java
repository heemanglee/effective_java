package example.effective_java.item05.bads.singleton;

import example.effective_java.item05.bads.Dictionary;
import java.util.List;

public class SpellChecker {

    // new 키워드를 사용하여 자원을 직접 명시
    private final Dictionary dictionary = new Dictionary();

    private SpellChecker() {
    }

    public static final SpellChecker INSTANCE = new SpellChecker();

    public boolean isValid(String word) {
        return dictionary.contains(word);
    }

    public List<String> suggestions(String typo) {
        return dictionary.closeWordsTo(typo);
    }

}
