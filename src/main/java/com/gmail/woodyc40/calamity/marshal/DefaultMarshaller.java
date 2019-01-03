package com.gmail.woodyc40.calamity.marshal;

import com.gmail.woodyc40.calamity.CalamityBuf;
import com.gmail.woodyc40.calamity.StrippedCalamityBuf;

import static com.gmail.woodyc40.calamity.indexer.IdentityIndexKey.READER;
import static com.gmail.woodyc40.calamity.indexer.IdentityIndexKey.WRITER;

/**
 * The default marshaller supports marshalling of byte
 * arrays to and from the buffer.
 *
 * @author agenttroll
 */
public class DefaultMarshaller implements Marshaller<byte[]> {
    @Override
    public int write(StrippedCalamityBuf buf, int toIndex, byte[] from, int fromIndex, int length) {
        int bytesToCopy = Math.min(from.length, length);
        buf.resizer().resize(buf, toIndex, bytesToCopy);
        buf.byteStore().write(toIndex, from, fromIndex, bytesToCopy);
        buf.idx(WRITER, toIndex + bytesToCopy);

        return bytesToCopy;
    }

    @Override
    public int read(StrippedCalamityBuf buf, int toIndex, byte[] to, int fromIndex, int length) {
        int bytesToCopy = Math.min(to.length - fromIndex, length);
        buf.byteStore().read(toIndex, to, fromIndex, bytesToCopy);
        buf.idx(READER, fromIndex + bytesToCopy);

        return bytesToCopy;
    }

    @Override
    public void init(CalamityBuf buf) {
    }

    @Override
    public boolean isThreadSafe() {
        return false;
    }

    @Override
    public void free() {
    }
}
