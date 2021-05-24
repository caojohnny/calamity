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
 * @author caojohnny
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
        byte[] newBytes = new byte[newLength];
        if (this.bytes != null) {
            System.arraycopy(this.bytes, 0, newBytes, 0, this.bytes.length);
        }

        this.bytes = newBytes;
    }

    @Override
    public byte[] array() {
        return this.bytes;
    }

    @Override
    public boolean isArrayRaw() {
        return true;
    }

    @Override
    public void write(int idx, byte b) {
        this.bytes[idx] = b;
    }

    @Override
    public byte read(int idx) {
        return this.bytes[idx];
    }

    @Override
    public void write(int toIndex, byte[] from, int fromIndex, int length) {
        System.arraycopy(from, fromIndex, this.bytes, toIndex, length);
    }

    @Override
    public void read(int toIndex, byte[] to, int fromIndex, int length) {
        System.arraycopy(this.bytes, fromIndex, to, toIndex, length);
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