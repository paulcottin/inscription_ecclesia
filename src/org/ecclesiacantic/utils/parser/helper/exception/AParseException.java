package org.ecclesiacantic.utils.parser.helper.exception;

import org.ecclesiacantic.utils.parser.helper.AParseErrorContent;

public abstract class AParseException extends Exception {

    protected AParseErrorContent _content;
    private final String _typeName;
    private final Exception _cause;

    public AParseException(final String parTypeName, final Exception parCause) {
        super(parCause);
        _typeName = parTypeName;
        _cause = parCause;
    }

    protected abstract AParseErrorContent computeContent(final Exception parCause);

    public final AParseErrorContent getContent() {
        if (_content == null) {
            _content = computeContent(_cause);
            _content.setTypeName(_typeName);
        }
        return _content;
    }
}
