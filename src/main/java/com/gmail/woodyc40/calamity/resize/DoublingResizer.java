package com.gmail.woodyc40.calamity.resize;

import com.gmail.woodyc40.calamity.CalamityBuf;
import com.gmail.woodyc40.calamity.bytes.ByteStore;

/**
 * A resizing policy that will double the length of the
 * {@link ByteStore} or until there is sufficient memory to
 * hold the incoming data, whichever one is smaller.
 *
 * @author agenttroll
 */
public class DoublingResizer extends AbstractResizer {
    /**
     * The instance of the resizer.
     *
     * <p>As this class does not have a state, the singleton
     * instance should be used whenever possible in order to
     * save on unnecessary instantiation costs.</p>
     */
    public static final DoublingResizer INSTANCE =
            new DoublingResizer();

    /**
     * An empty constructor used to protect the singleton.
     *
     * <p>Subclasses may still extend this resizer, but as
     * no state is involved here, the protected constructor
     * is provided for convenience.</p>
     */
    protected DoublingResizer() {
    }

    @Override
    public void resize(CalamityBuf buf, int beginIndex, int additional) {
        ByteStore byteStore = buf.byteStore();
        int required = byteStore.length() + additional;
        byteStore.resize(required);
    }
}