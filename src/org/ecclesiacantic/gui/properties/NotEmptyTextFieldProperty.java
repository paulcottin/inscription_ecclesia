package org.ecclesiacantic.gui.properties;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.utils.StringUtils;

public class NotEmptyTextFieldProperty extends AValidatedTextFieldProperty {


    public NotEmptyTextFieldProperty(final String parLabel, final EnumConfigProperty parProperty) {
        super(parLabel, parProperty);
    }

    @Override
    protected TextFormatter getTextFormatter() {
        return new TextFormatter<>(new StringConverter<String>() {
            @Override
            public String toString(final String parObject) {
                return StringUtils.isNullOrEmpty(parObject) ? "" : parObject;
            }

            @Override
            public String fromString(final String parString) {
                return StringUtils.isNullOrEmpty(parString) ? "[Non vide]" : parString;
            }
        });
    }
}
