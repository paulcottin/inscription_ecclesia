package org.ecclesiacantic.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CompareUtils {

    /**
     * Trie la liste du plus petit au plus grand
     * @param parToSortList
     * @return
     */
    static public final List<? extends Comparable> sort(final Collection<? extends Comparable> parToSortList) {
        final Comparable[] locSortedTab = new Comparable[parToSortList.size()];
        parToSortList.toArray(locSortedTab);
        Arrays.sort(locSortedTab, null);
        return Arrays.asList(locSortedTab);
    }

    /**
     * Trie la liste du plus grand au plus petit
     * @param parToSortList
     * @return
     */
    static public final List<? extends Comparable> inverseSort(final Collection<? extends Comparable> parToSortList) {
        final List<? extends Comparable> locSortedList = sort(parToSortList);
        final List<Comparable> locInverseSortedList = new ArrayList<>(parToSortList.size());
        for (int locI = parToSortList.size() - 1 ; locI >= 0 ;  locI--) {
            locInverseSortedList.add(locSortedList.get(locI));
        }
        return locInverseSortedList;
    }

    static public final List<?> exclude(final List<?> parRefList, final List<?> parToExcludeList) {
        final List locReturnList = new ArrayList();
        for (final Object locObject : parRefList) {
            if (!parToExcludeList.contains(locObject)) {
                locReturnList.add(locObject);
            }
        }
        return locReturnList;
    }
}
