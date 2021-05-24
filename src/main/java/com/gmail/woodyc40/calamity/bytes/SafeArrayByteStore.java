package com.gmail.woodyc40.calamity.bytes;

/**
 * A subclass of {@link ArrayByteStore} in which the array
 * obtained through {@link #array()} is a copy of the bytes
 * stored in the underlying array rather than the array
 * itself.
 *
 * <p>This is simply a safer, more defensive alternative
 * when the bytes need to be manipulated independently from
 * the actual buffer.</p>
 *
 * @author caojohnny
 */
public class SafeArrayByteStore extends ArrayByteStore {
    /**
     * Obtains the raw array instead of returning a copy,
     * for internal use.
     *
     * @return the raw array, in which changes made will be
     * reflected in the array held by this {@link ByteStore}
     */
    public byte[] raw() {
        return super.array();
    }

    @Override
    public byte[] array() {
        byte[] source = super.array();
        byte[] array = new byte[this.length()];
        System.arraycopy(source, 0, array, 0, this.length());

        return array;
    }

    @Override
    public boolean isArrayRaw() {
        return false;
    }
}