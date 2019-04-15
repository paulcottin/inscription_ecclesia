package org.ecclesiacantic.utils.parser.helper.error_content;

import org.ecclesiacantic.model.data.archi.EnumDataColumImport;

public class IntParseError extends AConvertTypeError {

    protected final String _currentValue;

    public IntParseError(final EnumDataColumImport parColumImport, final String parIdValue, final String parCurrentValue) {
        super(parColumImport, parIdValue, parCurrentValue);
        _currentValue = parCurrentValue;
    }

    @Override
    public String getErrorString() {
        return String.format("Impossible de convertir en nombre entier la valeur \"%s\" pour la colonne \"%s\" pour l'identifiant \"%s\"", _currentValue, _columImport.getHeaderName(), _idValue);
    }
}
