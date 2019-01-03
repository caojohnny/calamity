package com.gmail.woodyc40.calamity;

import com.gmail.woodyc40.calamity.bytes.ByteStore;
import com.gmail.woodyc40.calamity.indexer.IndexKey;
import com.gmail.woodyc40.calamity.indexer.Indexer;
import com.gmail.woodyc40.calamity.marshal.MarshallingResolver;
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
     * The options used to build this buffer
     */
    private final CalamityOptions options;
    /**
     * The byte storage device
     */
    private final ByteStore byteStore;
    /**
     * The resizing component provided to the buffer
     */
    private final Resizer resizer;
    /**
     * The indexer used to hold index key to index mappings
     */
    private final Indexer indexer;
    /**
     * The marshaller used to transfer bytes to and from the
     * buffer
     */
    private final MarshallingResolver resolver;

    /**
     * Creates the buffer implementation with the given
     * options and components.
     *
     * @param options the options which to initialize this
     *                buffer
     */
    private CalamityBufImpl(CalamityOptions options) {
        this.options = options;
        this.byteStore = options.newByteStore();
        this.resizer = options.newResizer();
        this.indexer = options.newIndexer();
        this.resolver = options.newResolver();
    }

    /**
     * Allocates the buffer using all default settings.
     *
     * @return a new instance of the default buffer
     */
    public static CalamityBufImpl alloc() {
        return alloc(CalamityOptions.getDefault());
    }

    /**
     * Allocates a buffer implementation with the options
     * passed from the given {@link CalamityOptions} object.
     *
     * @param options the options which to initialize this
     *                buffer
     * @return a new instance of the buffer with the given
     * options set
     */
    static CalamityBufImpl alloc(CalamityOptions options) {
        CalamityBufImpl buf = new CalamityBufImpl(options);
        buf.internalInit();

        return buf;
    }

    @Override
    public <T extends ByteStore> T byteStore() {
        return (T) this.byteStore;
    }

    @Override
    public <T extends Resizer> T resizer() {
        return (T) this.resizer;
    }

    @Override
    public <T extends Indexer> T indexer() {
        return (T) this.indexer;
    }

    @Override
    public <T extends MarshallingResolver> T resolver() {
        return (T) this.resolver;
    }

    @Override
    public CalamityOptions options() {
        return this.options;
    }

    @Override
    public int idx(IndexKey key) {
        return this.indexer.idx(key);
    }

    @Override
    public void idx(IndexKey key, int idx) {
        this.indexer.setIdx(key, idx);
    }

    @Override
    public void write(int idx, byte b) {
        this.byteStore.write(idx, b);
    }

    @Override
    public byte read(int idx) {
        return this.byteStore.read(idx);
    }

    @Override
    public int write(int toIndex, byte[] from, int fromIndex, int length) {
        return this.resolver.defaultMarshaller().write(this, toIndex, from, fromIndex, length);
    }

    @Override
    public int read(int toIndex, byte[] to, int fromIndex, int length) {
        return this.resolver.defaultMarshaller().read(this, toIndex, to, fromIndex, length);
    }

    @Override
    public void reset() {
        this.byteStore.reset();
        this.indexer.reset();
    }

    @Override
    public void free() {
        this.byteStore.free();
        this.resizer.free();
        this.indexer.free();
        this.resolver.free();
    }

    @Override
    public void init(CalamityBuf buf) {
    }

    private void internalInit() {
        this.byteStore.init(this);
        this.resizer.init(this);
        this.indexer.init(this);
        this.resolver.init(this);
    }

    @Override
    public boolean isThreadSafe() {
        return false;
    }
}