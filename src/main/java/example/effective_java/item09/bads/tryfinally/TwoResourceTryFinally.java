package example.effective_java.item09.bads.tryfinally;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
        String src = "";
        String dst = "";
        copy(src, dst);
    }
}
