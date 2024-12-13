package example.effective_java.item09.goods.tryresources;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
