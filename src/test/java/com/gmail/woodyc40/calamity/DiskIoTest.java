package com.gmail.woodyc40.calamity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DiskIoTest {
    public static void main(String[] args) {
        CalamityBuf buf = CalamityBufImpl.alloc();

        byte[] bytes = new byte[65536];
        try (FileInputStream fis = new FileInputStream(new File("src/test/resources/disk-io-test-file"))) {
            int currentIdx = 0;
            while (true) {
                int read = fis.read(bytes, currentIdx, 5);
                System.out.println("Read " + read + " bytes");
                if (read < 0) {
                    break;
                }
                currentIdx += read;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("File contents: " + new String(bytes).trim());
    }
}