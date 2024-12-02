package example.effective_java.item03.goods.serializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Main {
    public static void main(String[] args) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("elvis.obj"))) {
            oos.writeObject(Elvis.INSTANCE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("elvis.obj"))) {
            // readObject()는 생성자를 거치지 않는다.
            Elvis elvis = (Elvis) ois.readObject();
            // readResolve()을 재정의하여 역직렬화 시점에 새로운 인스턴스가 아닌,
            // 기존의 인스턴스를 반환한다.
            System.out.println(elvis == Elvis.INSTANCE); // true
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
