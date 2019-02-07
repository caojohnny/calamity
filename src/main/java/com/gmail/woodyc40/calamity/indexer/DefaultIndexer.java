package com.gmail.woodyc40.calamity.indexer;

import com.gmail.woodyc40.calamity.CalamityBuf;
import com.gmail.woodyc40.calamity.util.Object2IntOpenHashMap;

import static com.gmail.woodyc40.calamity.indexer.IdentityIndexKey.READER;
import static com.gmail.woodyc40.calamity.indexer.IdentityIndexKey.WRITER;

/**
 * The default indexing component which provides the
 * capability to add new index keys and use a fast-path
 * to lookup the {@link IdentityIndexKey#READER} and the
 * {@link IdentityIndexKey#WRITER} keys.
 *
 * @author agenttroll
 */
public class DefaultIndexer implements Indexer {
    /**
     * The cached reader index, used to fast path the
     * {@link IdentityIndexKey#READER} key
     */
    private int readerIndex;
    /**
     * The cached reader index, used to fast path the
     * {@link IdentityIndexKey#WRITER} key
     */
    private int writerIndex;

    /**
     * An index key lookup map used to provide capability
     * for key expansion
     */
    private final Object2IntOpenHashMap<IndexKey> indexes =
            new Object2IntOpenHashMap<>();

    @Override
    public void init(CalamityBuf buf) {
    }

    @Override
    public boolean isThreadSafe() {
        return false;
    }

    @Override
    public void free() {
        this.indexes.clear();
    }

    @Override
    public int idx(IndexKey key) {
        if (READER.equals(key)) {
            return this.readerIndex;
        }

        if (WRITER.equals(key)) {
            return this.writerIndex;
        }

        return this.indexes.getInt(key);
    }

    @Override
    public void setIdx(IndexKey key, int idx) {
        if (READER.equals(key)) {
            this.readerIndex = idx;
        }

        if (WRITER.equals(key)) {
            this.writerIndex = idx;
        }

        this.indexes.put(key, idx);
    }

    @Override
    public void reset() {
        this.readerIndex = 0;
        this.writerIndex = 0;

        for (Object2IntOpenHashMap.Entry<IndexKey> entry : this.indexes.object2IntEntrySet()) {
            entry.setValue(0);
        }
    }
}