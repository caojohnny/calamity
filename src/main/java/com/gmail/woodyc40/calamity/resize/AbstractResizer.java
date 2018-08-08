package com.gmail.woodyc40.calamity.resize;

/**
 * An abstract, base implementation of a resizer, which
 * handles the component-related functions of the resizer.
 *
 * <p>Unless resizers collect statistics or perform other
 * actions that are deemed necessary, any that do contain
 * a state should be carefully reconsidered.</p>
 */
public abstract class AbstractResizer implements Resizer {
    /**
     * The maximum number of bytes that is provided to the
     * constructed buffer
     */
    private final int writableLimit;

    /**
     * Creates a new resizer that is limited to the size
     * given by the {@code writableLimit}.
     *
     * @param writableLimit the maximum number of bytes that
     *                      the buffer should be capable of
     *                      attaining
     */
    public AbstractResizer(int writableLimit) {
        this.writableLimit = writableLimit;
    }

    @Override
    public int writableLimit() {
        return this.writableLimit;
    }

    @Override
    public void free() {
    }
}