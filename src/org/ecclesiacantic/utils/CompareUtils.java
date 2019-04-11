package org.ecclesiacantic.utils;

import org.ecclesiacantic.config.EnumConfigProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

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

    static public final boolean isMarkTrue(final String parValue) {
        if (StringUtils.isNullOrEmpty(parValue)) {
            return false;
        }
        final String locMark = EnumConfigProperty.BOOLEAN_MARK.stringV();
        return !StringUtils.isNullOrEmpty(locMark) && Pattern.compile(locMark, Pattern.CASE_INSENSITIVE).matcher(parValue).matches();
    }
}
