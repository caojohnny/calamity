package com.gmail.woodyc40.calamity.indexer;

import com.gmail.woodyc40.calamity.util.Object2IntOpenHashMap;

import static com.gmail.woodyc40.calamity.indexer.IdentityIndexKey.READER;
import static com.gmail.woodyc40.calamity.indexer.IdentityIndexKey.WRITER;

public class DefaultIndexer implements Indexer {
    private int readerIndex;
    private int writerIndex;

    private final Object2IntOpenHashMap<IndexKey> indexes =
            new Object2IntOpenHashMap<>();

    @Override
    public boolean isThreadSafe() {
        return false;
    }

    @Override
    public void free() {
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