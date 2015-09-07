package net.softwarepage.facharbeit.normalgame.helpers;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListHelper {
    public static boolean isListOfSameElements(List<? extends Object> list) {
        Set<Object> set = new HashSet<>(list.size());
        for (Object o : list) {
            if (set.isEmpty()) {
                set.add(o);
            } else {
                if (set.add(o)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean isElementTwice(List<? extends Object> list) {
        return list.stream().anyMatch((o) -> (Collections.frequency(list, o) > 1));
    }
}
