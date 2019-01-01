package com.gmail.woodyc40.calamity.marshal;

import com.gmail.woodyc40.calamity.StrippedCalmityBuf;
import com.gmail.woodyc40.calamity.bytes.ByteStore;

public interface Marshaller<B extends ByteStore, T> {
    void write(StrippedCalmityBuf buf, B dest, int idx, byte b);

    T read(StrippedCalmityBuf buf, B src, int idx);

    int write(StrippedCalmityBuf buf, B dest, T from);

    int write(StrippedCalmityBuf buf, B dest, int toIndex, T from, int fromIndex, int length);

    int read(StrippedCalmityBuf buf, B src, T to);

    int readTo(StrippedCalmityBuf buf, B src, int toIndex, T to, int fromIndex, int length);
}
