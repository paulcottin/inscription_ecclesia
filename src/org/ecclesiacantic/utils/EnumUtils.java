package org.ecclesiacantic.utils;

import org.ecclesiacantic.model.data.archi.itf.ISimpleValueEnum;
import org.ecclesiacantic.model.data.archi.EnumDataColumnExportList;

import java.util.ArrayList;
import java.util.List;

public class EnumUtils {

    static public final ISimpleValueEnum getEnumFromString(final Class<? extends ISimpleValueEnum> parEnum,
                                                           final String parStringValue)  {
        for (final ISimpleValueEnum locEnumValue : parEnum.getEnumConstants()) {
            if (locEnumValue.getValue().equals(parStringValue)) {
                return locEnumValue;
            }
        }
        System.err.println(String.format("Impossible de trouver une valeur d'Enum de type %s pour la valeur %s",
                parEnum.toString(), parStringValue));
        return null;
    }

    static public final List<String> emptyListFromEnum(final EnumDataColumnExportList parEnumDataColumnExportList) {
        final int locListSize = parEnumDataColumnExportList.asList().size();
        final List<String> locReturnList = new ArrayList<>(locListSize);
        for (int locI = 0; locI < locListSize; locI++) {
            locReturnList.add("");
        }
        return locReturnList;
    }
}
