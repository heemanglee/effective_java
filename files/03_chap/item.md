# [item3] 생성자나 열거 타입으로 싱글턴임을 보증하라

## Bads1. public static final 필드 방식의 싱글톤

```java
// 1. public static final 필드 방식의 싱글톤
public class Elvis {

    // 싱글톤 오브젝트
    public static final Elvis INSTANCE = new Elvis();

    private Elvis() {}

    public void leaveTheBuilding() {
        System.out.println("Whoa Baby, I'm outta here!");
    }

    public void sing() {
        System.out.println("I'll have a blue~ Christmas without you~");
    }
}
```

싱글톤을 사용하는 클라이언트 코드를 테스트하기 어려워진다.

인터페이스를 제공하지 않기 때문에, 테스트를 수행할 때마다 Elvis 객체를 사용해야 한다.

Concert 클래스가 있고, 내부에는 Elvis 객체가 존재한다.

예를 들어, 콘서트 관리인이 완벽한 공연을 추구하면서 여러 번의 리허설을 한다고 가정한다. Conert 클래스 내부에는 실제 Elvis 객체가 있으므로, 리허설을 수행할 때마다 실제 객체인 Elvis 객체를 사용해야 한다.

→ 보통 리허설을 반복할 경우, 엘비스(실제 객체)를 대신하여 리허설을 수행할 사람(대역)이 있어야 할 것이다. 그러나 Concert 클래스 내부에는 Elvis 타입의 객체를 사용하고 있으므로 대역으로 쓸 수 있는 사람(객체)가 없다.

```java
public class Concert {

    private boolean lightsOn;
    private boolean mainStateOpen;

    private Elvis elvis;

    public Concert(Elvis elvis) {
        this.elvis = elvis;
    }

    // 클라이언트 코드
    public void perform() {
        lightsOn = true;
        mainStateOpen = true;
        elvis.sing();
    }

    public boolean isLightsOn() {
        return lightsOn;
    }

    public boolean isMainStateOpen() {
        return mainStateOpen;
    }
}
```

Conert 인스턴스는 생성자 매개변수로 Elvis 타입의 객체를 받고 있으므로, 대역이 아닌 실제 객체를 삽입해야 한다. 

```java
class ConcertTest {

    @Test
    void perform() {
        // 실제 객체를 가지고 테스트를 수행해야 한다.
        Concert concert = new Concert(Elvis.INSTANCE);
        concert.perform(); // 리허설

        assertTrue(concert.isLightsOn());
        assertTrue(concert.isMainStateOpen());
    }

}
```

## Goods1. public static final 필드 방식의 싱글톤

Concert 생성자 매개변수로 실제 객체 타입(Elvis)를 사용했기 때문에 발생한 문제이다.

그렇다면 생성자에서 인터페이스를 받도록 하여 해결할 수 있을 것이다.

```java
public interface IElvis {
    
    void leaveTheBuilding();

    void sing();

}
```

Elvis 클래스는 IElvis 인터페이스를 구현한다.

```java
// 1. public static final 필드 방식의 싱글톤
public class Elvis implements IElvis {

    // 싱글톤 오브젝트
    public static final Elvis INSTANCE = new Elvis();

    private Elvis() {}

    @Override
    public void leaveTheBuilding() {
        System.out.println("Whoa Baby, I'm outta here!");
    }

    @Override
    public void sing() {
        System.out.println("I'll have a blue~ Christmas without you~");
    }
}
```

Concert 클래스의 생성자 매개변수로 실제 객체가 아닌 인터페이스를 입력받도록 한다.

```java
public class Concert {

    private IElvis elvis;

    public Concert(IElvis elvis) {
        this.elvis = elvis;
    }
    
    .. 생략
}
```

인터페이스를 사용한 덕분에, Concert 객체를 생성할 때 모킹된 객체를 삽입할 수 있게 된다. 

매 테스트마다 실제 객체가 아닌 모킹된 객체를 사용함으로써 테스트 비용을 낮출 수 있다.

```java
class ConcertTest {

    @Test
    void perform() {
        // 실제 객체가 아닌 대역(모킹)을 사용할 수 있다.
        // 대역(목 객체)를 사용하여 실제 객체를 사용할 때의 비용을 줄일 수 있다.
        Concert concert = new Concert(new MockElvis());
        concert.perform(); // 대역이 리ㅎ

        assertTrue(concert.isLightsOn());
        assertTrue(concert.isMainStateOpen());
    }

}
```

## Bads2. Reflection API를 사용하면 싱글톤이 깨진다.

private 생성자를 사용한다고 한들, Java Relection API를 사용하면 private 생성자를 호출할 수 있다.

private 생성자를 호출할 수 있기 때문에, 같은 타입의 인스턴스를 여러 개 생성할 수 있다.

```java
// 생성자 방식
public class ElvisReflection {

    public static void main(String[] args) {
        try {
            // Reflection API 사용
            // getConstructor() : public 생성자에만 접근 가능
            /* getDeclaredConstructor() : 접근 지시자 상관없이 선언된 생성자에 접근 가능
            * () : 파라미터가 없는 기본 생성자, (args1, args2, ...) 생성자 호출 가능
            */
            Constructor<Elvis> defaultConstructor = Elvis.class.getDeclaredConstructor();

            // Java Reflection API를 사용하여 private 접근 지시자를 무시하고 해당 생성자에 접근을 허용한다.
            defaultConstructor.setAccessible(true);

            // 생성자를 호출할 때마다 각기 다른 인스턴스가 생성된다.
            Elvis elvis1 = defaultConstructor.newInstance();
            Elvis elvis2 = defaultConstructor.newInstance();

            System.out.println(elvis1 == elvis2); // false
            System.out.println(elvis1 == Elvis.INSTANCE); // false
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

Elvis 클래스는 객체 생성을 하지 못하도록 private 생성자를 만들었으나, Java Reflection API를 통해 private 생성자를 호출할 수 있게 되었다. 따라서 각 인스턴스마다 다른 주소 값을 가지므로 동일성 비교에서 false를 반환한다.

```java
System.out.println(elvis1 == elvis2); // false
System.out.println(elvis1 == Elvis.INSTANCE); // false
```

## Goods2. 객체 생성 여부를 나타내는 필드를 선언하여, 여러 개의 객체 생성을 방지한다.

Reflection API가 private 생성자를 호출할 수 있게되면서 여러 개의 인스턴스를 생성하는 문제가 발생했다. 

이를 방지하기 위해서는 객체 생성 여부를 나타내는 필드를 사용하여 새로운 객체 생성을 방지할 수 있을 것이다.

아래 코드에서는 created 필드를 사용하여, 생성자가 호출될 때 created 필드의 값에 따라 객체 생성을 제어할 수 있도록 하였다.

```java
public class Elvis {

    // 싱글톤 오브젝트
    public static final Elvis INSTANCE = new Elvis();
    private static boolean created;

    private Elvis() {
        if (created) { // 생성자를 한 번만 호출할 수 있도록 한다.
            throw new UnsupportedOperationException("can't be created by constructor.");
        }

        created = true;
    }

    public void leaveTheBuilding() {
        System.out.println("Whoa Baby, I'm outta here!");
    }

    public void sing() {
        System.out.println("I'll have a blue~ Christmas without you~");
    }
}
```

INSTNACE 객체를 만들 때 기본 생성자가 이미 호출된 상태이므로 created = true이다.

elvis1을 생성하기 위해 생성자를 호출하게 되는데, 이 시점에 created는 true이므로 예외가 발생한다.

```java
public class ElvisReflection {

    public static void main(String[] args) {
        try {
            // Reflection API 사용
            // getConstructor() : public 생성자에만 접근 가능
            /* getDeclaredConstructor() : 접근 지시자 상관없이 선언된 생성자에 접근 가능
             * () : 파라미터가 없는 기본 생성자, (args1, args2, ...) 생성자 호출 가능
             */
            Constructor<Elvis> defaultConstructor = Elvis.class.getDeclaredConstructor();

            // Java Reflection API를 사용하여 private 접근 지시자를 무시하고 해당 생성자에 접근을 허용한다.
            defaultConstructor.setAccessible(true);

            // Reflection API를 통해 인스턴스를 여러 개 생성하게되면 -> 싱글톤이 깨지기 때문에
            // 클래스 내부에 인스턴스 생성 여부를 나타내는 필드를 통해 인스턴스 생성을 제어할 수 있음.
            // elvis1 생성 시점에 예외가 발생한다.
            Elvis elvis1 = defaultConstructor.newInstance();
            Elvis elvis2 = defaultConstructor.newInstance();

            System.out.println(elvis1 == elvis2);
            System.out.println(elvis1 == Elvis.INSTANCE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## Bads3. 싱글톤 객체 역직렬화 시에 새로운 인스턴스가 생성된다.

역직렬화 과정에서 생성자를 호출하지 않고, 새로운 인스턴스를 메모리에 로드한다. 

```java
public class Elvis implements Serializable {

    // 싱글톤 오브젝트
    public static final Elvis INSTANCE = new Elvis();
    private static final long serialVersionUID = 1L;

    private Elvis() {}

    public void leaveTheBuilding() {
        System.out.println("Whoa Baby, I'm outta here!");
    }

    public void sing() {
        System.out.println("I'll have a blue~ Christmas without you~");
    }
}
```

역직렬화에 사용된 ObjectInputStream#readObject()의 경우 생성자를 호출하지 않는다.
ObjectOutputStream을 사용하여 객체를 파일로 변환하고, ObjectInputStream을 통해 파일을 객체로 변환한다. 파일 → 객체로 변환 시점에 역직렬화가 발생하는데, 역직렬화는 새로운 인스턴스를 생성하여 메모리에 로드한다.

```java
public class Main {
    public static void main(String[] args) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("elvis.obj"))) {
            oos.writeObject(Elvis.INSTANCE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("elvis.obj"))) {
		        // readObject() 생성자를 호출하지 않는다.
            Elvis elvis = (Elvis) ois.readObject();
            // 역직렬화 시에 새로운 인스턴스를 생성한다.
            System.out.println(elvis == Elvis.INSTANCE); // false
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

따라서 동일성 비교를 하였을 때, false를 반환한다.

```java
Elvis elvis = (Elvis) ois.readObject();
// 역직렬화 시에 새로운 인스턴스를 생성한다.
System.out.println(elvis == Elvis.INSTANCE); // false
```

## Goods3. 역직렬화 시에 readResolve()를 오버라이딩한다.

![%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-12-02_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_1 35 32](https://github.com/user-attachments/assets/969bc89d-c5d9-47bb-8f66-c2543a6e6f62)


```java
public class Elvis implements Serializable {

    // 싱글톤 오브젝트
    public static final Elvis INSTANCE = new Elvis();
    private static final long serialVersionUID = 1L;

    private Elvis() {}

    public void leaveTheBuilding() {
        System.out.println("Whoa Baby, I'm outta here!");
    }

    public void sing() {
        System.out.println("I'll have a blue~ Christmas without you~");
    }

    // 역직렬화할 때, 항상 동일한 인스턴스를 반환하도록 할 수 있다.
    private Object readResolve() {
        return INSTANCE; // 기존의 싱글톤 인스턴스를 반환한다.
    }
}
```

ObjectInputStream#readObject()가 호출된 후 readResolve()가 자동으로 호출된다. 

readSolve()를 오버라이딩하여 이미 생성된 싱글톤 객체를 반환하도록 바꿔치기할 수 있다. 그렇다면 readObject()에 의해 새로 생성된 인스턴스는 GC의 대상이 된다.

```java
public class Main {
    public static void main(String[] args) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("elvis.obj"))) {
            oos.writeObject(Elvis.INSTANCE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("elvis.obj"))) {
		        // readObject() 생성자를 호출하지 않는다.
            Elvis elvis = (Elvis) ois.readObject();
            // readResolve()을 재정의하여 역직렬화 시점에 새로운 인스턴스가 아닌,
            // 기존의 인스턴스를 반환한다.
            System.out.println(elvis == Elvis.INSTANCE); // true
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
```

따라서 역직렬화된 elvis 객체와 싱글톤 객체 간에 동일성 비교 시에 true를 반환한다.

```java
  Elvis elvis = (Elvis) ois.readObject();
  // readResolve()을 재정의하여 역직렬화 시점에 새로운 인스턴스가 아닌,
  // 기존의 인스턴스를 반환한다.
  System.out.println(elvis == Elvis.INSTANCE); // true
```

## Goods4. 메서드로 싱글톤 접근

메서드를 통해 싱글톤 객체를 반환한다고 한들, 동일하게 직렬화와 리플렉션 시에 동일한 인스턴스를 반환하지 않는 문제가 있다.

```java
public class Elvis {

    // 싱글톤 오브젝트
    private static final Elvis INSTANCE = new Elvis();
    public static Elvis getInstance() {
        return INSTANCE;
    }

    private Elvis() {}

    public void leaveTheBuilding() {
        System.out.println("Whoa Baby, I'm outta here!");
    }

    public void sing() {
        System.out.println("I'll have a blue~ Christmas without you~");
    }
}
```

메서드(getInstance())를 통해 싱글톤 객체를 제공하면, 메서드 내용이 수정되더라도 클라이언트 코드는 수정할 필요가 없다는 것이다.

```java
public class Elvis {

    public static Elvis getInstance() {
        return new Elvis(); // 싱글톤 -> 새로운 인스턴스로 변경
    }
}
```

getInstance() 내용이 바꼈음에도, 클라이언트 코드에는 영향을 미치지 않는다.

```java
public static void main(String[] args) {
    // 메서드(getInstance)로 싱글톤 객체를 받게 되면,
    // 메서드 동작을 바꾸더라도 클라이언트 코드는 수정하지 않아도 된다.
    Elvis elvis = Elvis.getInstance();
    elvis.leaveTheBuilding();
  }
}
```

## Best.Enum 싱글톤

Enum을 사용하여 싱글톤을 표현할 경우, 직렬화나 리플렉션으로부터 새로운 인스턴스가 생성되는 것을 방지한다.

```java
public class EnumReflection {
    public static void main(String[] args) {
        try {
            Constructor<Elvis> declaredConstructor = Elvis.class.getDeclaredConstructor();
            System.out.println(declaredConstructor);
        } catch(NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
```

리플렉션을 통해 새로운 인스턴스를 생성하려고 하면 예외가 발생하면서 차단된다.

```java
java.lang.NoSuchMethodException: example.effective_java.item03.goods.enums.Elvis.<init>()
	at java.base/java.lang.Class.getConstructor0(Class.java:3761)
	at java.base/java.lang.Class.getDeclaredConstructor(Class.java:2930)
	at example.effective_java.item03.goods.enums.EnumReflection.main(EnumReflection.java:8)
```

Enum 클래스는 역직렬화할 때 새로운 인스턴스를 생성하지 않고, 직렬화에 사용된 객체를 반환한다.

따라서 싱글톤 객체를 직렬화하여 serializedElvis를 얻고, serializedElvis를 역직렬화하여 elvis 객체를 얻었을 때, 동일성 비교를 하면 true를 반환한다.

```java
public class Main {

    public static void main(String[] args) {
        byte[] serializedElvis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(Elvis.INSTANCE);
            serializedElvis = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(Base64.getEncoder().encodeToString(serializedElvis));
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedElvis);
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            Elvis elvis = (Elvis) ois.readObject();
            System.out.println(elvis == Elvis.INSTANCE); // true
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
