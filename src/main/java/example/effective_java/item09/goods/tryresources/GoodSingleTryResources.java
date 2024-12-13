package example.effective_java.item09.goods.tryresources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GoodSingleTryResources {

    static String firstLineOfFile(String path) throws IOException {
        // close() -> StreamCorruptedException 예외 발생
        try (BufferedReader br = new BadBufferedReader(new FileReader(path))) {
            return br.readLine(); // readLine() -> CharConversionException 예외 발생
        } catch(IOException e) {
            return "Default";
        } finally {
            System.out.println("hello");
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(firstLineOfFile("input.txt"));
    }
}
