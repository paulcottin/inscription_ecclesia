package org.ecclesiacantic.model.data.beans.participant;

import org.ecclesiacantic.model.data.archi.itf.ISimpleValueEnum;

public enum EnumCivilite implements ISimpleValueEnum {

    MONSIEUR("Monsieur") {
        @Override
        public boolean isFille() {
            return false;
        }
    },

    MADAME("Madame") {
        @Override
        public boolean isFille() {
            return true;
        }
    },

    MADEMOISELLE("Mademoiselle") {
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

    final String _value;

    EnumCivilite(final String parStringValue) {
        _value = parStringValue;
    }

    public abstract boolean isFille();


    public final boolean isGarcon() {
        return !isFille();
    }

    @Override
    public String getValue() {
        return _value;
    }
}
