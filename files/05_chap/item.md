## Bad. 자원을 직접 명시하기

```java
public class SpellChecker {

		// new 키워드를 통해 직접 명시한다.
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
```

자원을 직접 명시하게 되면 다른 자원으로 변경할 수 없다. 

이는 테스트 코드 작성 시에 매우 불리하게 작용한다. Dictionary 생성 비용이 클 때, 테스트를 수행할 때마다 생성  비용이 큰 객체를 사용하게 된다. 이를 방지하기 위해 Mock 객체를 사용할 수 있겠으나, new 키워드를 통해 자원을 직접 명시했기 때문에 반드시 진짜 객체를 삽입해야 한다.

```java
class SpellCheckerTest {

    @Test
    void isValid() {
        assertTrue(SpellChecker.isValid("test"));
    }

}
```

## Good. 의존성 주입을 통해 유동적으로 인스턴스 삽입하기

```java
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

```

외부에서 의존성 주입을 통해 사용하고자 하는 객체를 주입할 수 있다. 사전 종류를 바꾸더라도, SpellChecker 내의 코드를 변경하지 않고 그대로 재사용할 수 있다.

자원을 직접 명시한 경우에는 테스트 코드 작성 시에 사용할 객체를 변경할 수 없었다. 

의존성 주입을 사용하게 되면 테스트에 특화된 객체(ex. Mock 객체)를 사용하여 테스트 비용을 낮출 수 있다. 객체를 유연하게 주입하여 테스트를 효율적으로 실행할 수 있다.

```java
class SpellCheckerTest {

    @Test
    void isValid() {
        Dictionary dictionary = new DefaultDictionary();
        SpellChecker spellChecker = new SpellChecker(dictionary); // 의존성 주입
        assertTrue(spellChecker.isValid("test"));
    }

}
```

## 팩토리 메서드 패턴

<img width="780" alt="%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-12-05_%E1%84%8B%E1%85%A9%E1%84%8C%E1%85%A5%E1%86%AB_2 34 43" src="https://github.com/user-attachments/assets/2341acf0-edaf-4113-b8c7-eae33c0f9c48">


인스턴스를 만드는 과정이 복잡하거나, 추상 클래스가 존재하고 여러 개의 구체 클래스가 존재할 때 팩토리 메서드 패턴을 적용할 수 있다.

팩토리 메서드 패턴을 적용하여 공통 코드는 재사용하되, 인스턴스마다 구체적인 코드는 다르게 할 수 있다.

쉽게 말하자면, 팩토리 메서드 패턴은 구체적인 인스턴스를 팩토리에서 만들도록 하여 반환하는 방법이다. 즉, 자동차라는 추상 클래스가 존재할 때, 현대 자동차를 만드는 팩토리 클래스에서 현대 자동차를 만들어서 반환하면 된다.

- 클라이언트 코드에서는 추상화된 팩토리를 사용하여, 새로운 팩토리 클래스가 만들어지더라도 클라이언트 코드에는 변경이 발생하지 않도록 할 수 있다. → `OCP 준수`

### 클라이언트 코드

```java
// 클리이언트 코드
public class SpellChecker {

    private Dictionary dictionary; // 추상화 클래스

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
```

클라이언트 코드에서는 외부에서 구체화된 팩토리 클래스를 주입받도록 하였다. DictionaryFactory의 구현체인 KoreanDictionaryFactory, EnglishDictionaryFactory 등 구현체를 만드는 팩토리 클래스를 주입하면 된다. 클라이언트 코드인 SpellChecker에서 기존에 한글 사전을 사용하다가 영어 사전으로 변경해야 한다면, 외부에서 KoreanDictionaryFactory가 아닌 EnglishDictionaryFactory를 주입하면 된다. → 클라이언트 코드는 변경할 코드가 없다.

### Factory interface

Dictionary 구현체를 반환하는 추상화된 팩토리 클래스이다.

```java
public interface DictionaryFactory {

    Dictionary getDictionary();

}
```

### 구체화된 Factory 클래스

추상화된 DictionaryFactory의 규격을 맞춰 DefaultDictionaryFactory 구현체를 반환하는 코드를 작성한다.

```java
// DefaultDictionary 구현체를 만드는 Factory
public class DefaultDictionaryFactory implements DictionaryFactory {

    @Override
    public Dictionary getDictionary() {
        return new DefaultDictionary();
    }

}
```

아래도 마찬 가지로 추상화된 DictionaryFactory의 규격을 맞춰 MockDictionaryFactory 구현체를 반환하는 코드를 작성한다.

```java
// MockDictionary를 만드는 Factory
public class MockDictionaryFactory implements DictionaryFactory {

    @Override
    public Dictionary getDictionary() {
        return new MockDictionary();
    }

}
```

### 팩토리 메서드 패턴은 OCP를 준수한다.

OCP란 확장엔 열려있고 수정엔 닫혀있는 방법이다.

새로운 사전 종류(Dictionary)를 만들 수 있되(확장엔 열려 있고), 클라이언트 코드는 변경되지 않는다.(수정엔 닫혀 있다)

```java
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

```

defaultDictionaryFactory에서 mockDictionaryFactory로 바꾸더라도 클라이언트 코드인 SpellChekcer에서는 코드 변경이 발생하지 않는다.  그 이유는, 외부에서 주입받는 클래스가  DictionaryFactory 추상화 클래스이기 때문이다. DictionaryFactory의 규격을 따르는 구현체이기만 하면 된다.

```java
public SpellChecker(DictionaryFactory dictionaryFactory) {
    this.dictionary = dictionaryFactory.getDictionary(); 
}
```

## 스프링 IoC

스프링 프레임워크를 사용한다면 인스턴스 생성, 메서드 호출, 의존성 주입 등을 외부에서 제어한다. 

- 외부에서 제어한다는 것은
    - new 키워드를 사용하지 않아도 스프링에서 객체를 생성한다.
    - Get, Post 요청이 들어왔을 때, 서블릿 컨테이너가 doGet(), doPost() 메서드를 호출한다.

객체의 경우 스프링이 스프링 컨테이너에 빈으로 등록한다. 등록된 빈은 기본적으로 싱글톤으로 동작한다.

스프링 컨테이너에 등록된 빈을 필요로 하는 곳에 주입한다. → 스프링이 의존성 주입을 대신한다.

### 스프링 IoC의 장점

- 스프링 컨테이너에 등록된 빈을 싱글톤으로 관리한다.
    - 빈으로 등록된 인스턴스는 애플리케이션에서 재사용된다.
    - 스프링 컨테이너 내부에 하나의 인스턴스가 생성된다는 것이지, 컨테이너 외부에서 개발자가 직접 new 키워드로 인스턴스를 생성한 경우, 이는 스프링의 관리 대상이 아니다.
- 생성된 빈의 라이프 사이클을 견고하게 다룬다.
- 스프링 AOP? → 라이프사이클?

### 스프링은 POJO를 추구한다.

스프링 프레임워크를 사용한다고 해서, 기존의 클라스가 스프링에서 제공하는 클래스를 상속받지 않아도 된다.

스프링 프레임워크는 프레임워크의 코드가 노출되는 침투적인 프레임워크를 지향하지 않는다. 이는 스프링의 코드를 노출하지 않으면서도 스프링 기능을 사용할 수 있도록 해준다.

### 수동으로 빈 등록

```java
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
```

```java
@Configuration
public class AppConfig {

    @Bean
    public Dictionary dictionary() {
        return new SpringDictionary();
    }

    @Bean
    public SpellChecker spellChecker(Dictionary dictionary) {
        return new SpellChecker(dictionary);
    }
    
}
```

외부에서 생성된 spellChecker 객체의 경우 스프링이 관리하지 않는 객체이다.

```java
public class Application {
    public static void main(String[] args) {
        SpellChecker spellChecker = new SpellChecker();
    }
}
```

스프링 IoC 컨테이너인 ApplicationContext에 등록된 빈을 가져와서 사용할 수 있다.

애플리케이션 전역에서 싱글톤으로 생성된 빈을 사용하며, 빈은 스프링 라이프 사이클에 맞춰서 관리된다.

```java
public class Application {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        SpellChecker spellChecker = applicationContext.getBean(SpellChecker.class);
    }
}
```

POJO 상태를 유지하면서 스프링 프레임워크를 사용할 수 있다. 어느 클래스에서도 스프링 프레임워크에 의존하는 코드가 존재하지 않는다.

### 자동으로 빈 등록

컴포넌트 스캔의 대상은 최소한으로 하는 것이 좋다. 

수동으로 빈을 등록한 경우, @Configuration 클래스에 @Bean을 사용하여 직접 스프링 빈을 등록했다.

자동으로 하고 싶다면 빈으로 등록할 클래스에 @Component를 사용하고, 설정 클래스에서 @ComponentScan을 사용하여 @Component로 등록된 클래스를 조회하여 스프링 컨테이너에 저장한다.

```java
@Configuration
@ComponentScan(basePackageClasses = {AppConfig.class})
public class AppConfig {
    
}
```

```java
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
```

```java
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
```

```java
public class Application {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        SpellChecker spellChecker = applicationContext.getBean(SpellChecker.class);
    }
}

```

## 정리

클래스가 하나 이상의 자원에 의존하는 경우, 사용할 자원을 생성자로 의존성 주입을 할 수 있도록 하자.

의존성 주입 기법은 클래스의 유연성, 재사용성, 테스트 용이성을 개선해준다.

- 외부에서 규격에 맞는 여러 개의 인스턴스를 주입하더라도, 코드 수정이 발생하지 않는다.
- 테스트 코드에서 테스트를 위한 Mock 객체를 생성하여 주입할 수 있다.
