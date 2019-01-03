import com.gmail.woodyc40.calamity.CalamityBuf;
import com.gmail.woodyc40.calamity.CalamityOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.gmail.woodyc40.calamity.indexer.IdentityIndexKey.WRITER;

public class DiskIoTest {
    public static void main(String[] args) throws IOException {
        CalamityBuf buf = CalamityOptions.getDefault().newBuf();

        byte[] bytes = new byte[65536];
        try (FileInputStream fis = new FileInputStream(new File("src/test/resources/disk-io-test-file"))) {
            int currentIdx = 0;
            while (true) {
                int read = fis.read(bytes, currentIdx, 5);
                System.out.println("Read " + read + " bytes");
                if (read < 0) {
                    break;
                }

                buf.write(buf.idx(WRITER), bytes, currentIdx, read);
                currentIdx += read;
            }
        }
        System.out.println("File contents: " + new String(bytes).trim());

        byte[] output = new byte[buf.readable()];
        buf.read(output);
        System.out.println("Buffer contents: " + new String(output).trim());
    }
}