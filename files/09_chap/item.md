> https://bristle-comb-69b.notion.site/item9-try-finally-try-with-resources-15a43369026b805c864bea774a6f7617?pvs=4

## Bad1. finally 블럭에서도 예외가 발생할 수 있다.

예외는 `try` 블록과 `catch` 블록 둘 다 발생할 수 있다. 코드에서 InputStream과 OutputStream 사용을 마치고 자원을 해제하기 위해 finally 블록에서 close()를 호출한다. 순차적으로 자원이 종료되기 때문에 OutputStream 자원이 먼저 해제된 후, InputStream 자원이 해제된다. 이때, OutputStream 자원을 해제하는 과정에서 예외가 발생하면 InputStream 자원은 해제되지 않고 종료되게 된다. 자원이 해제되지 못한채 종료되기 때문에 메모리 누수가 발생한다.

```java
public class TwoResourceTryFinally {

    private static final int BUFFER_SIZE = 8 * 1024;

    static void copy(String src, String dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        try {
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while ((n = in.read(buf)) >= 0) {
                out.write(buf, 0, n);
            }
        } finally { // finally 블럭에서도 예외가 발생한다.
            out.close();
            in.close();
        }
    }

    public static void main(String[] args) throws IOException {
        String src = "input.txt";
        String dst = "output.txt";
        copy(src, dst);
    }
}
```

## Bad2. 여러 개의 finally 블록을 사용하는 것은 가독성이 떨어진다.

Bad1의 경우 하나의 finally 블록에서 두 개의 자원 해제를 명시하는 코드를 작성했었다. 이는 첫 번째 자원을 해제하는 과정에서 예외가 발생하면 두 번째 자원이 해제되지 못한채 종료된다는 문제가 있었다.

Bad2의 경우 Bad1을 개선하기 위해 자원의 개수에 맞게 finally 블록을 사용한 예제이다. 첫 번째 finally 블록에서 OutputStream을 해제하고, 두 번째 finally 블록에서 InputStream을 해제했다. 첫 번째 finally 블록에서 발생하더라도, 두 번째 finally 블록에는 영향을 미치지 않기 때문에 개선되었다고 할 수 있다.

그러나 try-finally 블록이 중첩되면서 가독성이 매우 떨어지게 되었다. 이 또한 좋은 방법이라고는 할 수 없다.

```java
public class DoubleTryFinally {

    private static final int BUFFER_SIZE = 8 * 1024;

    static void copy(String src, String dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                byte[] buf = new byte[BUFFER_SIZE];
                int n;
                while ((n = in.read(buf)) >= 0) {
                    out.write(buf, 0, n);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    public static void main(String[] args) throws IOException {
        String src = "input.txt";
        String dst = "output.txt";
        copy(src, dst);
    }
}

```

## 🌟Bad3. 먼저 발생한 예외 정보가 생략된다.

- readLine()에서 CharConversionException 예외가 발생한다.
- close()에서 StreamCorruptedException 예외가 발생한다.

```java
public class BadBufferedReader extends BufferedReader {

    public BadBufferedReader(Reader in, int sz) {
        super(in, sz);
    }

    public BadBufferedReader(Reader in) {
        super(in);
    }

    @Override
    public String readLine() throws IOException {
        throw new CharConversionException();
    }

    @Override
    public void close() throws IOException {
        throw new StreamCorruptedException();
    }
}
```

try 블록에서 readLine()을 호출하고 finally에서 close()를 호출한다. try-finally의 경우 try 블록에서 예외가 발생하더라도 finally 블록이 반드시 실행된다. 

```java
public class BadSingleTryFinally {

    static String firstLineOfFile(String path) throws IOException {
        BufferedReader br = new BadBufferedReader(new FileReader(path));
        try {
            return br.readLine(); // CharConversionException
        } finally {
            br.close(); // StreamCorruptedException
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(firstLineOfFile("input.txt"));
    }
}
```

readLine()에서 CharConversionException 예외가 먼저 발생하고, close()에서 StreamCorruptedException 예외가 다음으로 발생하기 때문에 두 예외 정보가 스택 트레이스에 남을 것이라 예상할 수 있다.

그러나 예상과는 달리 두 번째로 발생한 StreamCorruptedException 예외 메시지만 출력된다. 즉, CharConversionException 예외가 발생했음에도 스택 트레이스에 예외 정보가 남지 않는 것이다. 이것이 try-finally 블록의 가장 큰 단점이라고 할 수 있다. 스택 트레이스를 확인할 때 가장 먼저 발생한 예외 정보를 확인하게 되는데, 이게 남지 않는 것이므로 디버깅을 매우 어렵게 만든다.

<img width="1147" alt="%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-12-12_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_5 04 52" src="https://github.com/user-attachments/assets/5df0008a-b9ab-4a74-97ba-495ce7aa6573" />


## AutoCloseable

try-with-resource를 사용하면 try-finally를 사용했을 때 발생하는 문제들을 해결할 수 있다. try-with-resources는 try(…)에 명시한 자원들을 try 블록이 종료될 때 자동으로 반납할 수 있도록 한다.

try-with-resources를 사용하기 위해서는 해당 객체가 `AutoCloseable` 인터페이스를 구현해야 한다. 사용한 자원을 finally에 굳이 명시하지 않아도 try 블록이 종료될 때 close()를 묵시적으로 호출하여 자원을 반납한다.

```java
public interface AutoCloseable {

    void close() throws Exception;
}

```

### AutoCloseable 구현 예제

CustomResource 클래스는 AutoCloseable 인터페이스를 구현한다.

```java
public class CustomResource implements AutoCloseable {

    public void doSomething() {
        System.out.println("작업 ing...");
    }

    @Override
    public void close() {
        System.out.println("CustomResource 자원 반납");
    }
}
```

try(...) 블록에서 CustomResource 자원을 사용하여 doSomething() 작업을 수행하고 있다. 

출력 메시지를 확인하면 알 수 있듯이, close() 메서드를 명시적으로 호출하지 않았음에도 try 블록이 종료되면서 자동으로 호출하는 것을 알 수 있다.

```java
public class UseResourceMain {

    public static void main(String[] args) {
        try (CustomResource resource = new CustomResource()) {
            resource.doSomething();
        }
    }
}

[출력]
작업 ing...
CustomResource 자원 반납
```

## Good1. try-with-resources를 사용하면 close()를 묵시적으로 호출한다.

BufferedReader를 try 블록에서 사용을 마치면 자동으로 BufferedReader에 구현된 close()가 호출되면서 자원을 반납하게 된다.

```java
public class SingleResource {

    static String firstLineOfFile(String path) throws IOException {
        // try-with-resources는 자동으로 자원을 해제한다. -> close() 호출
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.readLine();
        }
    }

    public static void main(String[] args) throws IOException {
        String path = "input.txt";
        System.out.println(firstLineOfFile(path));
    }
}
```

### Good2. try(…) 블록에 여러 개의 자원을 사용할 수 있다.

자원을 여러 개 사용하더라도 두 자원 모두 close()가 호출되면서 반납하게 된다.

```java
public class DoubleResources {

    private static final int BUFFER_SIZE = 8 * 1024;

    static void copy(String src, String dst) throws IOException {

        // try-with-resources에 사용된 모든 자원을 해제한다. -> close() 호출
        try (
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dst);
        ) {
            byte[] buf = new byte[BUFFER_SIZE];
            int n;
            while ((n = in.read(buf)) >= 0) {
                out.write(buf, 0, n);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String src = "input.txt";
        String dst = "output.txt";
        copy(src, dst);
    }
}

```

## 🌟Good3. try-finally와 달리 예외 정보가 스택 트레이스에 포함된다.

- readLine()에서 CharConversionException 예외가 발생한다.
- close()에서 StreamCorruptedException 예외가 발생한다.

```java
public class BadBufferedReader extends BufferedReader {

    public BadBufferedReader(Reader in, int sz) {
        super(in, sz);
    }

    public BadBufferedReader(Reader in) {
        super(in);
    }

    @Override
    public String readLine() throws IOException {
        throw new CharConversionException();
    }

    @Override
    public void close() throws IOException {
        throw new StreamCorruptedException();
    }
}
```

firstLineOfFile()에서 readLine()과 close()가 호출되면서 CharConversionException, StreamCorruptedException 예외가 모두 발생한다. try-finally를 사용하는 경우에는 두 번째로 발생한 예외 정보만 스택 트레이스에 출력되고, 첫 번째로 발생한 예외는 출력되지 않는 문제가 있었다.

```java
public class GoodSingleTryResources {

    static String firstLineOfFile(String path) throws IOException {
        // close() -> StreamCorruptedException 예외 발생
        try (BufferedReader br = new BadBufferedReader(new FileReader(path))) {
            return br.readLine(); // readLine() -> CharConversionException 예외 발생
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(firstLineOfFile("input.txt"));
    }
}
```

반면에 try-with-resoruces를 사용하는 경우, 먼저 발생한 예외 정보가 가장 먼저 출력되고, 이후에 발생한 예외 정보는 `Suppressed`에 작성된다. 

- Suppressed : “스택 트레이스에 숨겨졌다” 의미

<img width="1056" alt="%E1%84%89%E1%85%B3%E1%84%8F%E1%85%B3%E1%84%85%E1%85%B5%E1%86%AB%E1%84%89%E1%85%A3%E1%86%BA_2024-12-12_%E1%84%8B%E1%85%A9%E1%84%92%E1%85%AE_5 25 57" src="https://github.com/user-attachments/assets/c6a2029c-5e42-4891-8fba-1c1d92f2ed6d" />


또한, try-with-resoruces에서도 catch, finally 블록을 사용할 수 있다.

```java
static String firstLineOfFile(String path) throws IOException {
    // close() -> StreamCorruptedException 예외 발생
    try (BufferedReader br = new BadBufferedReader(new FileReader(path))) {
        return br.readLine(); // readLine() -> CharConversionException 예외 발생
    } catch(CharConversionException | StreamCorruptedException e) {
        return "finally";
    }
}
```
