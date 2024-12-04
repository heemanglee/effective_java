package example.effective_java.item05.goods;

import java.util.List;

public class DefaultDictionary implements Dictionary {

    @Override
    public boolean contains(String word) {
        return true;
    }

    @Override
    public List<String> closeWordsTo(String typo) {
        return List.of();
    }
}
