package com.gmail.woodyc40.calamity;

import com.gmail.woodyc40.calamity.bytes.ByteStore;
import com.gmail.woodyc40.calamity.indexer.IndexKey;
import com.gmail.woodyc40.calamity.indexer.Indexer;
import com.gmail.woodyc40.calamity.marshal.MarshallingResolver;
import com.gmail.woodyc40.calamity.resize.Resizer;

import static com.gmail.woodyc40.calamity.indexer.IdentityIndexKey.READER;
import static com.gmail.woodyc40.calamity.indexer.IdentityIndexKey.WRITER;

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
public interface StrippedCalamityBuf {
    /**
     * Obtains the memory storage scheme used by the buffer.
     *
     * @param <T> the type of {@link ByteStore} to return
     * @return the {@link ByteStore} used by the buffer
     */
    <T extends ByteStore> T byteStore();

    /**
     * Obtains the resizer used by this buffer.
     *
     * @param <T> the type of {@link Resizer} to return
     * @return the buffer resizing component
     */
    <T extends Resizer> T resizer();

    /**
     * Obtains the indexer used to track the index keys for
     * this particular buffer.
     *
     * @param <T> the type of {@link Indexer} to return
     * @return the buffer indexer component
     */
    <T extends Indexer> T indexer();

    /**
     * Obtains the resolver used to transfer input bytes
     * into the buffer and buffered bytes into output.
     *
     * @param <T> the type of resolver used
     * @return the marshal resolver used by this buffer
     */
    <T extends MarshallingResolver> T resolver();

    /**
     * Obtains the instance of the options builder used to
     * construct this buffer.
     *
     * <p>The options instance may or may not be the same
     * instance used to construct the buffer in the first
     * place, although both the options instance used to
     * create the buffer and the options instance returned
     * by this method must have identical settings.</p>
     *
     * @return the options used to construct this buffer
     */
    CalamityOptions options();

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
    void idx(IndexKey key, int idx);

    /**
     * Obtains the number of bytes that are left to read.
     * This should be equal to the {@code writer} index
     * minus the {@code reader} index.
     *
     * @return the number of remaining bytes that can be
     * read from this buffer
     */
    default int readable() {
        return this.idx(WRITER) - this.idx(READER);
    }

    /**
     * Obtains the number of bytes that may be further
     * written into this buffer. This should be equal to
     * {@link CalamityOptions#maxLength()} minus the
     * {@code WRITER} index.
     *
     * @return the number of bytes that can be further
     * written to this buffer
     */
    default int writable() {
        return this.options().maxLength() - this.idx(WRITER);
    }
}
