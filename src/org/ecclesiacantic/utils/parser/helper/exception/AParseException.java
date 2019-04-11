package org.ecclesiacantic.utils.parser.helper.exception;

import org.ecclesiacantic.utils.StringUtils;
import org.ecclesiacantic.utils.parser.helper.AParseErrorContent;
import org.ecclesiacantic.utils.parser.helper.ParseErrorFactory;

public abstract class AParseException extends Exception {

    protected final String _typeName;
    protected final AParseErrorContent _content;

    public AParseException(final String parTypeName, final Exception parCause) {
        super(parCause);
        _typeName = parTypeName;
        _content = ParseErrorFactory.computeError(parCause);
    }

    protected abstract String preStandardMsg();

    protected abstract String postStandardMsg();

    public final String getUserMessage() {
        final String locPreStandardMsg = StringUtils.isNullOrEmpty(preStandardMsg()) ? "" : preStandardMsg() + "\n";
        final String locPostStandardMsg = StringUtils.isNullOrEmpty(postStandardMsg()) ? "" : postStandardMsg() + "\n";
        return String.format("%s%s%s", locPreStandardMsg, _content.getErrorString(), locPostStandardMsg);
    }

    public final String getTitle() {
        return String.format("Erreur sur les %s", _typeName);
    }
}
