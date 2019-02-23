package org.ecclesiacantic.gui.properties;

import javafx.scene.control.TextFormatter;
import javafx.util.converter.DoubleStringConverter;
import org.ecclesiacantic.config.EnumConfigProperty;

public class DoubleFieldProperty extends IntFieldProperty {

    public DoubleFieldProperty(final String parLabel, final EnumConfigProperty parProperty) {
        super(parLabel, parProperty);
    }

    @Override
    protected TextFormatter getTextFormatter() {
        return new TextFormatter<>(new DoubleStringConverter());
    }
}
