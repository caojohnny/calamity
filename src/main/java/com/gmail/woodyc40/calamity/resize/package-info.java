/**
 * Components pertaining to the resizing of buffers.
 *
 * <p>One of the components used to build the buffer is the
 * resizing component. This is used to change and ensure
 * that {@link com.gmail.woodyc40.calamity.bytes.ByteStore}
 * objects contain enough capacity for additional bytes to
 * be written to the buffer.</p>
 *
 * <p>Developers may consider changing the resizing
 * functionality of buffers to use a less aggressive
 * decision-making algorithm, or even to disable resizing
 * entirely if the buffer is only going to be used to read
 * a constant number of bytes.</p>
 */
package com.gmail.woodyc40.calamity.resize;