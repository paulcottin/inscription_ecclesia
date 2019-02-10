package org.ecclesiacantic.utils.parser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NumberUtils {

    static private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY_MM_dd_HH-mm");

    static public final int convertFieldToInt(final String parToConvertString) {
        int locInt = -1;
        try {
            locInt = Integer.parseInt(parToConvertString);
        } catch (final NumberFormatException parE) {
            System.err.println(String.format("Impossible de convertir le caractère suivant en nombre : %s",
                    parToConvertString)
            );
        }
        return locInt;
    }

    static public final double convertFieldToDouble(final String parToConvertString) {
        double locDouble = -1.0;
        try {
            locDouble = Double.parseDouble(parToConvertString);
        } catch (final NumberFormatException parE) {
            System.err.println(String.format("Impossible de convertir le caractère suivant en nombre : %s",
                    parToConvertString)
            );
        }
        return locDouble;
    }

    static public final double convertIntToDouble(final int parIntValue) {
        return new Double(parIntValue).doubleValue();
    }

    static public final double computePourcentage(final int parValue, final int parTotal) {
        return 100 * convertIntToDouble(parValue) / parTotal;
    }

    static public final String horodate() {
        return DATE_FORMAT.format(new Date());
    }
}
