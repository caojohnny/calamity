package com.gmail.woodyc40.calamity.indexer;

import com.gmail.woodyc40.calamity.comp.Component;

/**
 * An indexer is a component that manages the buffer index
 * mappings, i.e. index key to the index that has been set
 * to that key in the buffer.
 *
 * @author agenttroll
 */
public interface Indexer extends Component  {
    /**
     * Obtains the index at which the key is located.
     *
     * @param key the index to grab
     * @return the index, or {@code -1} if the key has not
     * yet been set
     */
    int idx(IndexKey key);

    /**
     * Sets the index to which the key will be located.
     *
     * @param key the key which to set the index
     * @param idx the index to set
     */
    void setIdx(IndexKey key, int idx);

    /**
     * Sets all index keys that have been mapped in this
     * indexer to 0.
     */
    void reset();
}