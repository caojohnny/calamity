package com.gmail.woodyc40.calamity.resize;

import com.gmail.woodyc40.calamity.CalamityBuf;
import com.gmail.woodyc40.calamity.bytes.ByteStore;
import com.gmail.woodyc40.calamity.util.Constants;

import java.util.function.Supplier;

/**
 * A resizing policy that will double the length of the
 * {@link ByteStore} or until there is sufficient memory to
 * hold the incoming data, whichever one is smaller.
 *
 * @author agenttroll
 */
public class DoublingResizer implements Resizer {
    public static final Supplier<Resizer> SUPPLIER = Constants.supplyConst(new DoublingResizer());

    @Override
    public void resize(CalamityBuf buf, int beginIndex, int length) {
        ByteStore byteStore = buf.byteStore();

        int newLength = byteStore.length();
        int requiredLength = beginIndex + length;
        if (requiredLength > newLength) {
            newLength <<= 1;
        } else {
            return;
        }

        if (requiredLength > newLength) {
            newLength = requiredLength;
        }

        if (newLength < 0 || newLength > buf.options().maxLength()) {
            throw new OutOfMemoryError(String.format("Buffer length overflow (newLength = %d)", newLength));
        }

        byteStore.setLength(newLength);
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