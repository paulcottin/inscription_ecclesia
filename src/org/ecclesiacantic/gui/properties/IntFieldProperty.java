package org.ecclesiacantic.gui.properties;

import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import org.ecclesiacantic.config.EnumConfigProperty;

public class IntFieldProperty extends AValidatedTextFieldProperty {

    public IntFieldProperty(final String parLabel, final EnumConfigProperty parProperty) {
        super(parLabel, parProperty);
    }

    @Override
    protected TextFormatter getTextFormatter() {
        return new TextFormatter<>(new IntegerStringConverter());
    }
}
