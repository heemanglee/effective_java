단순히 정적 메서드를 제공하는 유틸리티 클래스의 경우 인스턴스화가 인스턴스화가 불필요하다. 왜냐하면 정적 메서드는 `클래스명.메서드()`로 호출이 가능하기 때문이다.

## Bad. abstract

인스턴스화를 방지하기 위해 `추상 클래스`로 선언하면 될까? 정답은 `아니오`이다. 

컴파일러는 묵시적으로 기본 생성자를 추가한다. 이는 추상 클래스에도 적용된다. 자식 클래스의 생성자가 호출될 때, 컴파일러는 부모 클래스의 생성자를 먼저 호출한다.

```java
public abstract class UtilityClass {

    public UtilityClass() {
        System.out.println("UtilityClass Default Constructor Call");
    }

    public static String hello() {
        return "hello";
    }
}

public class ChildUtilityClass extends UtilityClass{

    public ChildUtilityClass() {
        System.out.println("ChildUtilityClass Default Constructor Call");
    }
}
```

```java
public class Main {
    public static void main(String[] args) {
        UtilityClass utilityClass = new ChildUtilityClass();
    }
}
```

부모 클래스가 추상 클래스일지라도, 자식 클래스에서 인스턴스화 하는 과정에서 부모 클래스의 기본 생성자를 호출하게 된다. 따라서 출력과 같이 부모 클래스의 기본 생성자가 먼저 호출되고, 다음으로 자식 클래스의 생성자가 호출된다.

```java
출력:
UtilityClass Default Constructor Call
ChildUtilityClass Default Constructor Call
```

## Best. private 생성자

생성자의 접근 지시자를 private으로 설정하면, 클라이언트 코드에서 생성자를 호출할 수 없다. 자식 클래스의 생성자를 호출할 때 부모 클래스의 기본 생성자를 호출하게 되는데, 자식 클래스마저 부모의 private 생성자를 호출할 수 없기 때문에, 자식 클래스 인스턴스화가 불가능하다.

```java
public class UtilityClass {

    private UtilityClass() {
    }

    public static String hello() {
        return "hello";
    }
}
```

클라이언트 코드에서 private 생성자를 호출하는 경우 예외가 발생한다.

<img width="901" alt="%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-12-03_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_5 41 20" src="https://github.com/user-attachments/assets/48d28730-d8c9-438d-8775-ec69bfd42309">


```java
public class Main {
    public static void main(String[] args) {
        
        new UtilityClass();
    }
}
```

private 접근 지시자 일지라도, 클래스 내부에서는 private 필드 또는 메서드를 호출할 수 있다. 즉, 클래스 내부에서 private 생성자도 호출할 수 있으므로 결국 인스턴스화가 가능하다는 것이다.

이를 대비하기 위해, 인스턴스화할 시에 예외를 발생시킬 수 있도록 할 수 있다.

```java
public class UtilityClass {

    private UtilityClass() {
        // 클래스 내부에서 인스턴스화를 막는다.
        throw new AssertionError();
    }

    public static String hello() {
        return "hello";
    }

    public static void main(String[] args) {
        new UtilityClass();
    }
}
```

UtilityClass 클래스에 private 생성자를 명시적으로 작성하였다. 사용하지 않는 코드를 명시적으로 작성했기 때문에 직관적이지 않을 수 있다.

이런 경우에는 주석을 작성하여 `문서화`를 할 수 있도록 한다.

```java
public class UtilityClass {

    /**
     * 이 클래스는 인스턴스를 만들 수 없다.
     */
    private UtilityClass() {
        System.out.println("UtilityClass Default Constructor Call");
    }

    public static String hello() {
        return "hello";
    }
}
```

참고로 java.util.Arrays의 경우에도 private 생성자를 작성하여, 클라이언트 코드에서 인스턴스화 하지 못하도록 하고 있다. 따라서 Arrays.sort()처럼 정적 메서드를 호출해야 한다.

```java
public final class Arrays {

    // Suppresses default constructor, ensuring non-instantiability.
    private Arrays() {}
    
    public static void sort(int[] a) {
        DualPivotQuicksort.sort(a, 0, 0, a.length);
    }
}
```
