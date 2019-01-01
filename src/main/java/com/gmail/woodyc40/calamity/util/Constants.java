package com.gmail.woodyc40.calamity.util;

import java.util.function.Supplier;

/**
 * A set of constants used by components and buffers.
 *
 * @author agenttroll
 */
public final class Constants {
    /**
     * The maximum safe array size that can be used to hold
     * data.
     */
    public static final int ARRAY_MAX_SIZE = Integer.MAX_VALUE - 8;

    /**
     * Construction disabled.
     */
    private Constants() {
    }

    /**
     * Obtains a supplier that supplies a constant value.
     *
     * @param t the value to supply
     * @param <T> the type which the supplier should provide
     * @return the supplier which returns the given value
     */
    public static <T> Supplier<T> supplyConst(T t) {
        return () -> t;
    }
}