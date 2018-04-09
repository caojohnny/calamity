package com.gmail.woodyc40.calamity.indexer;

/**
 * An index key that does not implement {@link #equals(Object)}
 * or {@link #hashCode()}.
 *
 * <p>Indexes referenced by instances of an
 * {@link IdentityIndexKey} must use the same instance of
 * the index key. There may be duplicate
 * {@link IdentityIndexKey}s in a buffer if the same
 * instance of the key is not used.</p>
 *
 * @author agenttroll
 */
public class IdentityIndexKey extends IndexKey {
    /**
     * A special-case key used by the buffer to track the
     * reader index
     */
    public static final IdentityIndexKey READER = new IdentityIndexKey("calamity.index.reader");
    /**
     * A special-case key used by the buffer to track the
     * writer index
     */
    public static final IdentityIndexKey WRITER = new IdentityIndexKey("calamity.index.writer");

    private final int hashCode = System.identityHashCode(this);

    /**
     * Creates a new index key using the specified String
     * key identifier.
     *
     * <p>Key IDs may not begin with {@code calamity.}.</p>
     *
     * @param keyName the key identifier
     */
    public IdentityIndexKey(String keyName) {
        super(keyName);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }
}