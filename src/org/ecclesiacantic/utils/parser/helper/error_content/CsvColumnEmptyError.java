package org.ecclesiacantic.utils.parser.helper.error_content;

import org.ecclesiacantic.model.data.archi.EnumDataColumImport;

import java.util.Collections;

public class CsvColumnEmptyError extends CsvParseError {

    public CsvColumnEmptyError(final int parLineIdx, final EnumDataColumImport parColumImport) {
        super(parLineIdx, parColumImport, Collections.emptyList());
    }

    @Override
    public String getErrorString() {
        return String.format("Erreur lors du parsing du fichier à la ligne %d pour la colonne %s. \nLa valeur n'est pas présente", _lineIdx, _column.getHeaderName());
    }
}
