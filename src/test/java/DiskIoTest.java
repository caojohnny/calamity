import com.gmail.woodyc40.calamity.CalamityBuf;
import com.gmail.woodyc40.calamity.CalamityOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.gmail.woodyc40.calamity.indexer.IdentityIndexKey.WRITER;

public class DiskIoTest {
    public static void main(String[] args) throws IOException {
        try (FileInputStream fis = new FileInputStream(new File("src/test/resources/disk-io-test-file"))) {
            System.out.println("Reading with BAOS:");
            readWtihBaos(fis);
        }

        System.out.println();

        try (FileInputStream fis = new FileInputStream(new File("src/test/resources/disk-io-test-file"))) {
            System.out.println("Reading with calamity buf:");
            readWithCalamityBuf(fis);
        }
    }

    private static void readWithCalamityBuf(FileInputStream fis) throws IOException {
        CalamityBuf buf = CalamityOptions.getDefault().newBuf();

        while (true) {
            int idx = buf.idx(WRITER);
            buf.resizer().resize(buf, idx, 5);
            int read = fis.read(buf.byteStore().array(), idx, 5);
            System.out.println("Read " + read + " bytes");
            if (read < 0) {
                break;
            }

            buf.idx(WRITER, idx + read);
        }

        byte[] output = new byte[buf.readable()];
        buf.read(output);

        System.out.println("Buffer contents: " + new String(output).trim());
    }

    private static void readWtihBaos(FileInputStream fis) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] bytes = new byte[65536];
        int currentIdx = 0;
        while (true) {
            int read = fis.read(bytes, currentIdx, 5);
            System.out.println("Read " + read + " bytes");
            if (read < 0) {
                break;
            }

            baos.write(bytes, currentIdx, read);
            currentIdx += read;
        }
        System.out.println("File contents: " + new String(bytes).trim());

        byte[] output = baos.toByteArray();
        System.out.println("Buffer contents: " + new String(output).trim());
    }
}