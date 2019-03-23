package org.ecclesiacantic.gui.properties;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import org.ecclesiacantic.config.ConfigManager;
import org.ecclesiacantic.config.EnumConfigProperty;

public class IntFieldProperty extends TextFieldProperty {

    public IntFieldProperty(final String parLabel, final EnumConfigProperty parProperty) {
        super(parLabel, parProperty);
    }

    @Override
    protected Node initPropertyField() {
        _propertyField.setTextFormatter(getTextFormatter());
        _propertyField.setText(_property.stringV());
        return _propertyField;
    }

    protected TextFormatter getTextFormatter() {
        return new TextFormatter<>(new IntegerStringConverter());
    }
}
