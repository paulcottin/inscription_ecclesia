package org.ecclesiacantic.model.data.beans.participant;

import org.ecclesiacantic.model.data.archi.itf.ISimpleValueEnum;

import java.util.regex.Pattern;

public enum EnumCivilite implements ISimpleValueEnum {

    MONSIEUR("M.|Mr.") {
        @Override
        public boolean isFille() {
            return false;
        }
    },

    MADAME("Mme|Mrs") {
        @Override
        public boolean isFille() {
            return true;
        }
    },

    MADEMOISELLE("Melle|Ms") {
        @Override
        public boolean isFille() {
            return true;
        }
    },

    SOEUR("Soeur") {
        @Override
        public boolean isFille() {
            return true;
        }
    },

    FRERE("Frère") {
        @Override
        public boolean isFille() {
            return false;
        }
    },

    PERE("Père") {
        @Override
        public boolean isFille() {
            return false;
        }
    };

    final Pattern _pattern;

    EnumCivilite(final String parStringValue) {
        _pattern = Pattern.compile(parStringValue);
    }

    static public final EnumCivilite computeFromName(final String parName) {
        if (parName == null) {
            System.err.println("Impossible de trouver une civilité pour la valeur null");
            return null;
        }
        for (final EnumCivilite locCivilite : values()) {
            if (locCivilite.getPattern().matcher(parName).matches()) {
                return locCivilite;
            }
        }
        System.err.println("Impossible de trouver une civilité pour le nom " + parName);
        return null;
    }

    public abstract boolean isFille();


    public final boolean isGarcon() {
        return !isFille();
    }

    public final Pattern getPattern() {
        return _pattern;
    }

    @Override
    public String getValue() {
        return _pattern.pattern();
    }
}
