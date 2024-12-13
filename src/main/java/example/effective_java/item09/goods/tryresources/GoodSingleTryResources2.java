package example.effective_java.item09.goods.tryresources;

import java.io.BufferedReader;
import java.io.CharConversionException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamCorruptedException;

public class GoodSingleTryResources2 {

    static String firstLineOfFile(String path) throws IOException {
        // close() -> StreamCorruptedException 예외 발생
        try (BufferedReader br = new BadBufferedReader(new FileReader(path))) {
            return br.readLine(); // readLine() -> CharConversionException 예외 발생
        } catch (CharConversionException | StreamCorruptedException e) {
            return "finally";
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(firstLineOfFile("input.txt"));
    }
}
