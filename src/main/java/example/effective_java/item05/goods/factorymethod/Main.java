package example.effective_java.item05.goods.factorymethod;

public class Main {
    public static void main(String[] args) {
        DictionaryFactory defaultDictionaryFactory = new DefaultDictionaryFactory();
        SpellChecker defaultSpellChecker = new SpellChecker(defaultDictionaryFactory);
        System.out.println(defaultSpellChecker.isValid("test"));

        DictionaryFactory mockDictionaryFactory = new DefaultDictionaryFactory();
        SpellChecker mockSpellChecker = new SpellChecker(mockDictionaryFactory);
        System.out.println(mockSpellChecker.isValid("test"));
    }
}
