package com.gmail.woodyc40.calamity;

import com.gmail.woodyc40.calamity.comp.Component;
import com.gmail.woodyc40.calamity.indexer.IndexKey;

import static com.gmail.woodyc40.calamity.indexer.IdentityIndexKey.READER;
import static com.gmail.woodyc40.calamity.indexer.IdentityIndexKey.WRITER;

/**
 * The standard specification interface for a buffer. This
 * extends upon the {@link StrippedCalamityBuf}, providing
 * the buffer interaction mechanisms in a coherent manner,
 * e.g. writing and reading from the components.
 *
 * @author caojohnny
 */
public interface CalamityBuf extends Component, StrippedCalamityBuf {
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
        int idx = this.idx(WRITER);
        this.write(idx, b);
        this.idx(WRITER, idx + 1);
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
        int idx = this.idx(READER);
        byte b = this.read(idx);
        this.idx(WRITER, idx + 1);

        return b;
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
     * to writing {@code writeFrom(idx(WRITER), from, 0,
     * from.length)}}
     * </p>
     *
     * @param from the data to write to the buffer
     * @return the number of bytes that were actually
     * written into the buffer
     */
    default int write(byte[] from) {
        return this.write(this.idx(WRITER), from, 0, from.length);
    }

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
    int write(int toIndex, byte[] from, int fromIndex, int length);

    /**
     * Reads from this buffer beginning at the
     * {@code READER} index into the given byte array
     * beginning at {@code 0} until {@code to.length} bytes
     * have been written, or until the buffer runs out of
     * bytes.
     *
     * <p>{@implNote This should be semantically equivalent
     * to writing {@code readTo(0, to, idx(READER),
     * to.length)}}</p>
     *
     * @param to the target array which to write the buffer
     * contents
     * @return the number of bytes written into {@code to}
     */
    default int read(byte[] to) {
        return this.read(0, to, this.idx(READER), to.length);
    }

    /**
     * Reads into the given array from this buffer,
     * beginning at {@code fromIndex} and writing into
     * {@code to[toIndex]}. {@code length} bytes will be
     * written, unless the buffer runs out of bytes to write
     * or there is not enough space left in {@code to}.
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
    int read(int toIndex, byte[] to, int fromIndex, int length);

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