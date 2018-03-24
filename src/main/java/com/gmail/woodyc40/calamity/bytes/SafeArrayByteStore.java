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
 * @author agenttroll
 */
public class SafeArrayByteStore extends ArrayByteStore {
    @Override
    public byte[] array() {
        byte[] source = super.array();
        byte[] array = new byte[this.length()];
        System.arraycopy(source, 0, array, 0, this.length());

        return array;
    }
}