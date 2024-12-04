package example.effective_java.item05.goods.factorymethod;

import example.effective_java.item05.goods.Dictionary;
import example.effective_java.item05.goods.MockDictionary;

// MockDictionary를 만드는 Factory
public class MockDictionaryFactory implements DictionaryFactory {

    @Override
    public Dictionary getDictionary() {
        return new MockDictionary();
    }

}
