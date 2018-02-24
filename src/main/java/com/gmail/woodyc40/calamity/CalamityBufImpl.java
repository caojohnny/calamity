package com.gmail.woodyc40.calamity;

import com.gmail.woodyc40.calamity.bytes.ByteStore;
import com.gmail.woodyc40.calamity.resize.Resizer;

/**
 * The primary implementation of the Calamity buffers
 * specification, making use of different components passed
 * in to define functionality of the buffer.
 *
 * @author agenttroll
 */
public class CalamityBufImpl implements CalamityBuf {
    /**
     * The byte storage device
     */
    private final ByteStore byteStore;
    /**
     * The resizing component provided to the buffer
     */
    private final Resizer resizer;

    /**
     * Creates the buffer implementation with the given
     * options and components.
     *
     * @param byteStore the storage device for data passed
     * to the buffer
     * @param initialLength the initial length of the buffer
     * @param resizer the resizing component
     */
    private CalamityBufImpl(ByteStore byteStore, int initialLength, Resizer resizer) {
        this.byteStore = byteStore;
        this.byteStore.init(initialLength);

        this.resizer = resizer;
    }

    /**
     * Allocates the buffer using all default settings.
     *
     * @return a new instance of the default buffer
     */
    public static CalamityBufImpl alloc() {
        return alloc(CalamityOptions.getDefault().initialLength());
    }

    /**
     * Allocates a new buffer using the default settings,
     * except that it may have a different size depending
     * on what is passed in.
     *
     * @param initialLength the initial length to allocate
     * the buffer
     * @return a new instance of the default buffer with
     * length {@code initialLength}
     */
    public static CalamityBufImpl alloc(int initialLength) {
        CalamityOptions options = CalamityOptions.getDefault();
        return alloc(options.byteStore(), initialLength, options.resizer());
    }

    /**
     * Allocates a buffer implementation with all options
     * given, without instantiating an extra options
     * instance.
     *
     * @param byteStore the byte storage component to use
     * @param initialLength the initial length of the buffer
     * @param resizer the resizing component to use
     * @return a new instance of the buffer with the given
     * settings set
     */
    public static CalamityBufImpl alloc(ByteStore byteStore, int initialLength, Resizer resizer) {
        return new CalamityBufImpl(byteStore, initialLength, resizer);
    }

    @Override
    public ByteStore byteStore() {
        return this.byteStore;
    }

    @Override
    public void writeFrom(byte[] bytes) {
    }

    @Override
    public void writeFrom(int toIndex, byte[] from, int fromIndex, int length) {
    }

    @Override
    public byte[] array() {
        return this.byteStore.array();
    }

    @Override
    public void reset() {
    }

    @Override
    public void free() {
        this.byteStore.free();
        this.resizer.free();
    }

    @Override
    public boolean isThreadSafe() {
        return false;
    }
}