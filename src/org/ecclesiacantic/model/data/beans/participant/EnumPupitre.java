package org.ecclesiacantic.model.data.beans.participant;

import org.ecclesiacantic.model.data.archi.itf.ISimpleValueEnum;

public enum EnumPupitre implements ISimpleValueEnum {

    SOPRANO("Soprano"), TENOR("Ténor"), ALTO("Alto"), BASSE("Basse"), NE_SAIS_PAS("Je ne sais pas");

    private String _value;

    EnumPupitre(final String parValue) {
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
