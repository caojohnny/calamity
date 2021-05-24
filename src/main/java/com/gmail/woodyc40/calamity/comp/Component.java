package com.gmail.woodyc40.calamity.comp;

import com.gmail.woodyc40.calamity.CalamityBuf;

/**
 * The component specification.
 *
 * <p>All buffer components must implement this interface
 * in order to build sufficient context for creating and
 * defining extensions to what is already available.</p>
 *
 * @author caojohnny
 */
public interface Component {
    /**
     * Defines logic to initialize the component once the
     * buffer has been instantiated.
     */
    void init(CalamityBuf buf);

    /**
     * Checks to determine whether or not the implemented
     * component is thread-safe, and may be shared along
     * with the buffer.
     *
     * @return {@code true} to indicate that the component
     * is safe to share across threads, {@code false} to
     * indicate that it is not thread-safe
     */
    boolean isThreadSafe();

    /**
     * Defines logic to release resources associated with
     * the component.
     *
     * <p>As components span a wide variety of functions
     * and held data, a {@code free} method must be defined
     * by all components (even if they are not used) in
     * order to ensure that buffers are properly cleaned up
     * at the end of the lifecycle.</p>
     */
    void free();
}