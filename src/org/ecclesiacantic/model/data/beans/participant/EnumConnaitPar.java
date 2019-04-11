package org.ecclesiacantic.model.data.beans.participant;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.utils.CompareUtils;

import java.util.HashMap;
import java.util.Map;

public enum EnumConnaitPar {

    CHORALE(EnumDataColumImport.P_CHORALE),

    MEDIA(EnumDataColumImport.MEDIA),

    RESEAUX_SOCIAUX(EnumDataColumImport.RESEAUX_SOCIAUX),

    BOUCHE_A_OREILLE(EnumDataColumImport.BOUCHE_OREILLE),

    BY_2016_PARTICIPANT(EnumDataColumImport.RESEAUX_SOCIAUX);

    private final EnumDataColumImport _dataColumn;

    EnumConnaitPar(final EnumDataColumImport parDataColumn) {
        _dataColumn = parDataColumn;
    }

    static public final Map<EnumConnaitPar, Boolean> initFromData(final Map<EnumDataColumImport, String> parData) {
        final Map<EnumConnaitPar, Boolean> locReturnMap = new HashMap<>();
        for (final EnumConnaitPar locConnaitPar : EnumConnaitPar.values()) {
            if (locConnaitPar.getDataColumn().isActive()) {
                locReturnMap.put(locConnaitPar, CompareUtils.isMarkTrue(parData.get(locConnaitPar.getDataColumn())));
            }
        }
        return locReturnMap;
    }

    public final EnumDataColumImport getDataColumn() {
        return _dataColumn;
    }
}
