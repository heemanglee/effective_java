package example.effective_java.item09.goods.tryresources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
