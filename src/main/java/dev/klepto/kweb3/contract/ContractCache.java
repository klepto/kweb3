package dev.klepto.kweb3.contract;

import lombok.Value;
import lombok.val;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class ContractCache {

    private Map<String, CacheItem> items = new HashMap<>();

    public void put(String key, CacheItem item) {
        removeExpired();
        items.put(key, item);
    }

    public Object get(String key) {
        removeExpired();
        return items.get(key).getValue();
    }

    public Object get(String key, Supplier<CacheItem> supplier) {
        removeExpired();
        return items.computeIfAbsent(key, k -> supplier.get());
    }

    public boolean remove(String key) {
        removeExpired();
        return items.remove(key) != null;
    }

    public void removeExpired() {
        val keys = new HashSet<>(items.keySet());
        for (val key : keys) {
            val item = items.get(key);
            if (item.getDuration() <= 0) {
                continue;
            }

            if (System.currentTimeMillis() < item.getTimestamp() + item.getDuration()) {
                continue;
            }

            items.remove(key);
        }
    }

    @Value
    public static class CacheItem {
        Object value;
        long timestamp;
        long duration;
    }

}
