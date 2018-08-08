package com.gmail.woodyc40.calamity.resize;

import com.gmail.woodyc40.calamity.CalamityBuf;
import com.gmail.woodyc40.calamity.bytes.ByteStore;

/**
 * A resizing policy that will double the length of the
 * {@link ByteStore} or until there is sufficient memory to
 * hold the incoming data, whichever one is smaller.
 *
 * @author agenttroll
 */
public class DoublingResizer extends AbstractResizer {
    /**
     * {@inheritDoc}
     */
    public DoublingResizer(int writableLimit) {
        super(writableLimit);
    }

    @Override
    public void resize(CalamityBuf buf, int beginIndex, int length) {
        ByteStore byteStore = buf.byteStore();

        int newLength = byteStore.length();
        int requiredLength = beginIndex + length;
        if (requiredLength > newLength) {
            newLength <<= 1;
        }

        if (requiredLength > newLength) {
            newLength = requiredLength;
        }

        if (newLength < 0 || newLength > this.writableLimit()) {
            throw new OutOfMemoryError(String.format("Buffer length overflow (newLength = %d)", newLength));
        }

        byteStore.setLength(newLength);
    }

    @Override
    public boolean isThreadSafe() {
        return false;
    }
}