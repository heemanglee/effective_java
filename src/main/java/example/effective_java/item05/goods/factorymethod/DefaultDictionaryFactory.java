package example.effective_java.item05.goods.factorymethod;

import example.effective_java.item05.goods.DefaultDictionary;
import example.effective_java.item05.goods.Dictionary;

// DefaultDictionary를 만드는 Factory
public class DefaultDictionaryFactory implements DictionaryFactory {

    @Override
    public Dictionary getDictionary() {
        return new DefaultDictionary();
    }

}
