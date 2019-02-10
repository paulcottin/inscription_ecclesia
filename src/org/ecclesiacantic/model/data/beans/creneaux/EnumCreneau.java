package org.ecclesiacantic.model.data.beans.creneaux;

import org.ecclesiacantic.model.data.archi.EnumDataColumImport;

import java.util.Arrays;
import java.util.List;

public enum EnumCreneau {

    CRENEAU1 {
        @Override
        public EnumCreneau next() {
            return CRENEAU2;
        }

        @Override
        public EnumCreneau previous() {
            return CRENEAU3;
        }

        @Override
        protected List<EnumDataColumImport> getMatchingDataColumn() {
            return Arrays.asList(EnumDataColumImport.MC_CRENEAU_1, EnumDataColumImport.S_CRENEAU_1);
        }
    },

    CRENEAU2 {
        @Override
        public EnumCreneau next() {
            return CRENEAU3;
        }

        @Override
        public EnumCreneau previous() {
            return CRENEAU2;
        }

        @Override
        protected List<EnumDataColumImport> getMatchingDataColumn() {
            return Arrays.asList(EnumDataColumImport.MC_CRENEAU_2, EnumDataColumImport.S_CRENEAU_2);
        }
    },

    CRENEAU3 {
        @Override
        public EnumCreneau next() {
            return CRENEAU1;
        }

        @Override
        public EnumCreneau previous() {
            return CRENEAU1;
        }

        @Override
        protected List<EnumDataColumImport> getMatchingDataColumn() {
            return Arrays.asList(EnumDataColumImport.MC_CRENEAU_3, EnumDataColumImport.S_CRENEAU_3);
        }
    };


    EnumCreneau() {
    }

    public abstract EnumCreneau next();
    public abstract EnumCreneau previous();

    public final EnumCreneau forward(final boolean parIsFroward) {
        if (parIsFroward) {
            return next();
        } else {
            return previous();
        }
    }

    protected abstract List<EnumDataColumImport> getMatchingDataColumn();

    public final boolean match(final EnumDataColumImport parEnumDataColumnImport) {
        for (final EnumDataColumImport locMatchingColumn : getMatchingDataColumn()) {
            if (locMatchingColumn.equals(parEnumDataColumnImport)) {
                return true;
            }
        }
        return false;
    }

    static public final int getNumberOfCreneaux() {
        return values().length;
    }
}
