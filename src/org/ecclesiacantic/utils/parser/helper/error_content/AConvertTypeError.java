package org.ecclesiacantic.utils.parser.helper.error_content;

import org.ecclesiacantic.model.data.archi.EnumDataColumImport;

import java.util.Collections;

public class AConvertTypeError extends ObjectInstanciationError {

    protected final String _currentValue;

    public AConvertTypeError(final EnumDataColumImport parColumImport, final String parIdValue, final String parCurrentValue) {
        super(parColumImport, parIdValue, Collections.emptyList());
        _currentValue = parCurrentValue;
    }
}
