package example.effective_java.item05.goods.springioc;

import example.effective_java.item05.goods.Dictionary;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SpellChecker {

    private Dictionary dictionary;

    public SpellChecker(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public boolean isValid(String word) {
        return dictionary.contains(word);
    }

    public List<String> suggestions(String typo) {
        return dictionary.closeWordsTo(typo);
    }
}
