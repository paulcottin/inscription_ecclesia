package org.ecclesiacantic.utils.parser.helper.exception;

import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.utils.parser.helper.AParseErrorContent;
import org.ecclesiacantic.utils.parser.helper.ParseErrorFactory;

import java.util.Collection;
import java.util.Collections;

public class CsvParseException extends AParseException {

    private final int _lineIdx;
    private final EnumDataColumImport _column;
    private final Collection<String> _availableValues;

    public CsvParseException(final String parTypeName, final Exception parCause) {
        this(parTypeName, parCause, -1, null, Collections.emptyList());
    }

    public CsvParseException(final String parTypeName, final Exception parCause, final int parLineIdx, final EnumDataColumImport parColumImport, final Collection<String> parAvailableValues) {
        super(parTypeName, parCause);
        _lineIdx = parLineIdx;
        _column = parColumImport;
        _availableValues = parAvailableValues;
    }

    public CsvParseException(final Exception parCause, final int parLineIdx, final EnumDataColumImport parColumImport) {
        this(null, parCause, parLineIdx, parColumImport, Collections.emptyList());
    }

    public CsvParseException(final int parLineIdx, final EnumDataColumImport parColumImport, final Collection<String> parAvailableValues) {
        this(null, null, parLineIdx, parColumImport, parAvailableValues);

    }

    @Override
    protected AParseErrorContent computeContent(final Exception parCause) {
        return ParseErrorFactory.computeCSV(parCause, _lineIdx, _column, _availableValues);
    }
}
