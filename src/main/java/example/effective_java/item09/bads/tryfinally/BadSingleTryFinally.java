package example.effective_java.item09.bads.tryfinally;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
