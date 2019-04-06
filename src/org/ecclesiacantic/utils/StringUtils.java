package org.ecclesiacantic.utils;

public class StringUtils {

    static public final boolean isNullOrEmpty(final String parString) {
        return parString == null || parString.trim().isEmpty();
    }
}
