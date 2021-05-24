package com.gmail.woodyc40.calamity.marshal;

import com.gmail.woodyc40.calamity.comp.Component;

/**
 * A resolver helps the buffer select an appropriate tool to
 * convert/marshal input and buffer contents into output.
 *
 * @author caojohnny
 */
public interface MarshallingResolver extends Component {
    /**
     * Resolves for a marshaller that is capable of
     * transferring the given bytes to and from the format
     * of the given class.
     *
     * @param cls the class type of the marshalling type
     * @param <T> the type of object that will be marshalled
     * @return the marshaller, if it exists; {@code null} if
     * it does not
     */
    <T> Marshaller<T> resolveType(Class<T> cls);

    /**
     * Obtains a marshaller that is implemented by the given
     * class.
     *
     * @param cls the class that implements the marshaller
     * @param <T> the type that is handled by the marshaller
     * @param <M> the type of the marshaller
     * @return the marshaller that is implemented by the
     * given class
     */
    <T, M extends Marshaller<T>> M resolveMarshal(Class<M> cls);

    /**
     * Obtains the default marshaller used to implement the
     * default functionality of the buffer as a byte array.
     *
     * @return the byte array marshaller
     */
    Marshaller<byte[]> defaultMarshaller();
}
