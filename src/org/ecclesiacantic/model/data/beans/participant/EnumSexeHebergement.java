package org.ecclesiacantic.model.data.beans.participant;

import org.ecclesiacantic.model.data.archi.itf.ISimpleValueEnum;

public enum EnumSexeHebergement implements ISimpleValueEnum {
    SEULEMENT_FILLE("Uniquement une (des) fille(s)"),

    SEULEMENT_GARCON("Uniquement un (des) garçon(s)"),

    LES_DEUX("Les deux"),

    AUCUN("");

    private String _value;

    EnumSexeHebergement(final String parValue) {
        _value = parValue;
    }

    @Override
    public String getValue() {
        return _value;
    }

    @Override
    public void setValue(final String parValue) {
        _value = parValue;
    }


}
