package util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Thread-safe LRU (Least Recently Used) Cache implementation
 *
 * Features:
 * - Automatic eviction when cache is full
 * - Thread-safe operations using synchronized methods
 * - Cache hit/miss statistics tracking
 * - Configurable maximum size
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public class LRUCache<K, V> {
    private final int maxSize;
    private final LinkedHashMap<K, V> cache;
    private long hits = 0;
    private long misses = 0;

    /**
     * Create LRU cache with specified maximum size
     *
     * @param maxSize Maximum number of entries in cache
     */
    public LRUCache(int maxSize) {
        this.maxSize = maxSize;
        // LinkedHashMap with access order (true) for LRU behavior
        this.cache = new LinkedHashMap<K, V>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > LRUCache.this.maxSize;
            }
        };
    }

    /**
     * Get value from cache
     *
     * @param key Key to lookup
     * @return Value if found, null otherwise
     */
    public synchronized V get(K key) {
        V value = cache.get(key);
        if (value != null) {
            hits++;
        } else {
            misses++;
        }
        return value;
    }

    /**
     * Put key-value pair into cache
     *
     * @param key Key to store
     * @param value Value to store
     */
    public synchronized void put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key and value cannot be null");
        }
        cache.put(key, value);
    }

    /**
     * Remove specific key from cache
     *
     * @param key Key to remove
     * @return The removed value, or null if key didn't exist
     */
    public synchronized V remove(K key) {
        return cache.remove(key);
    }

    /**
     * Clear all entries from cache
     */
    public synchronized void clear() {
        cache.clear();
        hits = 0;
        misses = 0;
    }

    /**
     * Get current cache size
     *
     * @return Number of entries in cache
     */
    public synchronized int size() {
        return cache.size();
    }

    /**
     * Get maximum cache size
     *
     * @return Maximum number of entries allowed
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Get cache hit rate
     *
     * @return Hit rate as percentage (0.0 to 100.0)
     */
    public synchronized double getHitRate() {
        long total = hits + misses;
        if (total == 0) {
            return 0.0;
        }
        return (double) hits * 100.0 / total;
    }

    /**
     * Get number of cache hits
     *
     * @return Total cache hits
     */
    public synchronized long getHits() {
        return hits;
    }

    /**
     * Get number of cache misses
     *
     * @return Total cache misses
     */
    public synchronized long getMisses() {
        return misses;
    }

    /**
     * Get cache statistics as formatted string
     *
     * @return Statistics string in format: "Cache[size=X/Y, hits=A, misses=B, hit-rate=C%]"
     */
    public synchronized String getStats() {
        return String.format(
            "Cache[size=%d/%d, hits=%d, misses=%d, hit-rate=%.2f%%]",
            size(), maxSize, hits, misses, getHitRate()
        );
    }

    /**
     * Reset statistics (hits and misses) to zero
     * Does not clear the cache contents
     */
    public synchronized void resetStats() {
        hits = 0;
        misses = 0;
    }

    /**
     * Check if cache contains key
     * Note: This does NOT count as a hit or miss
     *
     * @param key Key to check
     * @return true if key exists in cache
     */
    public synchronized boolean containsKey(K key) {
        return cache.containsKey(key);
    }
}
