package net.softwarepage.facharbeit.normalgame.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListHelper {  //Spezielle Methoden für Listen sind in dieser Klasse gesammelt

    public static boolean isListOfSameElements(List<?> list) {
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

    public static boolean isElementTwice(List<?> list) {
        return list.stream().anyMatch((o) -> (Collections.frequency(list, o) > 1));
    }
    
    public static <T> boolean isSpecificElementTwice(List<T> list, T element) {
        return Collections.frequency(list, element) > 1;
    }

    public static <T> List<List<T>> powerSet(Collection<T> list) {  //Gibt die Potenzmenge aus den Elementen in einer Liste zurück
        List<List<T>> powerSet = new ArrayList<>();
        powerSet.add(new ArrayList<>());
        for (T item : list) {
            List<List<T>> newPowerset = new ArrayList<>();
            for (List<T> subset : powerSet) {
                newPowerset.add(subset);

                List<T> newSubset = new ArrayList<>(subset);
                newSubset.add(item);
                newPowerset.add(newSubset);
            }
            powerSet = newPowerset;
        }
        return powerSet;
    }
}
