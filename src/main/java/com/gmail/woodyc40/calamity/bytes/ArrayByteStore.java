package com.gmail.woodyc40.calamity.bytes;

import com.gmail.woodyc40.calamity.CalamityBuf;

/**
 * A form of byte storage in which a standard {@code byte}
 * array is used and resized as needed in order for bytes
 * written to the buffer to be stored.
 *
 * <p>The array returned by this store's {@link #array()}
 * method is the raw underlying array.</p>
 *
 * @author agenttroll
 */
public class ArrayByteStore implements ByteStore {
    /**
     * The underlying data stored by this {@code ByteStore}
     */
    private byte[] bytes;

    @Override
    public int length() {
        return this.bytes.length;
    }

    @Override
    public void setLength(int newLength) {
        if (this.bytes == null) {
            this.bytes = new byte[newLength];
            return;
        }

        byte[] newBytes = new byte[newLength];
        System.arraycopy(this.bytes, 0, newBytes, 0, this.bytes.length);
        this.bytes = newBytes;
    }

    @Override
    public byte[] array() {
        return this.bytes;
    }

    public byte[] getRaw() {
        return this.bytes;
    }

    @Override
    public boolean isArrayRaw() {
        return true;
    }

    @Override
    public void reset() {
    }

    @Override
    public void init(CalamityBuf buf) {
        this.setLength(buf.options().initialLength());
    }

    @Override
    public boolean isThreadSafe() {
        return false;
    }

    @Override
    public void free() {
        this.bytes = null;
    }
}