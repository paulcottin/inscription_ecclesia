package org.ecclesiacantic.utils.parser.helper.error_content;

import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.utils.parser.helper.AParseErrorContent;

import java.util.Collection;
import java.util.Collections;

public class ObjectInstanciationError extends AParseErrorContent {

    protected final EnumDataColumImport _columImport;
    protected final String _idValue;
    protected final Collection<String> _availableValues;

    public ObjectInstanciationError(final EnumDataColumImport parColumImport, final String parIdValue, final Collection<String> parAvailableValues) {
        _columImport = parColumImport;
        _idValue = parIdValue;
        _availableValues = parAvailableValues;
    }

    public ObjectInstanciationError(final EnumDataColumImport parColumImport, final String parIdValue) {
        this(parColumImport, parIdValue, Collections.emptyList());
    }

    @Override
    public String getErrorString() {
        final String locValues = _availableValues.isEmpty() ? "" : String.format(".\nValeurs disponibles : %s", String.join(", ", _availableValues));
        return String.format("Impossible de trouver une valeur pour la colonne \"%s\" pour la ligne d'identifiant \"%s\"%s", _columImport.getHeaderName(), _idValue, locValues);
    }
}
