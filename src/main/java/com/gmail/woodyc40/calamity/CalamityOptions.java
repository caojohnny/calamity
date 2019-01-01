package com.gmail.woodyc40.calamity;

import com.gmail.woodyc40.calamity.bytes.ByteStore;
import com.gmail.woodyc40.calamity.bytes.SafeArrayByteStore;
import com.gmail.woodyc40.calamity.comp.Component;
import com.gmail.woodyc40.calamity.indexer.DefaultIndexer;
import com.gmail.woodyc40.calamity.indexer.Indexer;
import com.gmail.woodyc40.calamity.resize.DoublingResizer;
import com.gmail.woodyc40.calamity.resize.Resizer;
import com.gmail.woodyc40.calamity.util.Constants;

import java.util.function.Supplier;

/**
 * A class specifying options and components that will be
 * passed to a constructed buffer in order to customize
 * functionality.
 *
 * <p>Default values returned can be determined from looking
 * at the JavaDoc comments for each getter method.</p>
 *
 * @author agenttroll
 */
public final class CalamityOptions {
    /**
     * The singleton instance of the default options, used
     * by the default implementation in order to coalesce
     * changes to said defaults in this class.
     */
    private static final CalamityOptions DEFAULT = newBuilder().lock(true);

    /**
     * The initial length of the buffer
     */
    private int initialLength = 16;
    /**
     * The implementation of the byte storage device to use
     */
    private Supplier<ByteStore> byteStoreSupplier = SafeArrayByteStore::new;
    /**
     * The resizing component to use for handling memory
     * reallocation
     */
    private Supplier<Resizer> resizer = DoublingResizer.SUPPLIER;
    /**
     * The indexer component used for handling buffer
     * indexes
     */
    private Supplier<Indexer> indexer = DefaultIndexer::new;
    /**
     * The limit on the byte size of this buffer
     */
    private int maxLength = Constants.ARRAY_MAX_SIZE;
    /**
     * Whether or not to automatically free memory consumed
     * by bytes that have already been read from the buffer
     */
    private boolean autoFree;
    /**
     * Whether or not the buffer that will be built should
     * be thread-safe
     */
    private boolean threadSafe;

    /**
     * Whether or not this set of options should be
     * immutable to modification
     */
    private boolean locked;

    // INSTANTIATION ---------------------------------------

    /**
     * Disables instantiation, use the factory methods
     * instead.
     */
    private CalamityOptions() {
    }

    /**
     * Creates a new Calamity buffers options builder to use
     * for creation of a new set of buffer options.
     *
     * @return the new builder
     */
    public static CalamityOptions newBuilder() {
        return new CalamityOptions();
    }

    /**
     * Obtains the instance of the default options builder,
     * which will create a buffer with all of the default
     * settings.
     *
     * @return the default buffer options
     */
    public static CalamityOptions getDefault() {
        return DEFAULT;
    }

    // SETTERS ---------------------------------------------

    /**
     * Sets the initial length of the buffer.
     *
     * @param initialLength the initial length
     * @return the current instance of the options builder
     */
    public CalamityOptions initialLength(int initialLength) {
        this.checkImmutable();
        this.initialLength = initialLength;
        return this;
    }

    /**
     * Sets the byte storage component for the constructed
     * buffer to that supplied.
     *
     * @param byteStoreSupplier the storage supplier
     * @return the current instance of the options builder
     */
    public CalamityOptions byteStore(Supplier<ByteStore> byteStoreSupplier) {
        this.checkImmutable();
        this.byteStoreSupplier = byteStoreSupplier;
        return this;
    }

    /**
     * Sets the resizer for the buffer.
     *
     * @param resizer the resizer to use
     * @return the current instance of the options builder
     */
    public CalamityOptions resizer(Supplier<Resizer> resizer) {
        this.checkImmutable();
        this.resizer = resizer;
        return this;
    }

    /**
     * Sets the indexer for the buffer.
     *
     * @param indexer the indexer to use
     * @return the current instance of the options builder
     */
    public CalamityOptions indexer(Supplier<Indexer> indexer) {
        this.checkImmutable();
        this.indexer = indexer;
        return this;
    }

    /**
     * Sets the size limit for buffer writers.
     *
     * @param writableLimit the length limit for that may
     * be taken up by the storage device
     * @return the current instance of the options builder
     */
    public CalamityOptions maxLength(int writableLimit) {
        this.checkImmutable();
        this.maxLength = writableLimit;
        return this;
    }

    /**
     * Sets whether or not the buffer should automatically
     * free read bytes.
     *
     * @param autoFree {@code true} to free bytes read from
     * the buffer
     * @return the current instance of the options builder
     */
    public CalamityOptions autoFree(boolean autoFree) {
        this.checkImmutable();
        this.autoFree = autoFree;
        return this;
    }

    /**
     * Ensures that the built buffer will be thread-safe and
     * sharable.
     *
     * @param threadSafe {@code true} to ensure thread-
     * safety
     * @return the current instance of the options builder
     */
    public CalamityOptions threadSafe(boolean threadSafe) {
        this.checkImmutable();
        this.threadSafe = threadSafe;
        return this;
    }

    /**
     * Locks this builder and prevents further changes from
     * being made to its options.
     *
     * <p>Once set to {@code true}, it may not be changed
     * to {@code false}.</p>
     *
     * @param locked {@code true} to set immutable.
     * @return the current instance of the options builder
     */
    public CalamityOptions lock(boolean locked) {
        this.checkImmutable();
        this.locked = locked;
        return this;
    }

    // GETTERS ---------------------------------------------

    /**
     * Obtains the initial length which to allocate the
     * memory space used by the buffer.
     *
     * <p>By default, the initial length is {@code 16}.</p>
     *
     * @return the buffer's initial length
     */
    public int initialLength() {
        return this.initialLength;
    }

    /**
     * Creates a new instance of the byte storage device.
     *
     * <p>By default, the byte store used is an instance
     * of {@link SafeArrayByteStore}.</p>
     *
     * @return the byte storage device
     */
    public ByteStore newByteStore() {
        return this.byteStoreSupplier.get();
    }

    /**
     * Obtains the resizer that will be used by the buffer.
     *
     * <p>By default, the resizer to use is an instance of
     * {@link DoublingResizer}.</p>
     *
     * @return the resizing policy to use
     */
    public Resizer newResizer() {
        return this.resizer.get();
    }

    /**
     * Obtains the indexer that will be used to hold the
     * buffer index mappings.
     *
     * <p>By default, the indexer to use is an instance of
     * {@link DefaultIndexer}.</p>
     *
     * @return the index handler
     */
    public Indexer newIndexer() {
        return this.indexer.get();
    }

    /**
     * Obtains the limit of bytes that may be held in the
     * buffer.
     *
     * <p>By default, this returns
     * {@link Constants#ARRAY_MAX_SIZE}.</p>
     *
     * @return the number of bytes that may be written to
     * the buffer
     */
    public int maxLength() {
        return this.maxLength;
    }

    /**
     * Checks to determine whether the buffer will
     * automatically free read bytes off of the buffer.
     *
     * <p>By default, auto-freeing is not used.</p>
     *
     * @return {@code true} to indicate that bytes will be
     * freed as they are read
     */
    public boolean autoFree() {
        return this.autoFree;
    }

    /**
     * Checks to determine whether the buffer should be
     * thread-safe.
     *
     * <p>By default, the buffer is not thread-safe.</p>
     *
     * @return {@code true} to ensure thread-safety
     */
    public boolean threadSafe() {
        return this.threadSafe;
    }

    // MISC ------------------------------------------------

    /**
     * Obtains a new copy of the options contained in this
     * builder.
     *
     * <p>The returned options will never have the
     * {@code immutable} flag set. This will allow the copy
     * to be modified at will, even if the original set of
     * options was not.</p>
     *
     * @return a new set of options that are set to all of
     * this set of options
     */
    public CalamityOptions copy() {
        return new CalamityOptions()
                .initialLength(this.initialLength)
                .byteStore(this.byteStoreSupplier)
                .resizer(this.resizer)
                .indexer(this.indexer)
                .maxLength(this.maxLength)
                .autoFree(this.autoFree)
                .threadSafe(this.threadSafe);
    }

    /**
     * Creates a new buffer using the options that have been
     * set using this builder.
     *
     * @return the new buffer
     */
    public CalamityBuf newBuf() {
        ByteStore store = this.byteStoreSupplier.get();
        Resizer resizer = this.newResizer();
        Indexer indexer = this.newIndexer();
        if (this.threadSafe) {
            checkThreadSafety(store);
            checkThreadSafety(resizer);
            checkThreadSafety(indexer);
        }

        return CalamityBufImpl.alloc(this.copy().lock(true));
    }

    /**
     * Checks to ensure that this options builder is not
     * immutable, otherwise throws an exception to prevent
     * the call from proceeding.
     */
    private void checkImmutable() {
        if (this.locked) {
            throw new IllegalStateException("Instance of CalamityOptions is immutable");
        }
    }

    /**
     * Checks to ensure that this options builder contains
     * only thread-safe components, otherwise throws an
     * exception to prevent the call from proceeding to
     * constructing the buffer.
     *
     * @param component the component to check for thread-
     * safety
     */
    private static void checkThreadSafety(Component component) {
        if (!component.isThreadSafe()) {
            throw new IllegalArgumentException("threadSafe was specified, aborting non thread-safe buffer because " +
                    component.getClass().getName() + " was used");
        }
    }
}