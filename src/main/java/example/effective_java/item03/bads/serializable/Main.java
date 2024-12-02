package example.effective_java.item03.bads.serializable;

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
            Elvis elvis = (Elvis) ois.readObject();
            // 역직렬화 시에 새로운 인스턴스를 생성한다.
            System.out.println(elvis == Elvis.INSTANCE); // false
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
