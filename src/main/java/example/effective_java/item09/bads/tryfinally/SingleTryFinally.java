package example.effective_java.item09.bads.tryfinally;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SingleTryFinally {

    static String firstLineOfFile(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        try {
            return br.readLine();
        } finally { // 반드시 호출
            br.close(); // 사용하지 않는 자원 회수
        }
    }

    public static void main(String[] args) throws IOException {
        String path = "";
        System.out.println(firstLineOfFile(path));
    }
}
