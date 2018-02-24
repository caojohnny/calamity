package com.gmail.woodyc40.calamity;

import com.gmail.woodyc40.calamity.bytes.ByteStore;
import com.gmail.woodyc40.calamity.comp.Component;

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
     * Writes to the buffer from the given byte array.
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
     * Obtains the data contained in this buffer as an
     * array.
     *
     * <p>There are no guarantees made about whether or not
     * this array will reflect changes in the buffer, or if
     * the array even is the same used by the buffer.</p>
     *
     * @return the array representation of the data held in
     * this buffer
     */
    byte[] array();

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