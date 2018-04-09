/**
 * Calamity buffers uses a multi-index system to track
 * readers, writers, and markers.
 *
 * <p>This package contains the necessary support classes
 * used to handle indexing. By default, user should always
 * use {@link com.gmail.woodyc40.calamity.indexer.IdentityIndexKey}
 * over {@link com.gmail.woodyc40.calamity.indexer.IndexKey}
 * because the former provides the fastest possible lookup
 * thanks to both the user having to cache the key in order
 * to work, and because identity comparisons have a
 * deterministic lookup speed and would not be affected by
 * the length of the key identifier.</p>
 *
 * <p>In the case that the user needs to use the index key
 * in multiple places but access to the instance of the key
 * elsewhere is not possible, {@link com.gmail.woodyc40.calamity.indexer.IndexKey}
 * would be an ideal alternative.</p>
 */
package com.gmail.woodyc40.calamity.indexer;