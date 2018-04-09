package com.gmail.woodyc40.calamity.bytes;

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
    public void init(int initialLength) {
        this.bytes = new byte[initialLength];
    }

    @Override
    public int length() {
        return this.bytes.length;
    }

    @Override
    public void setLength(int newLength) {
        byte[] newBytes = new byte[newLength];
        System.arraycopy(this.bytes, 0, newBytes, 0, this.bytes.length);
        this.bytes = newBytes;
    }

    @Override
    public byte[] array() {
        return this.bytes;
    }

    @Override
    public void reset() {
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