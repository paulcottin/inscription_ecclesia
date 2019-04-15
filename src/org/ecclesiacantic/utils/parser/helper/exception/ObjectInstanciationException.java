package org.ecclesiacantic.utils.parser.helper.exception;

import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.utils.parser.helper.AParseErrorContent;
import org.ecclesiacantic.utils.parser.helper.ParseErrorFactory;

import java.util.Collection;
import java.util.Collections;

public class ObjectInstanciationException extends AParseException {

    protected final EnumDataColumImport _columImport;
    protected final String _idValue;
    protected final Collection<String> _availableValues;

    public ObjectInstanciationException(final String parTypeName, final Exception parE, final EnumDataColumImport parColumImport,
                                        final String parIdValue, final Collection<String> parAvailableValues) {
        super(parTypeName, parE);
        _columImport = parColumImport;
        _idValue = parIdValue;
        _availableValues = parAvailableValues;
    }

    public ObjectInstanciationException(final String parTypeName, final Exception parE, final EnumDataColumImport parColumImport, final String parIdValue) {
        this(parTypeName, parE, parColumImport, parIdValue, Collections.emptyList());
    }

    public ObjectInstanciationException(final String parTypeName, final EnumDataColumImport parColumImport, final String parIdValue) {
        this(parTypeName, null, parColumImport, parIdValue, Collections.emptyList());
    }

    public ObjectInstanciationException(final String parTypeName, final EnumDataColumImport parColumImport,
                                        final String parIdValue, final Collection<String> parAvailableValues) {
        this(parTypeName, null, parColumImport, parIdValue, parAvailableValues);
    }

    @Override
    protected AParseErrorContent computeContent(final Exception parCause) {
        return ParseErrorFactory.computeObjectInstanciation(parCause, _columImport, _idValue, _availableValues);
    }
}
