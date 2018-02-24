package com.gmail.woodyc40.calamity.resize;

/**
 * An abstract, base implementation of a resizer, which
 * handles the component-related functions of the resizer.
 *
 * <p>Resizers are not complicated components, and most
 * should probably not even have a state, and therefore do
 * not need either memory management or the worry of thread-
 * safety.</p>
 *
 * <p>Unless resizers collect statistics or perform other
 * actions that are deemed necessary, any that do contain
 * a state should be carefully reconsidered.</p>
 */
public abstract class AbstractResizer implements Resizer {
    @Override
    public boolean isThreadSafe() {
        return true;
    }

    @Override
    public void free() {
    }
}