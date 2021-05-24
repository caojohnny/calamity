package com.gmail.woodyc40.calamity.marshal;

import com.gmail.woodyc40.calamity.StrippedCalamityBuf;
import com.gmail.woodyc40.calamity.comp.Component;

/**
 * A marshaller is a type converter that allows implementers
 * to attach functionality to the buffer and marshal bytes
 * from a given object into the buffer, and vice versa.
 *
 * @param <T> the type that is marshalled
 * @author caojohnny
 */
public interface Marshaller<T> extends Component {
    /**
     * Converts the given object into bytes and writes it to
     * the byte storage defined by the given buffer.
     *
     * @param buf the buffer which to write bytes into
     * @param toIndex the index at which to begin writing
     * @param from the object which to marshal into bytes
     * @param fromIndex the index at which to begin
     *                  marshalling, if needed
     * @param length the number of bytes to marshal, if
     *               needed
     * @return the number of bytes marshalled
     */
    int write(StrippedCalamityBuf buf, int toIndex, T from, int fromIndex, int length);

    /**
     * Coerces the bytes stored in the given buffer's
     * {@link com.gmail.woodyc40.calamity.bytes.ByteStore}
     * into the form represented by {@code T}.
     *
     * @param buf the buffer which to marshal bytes from
     * @param toIndex the index at which to begin writing
     *                bytes marshalled from this buffer
     *                into the given object, if needed
     * @param to the object which to marshal bytes into
     * @param fromIndex the index at which to begin marshal
     *                  bytes from the buffer
     * @param length the number of bytes that need to be
     *               transferred
     * @return the number of bytes that are transferred
     */
    int read(StrippedCalamityBuf buf, int toIndex, T to, int fromIndex, int length);
}
