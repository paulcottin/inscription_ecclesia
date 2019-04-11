package org.ecclesiacantic.utils.parser.helper;

public abstract class AParseErrorContent {

    protected String _typeName;

    public AParseErrorContent() {

    }

    public abstract String getErrorString();

    public final String getTitle() {
        return String.format("Erreur sur les %s", _typeName);
    }

    public final void setTypeName(final String parTypeName) {
        _typeName = parTypeName;
    }
}
