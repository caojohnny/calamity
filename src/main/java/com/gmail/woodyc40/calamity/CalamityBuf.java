package com.gmail.woodyc40.calamity;

import com.gmail.woodyc40.calamity.bytes.ByteStore;
import com.gmail.woodyc40.calamity.comp.Component;
import com.gmail.woodyc40.calamity.indexer.IndexKey;

/**
 * The Calamity buffers specification.
 *
 * <p>The standard buffer implemented by the Calamity
 * buffers API conforms to the specification provided by
 * this class. This provides a consistent platform for which
 * implementors can attach their own components to the
 * buffer and customize functionality without losing basic
 * access and buffer control.</p>
 *
 * @author agenttroll
 */
public interface CalamityBuf extends Component {
    /**
     * Obtains the memory storage scheme used by the buffer.
     *
     * @return the {@link ByteStore} used by the buffer
     */
    ByteStore byteStore();

    /**
     * Obtains the index that has been mapped to the given
     * key.
     *
     * @param key the key which to find the mapped index
     * @return the index mapped to the key, or {@code -1} if
     * the key has not been mapped yet
     */
    int idx(IndexKey key);

    /**
     * Maps the given index to the given key.
     *
     * @param key the key which to map the index
     * @param idx the index which to map to the key
     */
    void setIdx(IndexKey key, int idx);

    /**
     * Writes to the buffer from the given byte array.
     *
     * <p>{@implNote This should be semantically equivalent
     * to writing {@code writeFrom(}}</p>
     *
     * @param from the data to write to the buffer
     */
    void writeFrom(byte[] from);

    /**
     * Writes to the buffer from the given byte array.
     *
     * @param toIndex the index of the buffer to begin
     * @param from the data to write to the buffer
     * @param fromIndex the index of the data to begin
     * @param length the length of data to write to the
     * buffer
     */
    void writeFrom(int toIndex, byte[] from, int fromIndex, int length);

    /**
     * Writes to the given array from this buffer.
     *
     * @param to the target array which to write the buffer
     * contents
     * @return the number of bytes written
     */
    int writeTo(byte[] to);

    /**
     * Writes to the given array from this buffer.
     *
     * @param toIndex the index at which to begin writing
     * to the destination
     * @param to the destination array
     * @param fromIndex the index at which to begin reading
     * from the buffer
     * @param length the number of bytes to write from the
     * buffer
     * @return the number of bytes written
     */
    int writeTo(int toIndex, byte[] to, int fromIndex, int length);

    /**
     * Resets the indexes of the buffer.
     *
     * <p>There is no specific functionality that should be
     * guaranteed by resetting the buffer, however, it is
     * recommended that implementors attempt to free data
     * even if {@code autoFree} is not enabled here because
     * the data <em>should</em> no longer be accessible.</p>
     */
    void reset();

    /**
     * Frees the memory and resources that are used by this
     * buffer.
     */
    @Override
    void free();
}