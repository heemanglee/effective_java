package example.effective_java.item05.goods.factorymethod;

import example.effective_java.item05.goods.Dictionary;
import java.util.List;

// 클리이언트 코드
public class SpellChecker {

    private Dictionary dictionary; // 추상화

    // factory 구현체를 통해 구현체 주입
    // 구현체가 바뀌더라도 클라이언트 코드에 영향 X
    public SpellChecker(DictionaryFactory dictionaryFactory) {
        this.dictionary = dictionaryFactory.getDictionary();
    }

    public boolean isValid(String word) {
        return dictionary.contains(word);
    }

    public List<String> suggestions(String typo) {
        return dictionary.closeWordsTo(typo);
    }

}