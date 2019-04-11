package org.ecclesiacantic.utils.parser.helper.error_content;

import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.utils.parser.helper.AParseErrorContent;

public class ObjectInstanciationError extends AParseErrorContent {

    protected final EnumDataColumImport _columImport;
    protected final String _idValue;

    public ObjectInstanciationError(final EnumDataColumImport parColumImport, final String parIdValue) {
        _columImport = parColumImport;
        _idValue = parIdValue;
    }

    @Override
    public String getErrorString() {
        return String.format("Impossible de trouver une valeur pour la colonne \"%s\" pour la ligne d'identifiant \"%s\"", _columImport.getHeaderName(), _idValue);
    }
}
