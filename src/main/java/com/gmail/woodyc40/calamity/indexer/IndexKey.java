package com.gmail.woodyc40.calamity.indexer;

/**
 * An index key that implements the {@link #hashCode()} and
 * {@link #equals(Object)} methods.
 *
 * <p>Indexes referenced by this key may be referenced again
 * by creating a new instance of the index key using the
 * same key identifier.</p>
 *
 * @author caojohnny
 */
public class IndexKey {
    /**
     * The key's name
     */
    private final String name;

    /**
     * Creates a new index key that compares by key
     * identifiers.
     *
     * {@inheritDoc}
     *
     * @param keyName the name of the key
     */
    public IndexKey(String keyName) {
        if (keyName.startsWith("calamity.")) {
            throw new IllegalArgumentException("Reserved key identifier");
        }

        this.name = keyName;
    }

    /**
     * Bypass constructor used to create keys that have
     * reserved key names.
     *
     * @param keyName the key name
     * @param ignored ignored
     */
    IndexKey(String keyName, Object ignored) {
        this.name = keyName;
    }

    /**
     * Obtains the name used to identify this key.
     *
     * @return the key name
     */
    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof IndexKey &&
                ((IndexKey) obj).getName().equals(this.getName());
    }
}