package example.effective_java.item05.goods.dependencyinjection;

import example.effective_java.item05.goods.Dictionary;
import java.util.List;

public class SpellChecker {

    private final Dictionary dictionary;

    // 의존성 주입
    // 자원을 직접 명시하지 않고, 외부로부터 주입받을 수 있도록 한다.
    public SpellChecker(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    // 사전 종류(Dictionary)가 바뀐다고 한더라도, 코드 재사용이 가능하다.
    // 단, Dictionary는 인터페이스이어야 한다.
    // 인터페이스가 아니면 규약이 없기 때문에, 사전 종류 별로 다른 메서드를 가질 수 있기 때문이다.
    public boolean isValid(String word) {
        return dictionary.contains(word);
    }

    public List<String> suggestions(String typo) {
        return dictionary.closeWordsTo(typo);
    }
}
