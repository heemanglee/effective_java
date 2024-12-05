## 동일한 문자열을 생성할 때, new 키워드를 사용하는 것은 안티 패턴이다.

![%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-12-06_%E1%84%8B%E1%85%A9%E1%84%8C%E1%85%A5%E1%86%AB_1 33 12](https://github.com/user-attachments/assets/0df6c3d6-81a6-42b6-a820-50f3c9986626)


- 문자열 리터럴로 문자열을 생성하는 경우
    - 문자열 상수 풀(String Constant Pool)에 해당 문자열이 존재한다면, 해당 문자열을 참조한다.
    - 문자열 상수 풀에 해당 문자열이 없다면, 새로운 문자열을 생성하여 문자열 상수 풀에 저장한 후 반환한다.
- new 키워드로 문자열을 생성하는 경우
    - 새로운 객체를 생성하여 JVM Heap 메모리에 할당한다.

```java
public class Strings {

    public static void main(String[] args) {
        String s1 = "hello"; // String Constant Pool
        String s2 = new String("hello"); // JVM Heap
        String s3 = "hello"; // String Constant Pool

        // JVM은 문자열 값을 문자열 상수 풀에 저장하여, 참조하는 방식으로 재사용한다.
        // 문자열 리터털로 문자열을 생성한 경우 문자열 상수 풀에 저장되지만,
        // new 키워드로 생성한 경우 매 번 새로운 객체를 생성하여 JVM Heap 메모리에 저장한다.

        // s1은 문자열 상수 풀에 저장된 문자열을 참조하고, s2는 힙 메모리에 할당된 주소를 가리킨다.
        System.out.println(s1 == s2); // false

        // s1과 s3 둘 다 문자열 상수 풀에 저장된 문자열을 참조한다.
        System.out.println(s1 == s3); // true

        // String 클래스는 Object 클래스의 equals를 문자열 값을 비교하도록 재정의하였다.
        System.out.println(s1.equals(s2)); // true
        System.out.println(s1.equals(s3)); // true
    }
}

```

동일한 문자열을 생성할 때, 반드시 문자열 리터럴 방식을 사용한다. new 키워드 방식은 새로운 객체를 생성하여 힙 메모리에 저장하는 과정이 발생하므로 객체 생성 비용이 높다. 그러나 문자열 리터럴 방식은 문자열 상수 풀에 캐싱된 문자열을 참조하므로 새로운 객체를 생성하지 않는다. 따라서 매 번 새로운 객체를 생성하는 것보다 캐싱되어 있는 값을 사용하는 것이 당연히 성능상 좋을 수 밖에 없다. 

### 성능 비교

- new 키워드
    - 생성된 객체가 JVM Heap 메모리에 저장된다.

```java
public class Strings {

    public static void main(String[] args) {
        String s = "hello";
        long start = System.nanoTime();
        for (int i = 0; i < 100_000; i++) {
            String tmp = new String("hello");
        }
        long end = System.nanoTime();
        System.out.println(end - start + " ns"); // 1647750 ns
    }
}
```

- 문자열 리터럴
    - 문자열 상수 풀에 캐싱된 객체를 참조한다.

```java
public class Strings {

    public static void main(String[] args) {
        String s = "hello";
        long start = System.nanoTime();
        for (int i = 0; i < 100_000; i++) {
            String tmp = "hello";
        }
        long end = System.nanoTime();
        System.out.println(end - start + " ns"); // 549042 ns
    }
}
```

문자열을 100_000번 생성하였을 때, new 키워드 방식과 문자열 리터럴 방식의 성능을 비교한다.

- new 키워드 방식 : 1647750 ns
- 문자열 리터럴 방식 : 549042 ns

문자열 리터럴 방식이 new 키워드 방식에 비해 3배 좋은 성능을 보인다. 

## Pattern을 캐싱하여, 반복되는 패턴의 정규 표현식(Regex) 검사에서 성능을 향상시킬 수 있다.

String.matches() 메서드는 매개 변수로 문자열을 받아 정규 표현식과 일치하는지 여부를 반환하는 메서드이다.

```java
public boolean matches(String regex){}
```

matches()를 호출하면 내부적으로 정규 표현식을 갖는 Pattern 객체를 생성한다. Pattern 객체가 가지고 있는 정규 표현식과 검사하고자 하는 문자열을 비교하여 일치 여부를 반환한다.

1. String.matches()를 호출하면 Patten.matches()가 호출된다.
    
    ```java
    public boolean matches(String regex) {
        return Pattern.matches(regex, this);
    }
    ```
    
2. Patten.matches()에서는 Pattern.compile()을 호출하여 정규 표현식을 갖는 Pattern 객체를 생성한다.
    
    ```java
    public static boolean matches(String regex, CharSequence input) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        return m.matches();
    }
    
    public static Pattern compile(String regex) {
        return new Pattern(regex, 0);
    }
    ```
    
3. 정규 표현식 정보를 갖고 있는 Pattern 객체와 입력으로 받은 문자열을 비교하여 일치하는지 확인한다.
    
    ```java
    public Matcher matcher(CharSequence input) {
        if (!compiled) {
            synchronized(this) {
                if (!compiled)
                    compile();
            }
        }
        Matcher m = new Matcher(this, input);
        return m;
    }
    ```
    

위와 같이 String.matches()는 Pattern 객체를 생성하여 입력받은 문자열과 정규 표현식을 비교하는 것을 알 수 있다. 그러나 Pattern 객체의 경우 정규 표현식에 대해 유한 상태 기계(finite state machine)을 만드는데, 이는 객체 생성 비용이 높다. 즉, Pattern 객체의 생성 비용이 높다는 것이다.

입력받은 문자열을 검사하기 위해 생성된 Pattern 객체는 검사가 종료되는 대로 사용하지 않게 된다. 참조되지 않는 객체는 GC에 의해 정리 대상이 된다. 단순히 정규 표현식 검사를 위해 생성 비용이 높은 Pattern 객체를 생성하고, 검사가 끝나면 GC의 대상이 되느… 매우 비효율적인 방법 아닐까?

String 클래스에서도 언급한 내용이지만, 동일한 문자열을 사용하는 경우 문자열 상수 풀에 캐싱된 객체를 참조하는 것이 효율적이다. 생성 비용이 높은 Pattern 객체도 마찬가지로 매 번 인스턴스를 생성하여 메모리 공간을 할당하고 GC의 대상이 되는 것보단, 하나의 인스턴스를 생성해놓고 이를 재사용하는 것이 훨씬 성능에 좋을 것이다.

### 성능 비교

- isRomanNumeralSlow() : 매 번 새로운 Pattern 객체를 생성
- isRomanNumeralFast() : 미리 생성한 Pattern 객체를 재사용

```java
public class RomanNumerals {

    static final int COUNT = 100_000;

    static boolean isRomanNumeralSlow(String s) {
        return s.matches("^(?=.)M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[VX]|V?I{0,3})$");
    }

    private static final Pattern ROMAN = Pattern.compile(
            "^(?=.)M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[VX]|V?I{0,3})$"
    );

    static boolean isRomanNumeralFast(String s) {
        return ROMAN.matcher(s).matches();
    }
}
```

main 메서드에서는 isRomanNumeralSlow()과 isRomanNumeralFast()를 호출하여 소요 시간을 비교한다.

```java
public static void main(String[] args) {
    boolean result = false;
    long start = System.nanoTime();
    for (int j = 0; j < COUNT; j++) {
        // 성능 차이를 확인하려면 xxxFast 메서드로 변경
        result = isRomanNumeralSlow("MCMLXXVI");
    }
    long end = System.nanoTime();
    System.out.println(end - start + " ns"); // 124078459 ns
    System.out.println(result);
}
```

- isRomanNumeralSlow() : 124078459 ns
- isRomanNumeralFast() : 31443833 ns

정규 표현식 검사를 위해 매 번 생성 비용이 높은 Pattern 객체를 생성하는 방식보다, 미리 생성된 Pattern 객체를 재사용하는 방식이 소요 시간이 짧은 것을 확인하였다. 이로써 반복하여 사용되는 정규식 패턴을 미리 Pattern 객체로 생성해놓고 재사용해야함을 알 수 있었다.

## 불필요한 오토 박싱과 언 박싱을 주의한다.

- 오토 박싱 : Primitive → Wrapper
- 언 박싱 : Wrapper → Primitive

오토 박싱과 언 박싱은 JVM이 런타임 시점에 자동으로 변환한다. 아래 코드처럼 단순한 덧셈 연산에서도, sumBoxing()을 사용하는 경우 숫자 덧셈뿐만 아니라 오토 박싱을 수행하기 때문에 효율이 좋지 않다. 

반면에 sumNotBoxing()은 Primitive 값 간의 연산이기 때문에 오토 박싱이 발생하지 않는다. 따라서 덧셈만 집중적으로 수행하는 코드가 된다.

```java
public class Sum {

    // 오토 박싱과 언박싱은 JVM이 런타임 시점에 처리한다.
    // AutoBoxing :  Primitive -> Wrappper
    // UnBoxing : Wrapper -> Primitive
    private static long sumBoxing() {
        Long sum = 0L;
        for (long i = 0; i < Integer.MAX_VALUE; i++) {
            sum += i; // 불필요한 오토 박싱 발생
        }
        return sum;
    }

    private static long sumNotBoxing() {
        long sum = 0L;
        for (long i = 0; i < Integer.MAX_VALUE; i++) {
            sum += i; // primitive 연산
        }
        return sum;
    }
}
```

### 성능 비교

```java
public static void main(String[] args) {
    long start2 = System.nanoTime();
    long x2 = sumBoxing(); // 성능 비교를 하려면 sumNotBoxing()으로 변경.
    long end2 = System.nanoTime();
    System.out.println(end2 - start2 + " ns"); // 2277700375 ns
}
```

정수 값 최대 값(Integer_MAX_VALUE)만큼 반복하여 덧셈 연산을 수행하였다.

- 오토 박싱 : 2400638625 ns
- 오토 박싱 X : 2277700375 ns

단순한 Primitive 값 연산을 수행할 때의 성능이 약 123ms 정도 좋은 것을 확인할 수 있다.

## 결론

같은 객체를 반복하여 생성할 경우 불필요하게 메모리를 사용하게 된다. 매 번 새로운 객체를 생성하지 않고 재사용하게 되면 CPU를 덜 사용하게 되므로 당연히 성능에 좋을 수 밖에 없다.

새로운 객체를 생성하지 말라는 말이 아니다. 객체 생성이 필요하다면 객체를 생성해서 사용하되, 반복하여 사용되는 객체는 미리 만들어놓고 재사용하자는 것이다.
