package com.gmail.woodyc40.calamity.bytes;

import com.gmail.woodyc40.calamity.comp.Component;

/**
 * The component specification for byte storage.
 *
 * <p>All buffers contain a {@code ByteStore} in order to
 * hold bytes in memory.</p>
 *
 * @author agenttroll
 * @implNote {@code ByteStore} objects should always be
 * initialized lazily, in order to save memory allocation
 * until the buffer is actually initialized.
 */
public interface ByteStore extends Component {
    /**
     * Obtains the amount of memory, in bytes, that is
     * consumed by stored bytes.
     *
     * @return the length of the memory space allocated for
     * this {@code ByteStore}
     */
    int length();

    /**
     * Resizes the memory space represented by this
     * {@code ByteStore}.
     *
     * <p>No destruction of data should be done by this
     * method. When this method exits, the same data should
     * remain in the {@code DataStore}.</p>
     *
     * <p>Resizing should be handled by the
     * {@link com.gmail.woodyc40.calamity.resize.Resizer}
     * component, which will call this method as appropriate
     * in order to ensure that enough capacity is available
     * to store additional data.</p>
     *
     * @param newLength the new length which should be
     * consumed by the memory space
     */
    void setLength(int newLength);

    /**
     * Obtains the data stored in this {@code ByteStore} as
     * an array of bytes.
     *
     * <p>This method may or may not have to allocate
     * additional memory in order to convert the data into
     * a byte array. This specification makes no requirement
     * to implementors to convert the data in-place.</p>
     *
     * @return a byte array containing the data
     */
    byte[] array();

    /**
     * Determines whether the {@code byte[]} returned by the
     * {@link #array()} method returns a byte array that
     * reflects changes made to it in the buffer.
     *
     * @return {@code true} if the array is raw and changes
     * to the array will be reflected in the buffer,
     * {@code false} if it is a copy or other
     */
    boolean isArrayRaw();

    /**
     * Resets the byte storage area, without necessarily
     * freeing memory (although that may be done as well).
     *
     * <p>This prepares the byte store to accept new bytes
     * without regard for previously written/read bytes. No
     * action may even be taken at all, because the
     * behavior of the previous bytes should be handled by
     * the indexer, however implementations are free to
     * release resources or allocate more to accommodate
     * the state of the buffer being set back to empty.</p>
     */
    void reset();
}