package example.effective_java.item05.goods.springioc;

import example.effective_java.item05.goods.Dictionary;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SpringDictionary implements Dictionary {

    @Override
    public boolean contains(String word) {
        System.out.println("springdictionary contains");
        return false;
    }

    @Override
    public List<String> closeWordsTo(String typo) {
        return List.of();
    }
}
