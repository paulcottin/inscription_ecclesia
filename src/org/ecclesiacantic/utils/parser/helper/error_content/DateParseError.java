package org.ecclesiacantic.utils.parser.helper.error_content;

import org.ecclesiacantic.model.data.archi.EnumDataColumImport;

public class DateParseError extends ObjectInstanciationError {

    private final String _currentValue;

    public DateParseError(final EnumDataColumImport parColumImport, final String parIdValue, final String parCurrentValue) {
        super(parColumImport, parIdValue);
        _currentValue = parCurrentValue;
    }

    @Override
    public String getErrorString() {
        return String.format("Impossible de parser la date \"%s\" pour la colonne \"%s\" pour l'identifiant \"%s\"", _currentValue, _columImport.getHeaderName(), _idValue);
    }
}
