package org.ecclesiacantic.gui;

import javafx.beans.property.StringProperty;
import org.ecclesiacantic.utils.StringUtils;

public class MessageProvider {

    static private MessageProvider _instance;

    static public final MessageProvider getInstance() {
        if (_instance == null) {
            _instance = new MessageProvider();
        }
        return _instance;
    }

    private StringProperty _console;

    private MessageProvider() {

    }

    static public final void append(final String parS) {
        if (StringUtils.isNullOrEmpty(parS)) {
            return;
        }

        assert getInstance()._console != null;

        final String locCurrentValue = _instance._console.get();
        _instance._console.setValue(String.format("%s\n%s", locCurrentValue, parS));
    }

    public final void setStringProperty(final StringProperty parStringProperty) {
        _console = parStringProperty;
    }
}
