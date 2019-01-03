package com.gmail.woodyc40.calamity.resize;

import com.gmail.woodyc40.calamity.StrippedCalamityBuf;
import com.gmail.woodyc40.calamity.comp.Component;

/**
 * The resizing component specification.
 *
 * <p>Resizing logic should be implemented by developers who
 * find that the default is unsatisfactory or have very
 * specific needs of the buffer, such as disabling resizing
 * altogether.</p>
 *
 * <p>Resizing is completely handled by this method, and
 * implementors should call
 * {@link com.gmail.woodyc40.calamity.bytes.ByteStore#setLength(int)}
 * whenever necessary to achieve the desired effects of the
 * resizing operation.</p>
 *
 * <p>Resizing need not be done on every invocation of this
 * method, in fact, it is highly discouraged as it may not
 * be necessary to even resize at all. Implementors should
 * check the length and desired length of allocated memory
 * at all times.</p>
 *
 * @author agenttroll
 */
public interface Resizer extends Component {
    /**
     * Performs the resizing logic checks and passes off the
     * necessary data to the
     * {@link com.gmail.woodyc40.calamity.bytes.ByteStore}
     * for proper memory reallocation.
     *
     * @param buf the buffer that will experience a resize
     * @param beginIndex the index at which the data will
     * potentially need to be added
     * @param length the number of bytes to be added
     * starting at the {@code beginIndex}
     */
    void resize(StrippedCalamityBuf buf, int beginIndex, int length);
}