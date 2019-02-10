package org.ecclesiacantic.model.data.beans.participant;

import org.ecclesiacantic.utils.parser.NumberUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hebergement {

    static public final int getCapaciteFromStringData(final String parStringData) {
        final Pattern locCapacitePattern = Pattern.compile(".*([0-9]+).*");
        final Matcher locMatcher = locCapacitePattern.matcher(parStringData);
        int locCapacite = -1;
        String locCapaciteString = null;
        if (locMatcher.matches()) {
            locCapaciteString = locMatcher.group(1);
            locCapacite = NumberUtils.convertFieldToInt(locCapaciteString);
        }
        return locCapacite;
    }


    final private String _metro;
    final private EnumSexeHebergement _sexeHergement;
    final private int _capacite;

    public Hebergement(final String parMetro, final EnumSexeHebergement parSexeHergement, final int parCapacite) {
        this._metro = parMetro;
        this._sexeHergement = parSexeHergement;
        this._capacite = parCapacite;
    }

    public Hebergement(final String parMetro, final EnumSexeHebergement parSexeHergement, final String parStringData) {
        this(parMetro, parSexeHergement, getCapaciteFromStringData(parStringData));
    }
    @Override
    public String toString() {
        return String.format("Métro : %s - Capacite : %d (%s)", _metro, _capacite, _sexeHergement.toString());
    }

}
