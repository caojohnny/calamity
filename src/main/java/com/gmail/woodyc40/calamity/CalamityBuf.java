package com.gmail.woodyc40.calamity;

import com.gmail.woodyc40.calamity.bytes.ByteStore;
import com.gmail.woodyc40.calamity.comp.Component;
import com.gmail.woodyc40.calamity.indexer.IdentityIndexKey;
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
     * Appends the given byte to the space at which the
     * writer {@link #idx(IndexKey)} points.
     *
     * <p>As a result of this operation, this buffer's
     * writer index will increase by one.</p>
     *
     * @implNote this method should be equivalent to
     * writing
     * {@code
     *      CalamityBuf buffer = ...
     *      byte data = ...
     *
     *      buffer.write(buffer.idx(IdentityIndexKey.WRITER), data);
     * }
     *
     * @param b the byte to write
     */
    default void write(byte b) {
        this.write(this.idx(IdentityIndexKey.WRITER), b);
    }

    /**
     * Sets the byte at the given index to that specified by
     * the {@code b} parameter.
     *
     * @param idx the index to write
     * @param b the byte to write
     */
    void write(int idx, byte b);

    /**
     * Reads a byte from the buffer. The reader index
     * always follows the writer index, and therefore
     * reads the oldest byte in the buffer.
     *
     * <p>As a result of this operation, this buffer's
     * internal reader index will increase to the next
     * byte.</p>
     *
     * @implNote this method should be equivalent to:
     * {@code
     *      CalamityBuf buf = ...
     *      byte read = buf.read(buffer.idx(IdentityIndexKey.READER));
     * }</p>
     *
     * @return the byte read from the "back" of the
     * buffer
     */
    default byte read() {
        return read(this.idx(IdentityIndexKey.READER));
    }

    /**
     * Reads the byte at the given index of the buffer,
     * without making any changes to indexes involved.
     *
     * @param idx the index to read from
     * @return the byte from the given location
     */
    byte read(int idx);

    /**
     * Writes to the buffer from the given byte array.
     *
     * <p>{@implNote This should be semantically equivalent
     * to writing {@code writeFrom(}}</p>
     *
     * @param from the data to write to the buffer
     * @return the number of bytes that were actually
     * written into the buffer
     */
    int writeFrom(byte[] from);

    /**
     * Writes to the buffer from the given byte array.
     *
     * @param toIndex the index of the buffer to begin
     * @param from the data to write to the buffer
     * @param fromIndex the index of the data to begin
     * @param length the length of data to write to the
     * buffer
     * @return the number of bytes that were actually
     * written into the buffer
     */
    int writeFrom(int toIndex, byte[] from, int fromIndex, int length);

    /**
     * Writes to the given array from this buffer.
     *
     * @param to the target array which to write the buffer
     * contents
     * @return the number of bytes written into {@code to}
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
     * @return the number of bytes written into {@code to}
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