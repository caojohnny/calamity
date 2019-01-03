package com.gmail.woodyc40.calamity.marshal;

import com.gmail.woodyc40.calamity.CalamityBuf;

public class DefaultMarshallingResolver implements MarshallingResolver {
    private static final Marshaller<byte[]> BYTE_ARR_MARSHALLER =
            new DefaultMarshaller();

    @Override
    public void init(CalamityBuf buf) {
    }

    @Override
    public boolean isThreadSafe() {
        return false;
    }

    @Override
    public void free() {
    }

    @Override
    public <T> Marshaller<T> resolveType(Class<T> cls) {
        return null;
    }

    @Override
    public <T, M extends Marshaller<T>> M resolveMarshal(Class<M> cls) {
        return null;
    }

    @Override
    public Marshaller<byte[]> defaultMarshaller() {
        return BYTE_ARR_MARSHALLER;
    }
}
