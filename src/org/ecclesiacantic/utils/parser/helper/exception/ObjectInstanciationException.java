package org.ecclesiacantic.utils.parser.helper.exception;

import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.utils.parser.helper.AParseErrorContent;
import org.ecclesiacantic.utils.parser.helper.ParseErrorFactory;

public class ObjectInstanciationException extends AParseException {

    protected final EnumDataColumImport _columImport;
    protected final String _idValue;

    public ObjectInstanciationException(final String parTypeName, final Exception parE, final EnumDataColumImport parColumImport, final String parIdValue) {
        super(parTypeName, parE);
        _columImport = parColumImport;
        _idValue = parIdValue;
    }

    public ObjectInstanciationException(final String parTypeName, final EnumDataColumImport parColumImport, final String parIdValue) {
        this(parTypeName, null, parColumImport, parIdValue);
    }

    @Override
    protected AParseErrorContent computeContent(final Exception parCause) {
        return ParseErrorFactory.computeObjectInstanciation(parCause, _columImport, _idValue);
    }
}
