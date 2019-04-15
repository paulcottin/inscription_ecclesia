package org.ecclesiacantic.utils.parser.helper.error_content;

import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.utils.parser.helper.AParseErrorContent;

import java.util.Collection;

public class CsvParseError extends AParseErrorContent {

    protected final int _lineIdx;
    protected final EnumDataColumImport _column;
    protected final Collection<String> _availableValues;

    public CsvParseError(final int parLineIdx, final EnumDataColumImport parColumImport, final Collection<String> parAvailableValues) {
        super();
        _lineIdx = parLineIdx;
        _column = parColumImport;
        _availableValues = parAvailableValues;
    }

    @Override
    public String getErrorString() {
        int locNbCol = 0;
        final StringBuilder locStringBuilder = new StringBuilder();
        for (final String locCol : _availableValues) {
            if (locNbCol % 5 == 0) {
                locStringBuilder.append("\n");
            }
            if (locNbCol != _availableValues.size()) {
                locStringBuilder.append(locCol).append(", ");
            }
            locNbCol++;
        }
        final String locAvailablesValues = locStringBuilder.toString();
        return String.format("Erreur lors du parsing du fichier à la ligne %d pour la colonne %s. \nColonnes disponibles : %s", _lineIdx, _column.getHeaderName(), locAvailablesValues);
    }
}
