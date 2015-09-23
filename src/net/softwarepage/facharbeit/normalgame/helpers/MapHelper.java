package net.softwarepage.facharbeit.normalgame.helpers;

import java.util.HashMap;
import java.util.Map;

public class MapHelper {
    public static <K,V> Map<K,V> copyHash(Map<K,V> map) {
        Map<K,V> copy = new HashMap<>();
        for(K k : map.keySet()) {
            copy.put(k, map.get(k));
        }
        return copy;
    }
}
