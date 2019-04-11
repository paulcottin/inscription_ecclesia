package org.ecclesiacantic.gui.properties;

import javafx.scene.Node;
import javafx.scene.control.TextFormatter;
import org.ecclesiacantic.config.EnumConfigProperty;

public abstract class AValidatedTextFieldProperty extends TextFieldProperty {

    public AValidatedTextFieldProperty(final String parLabel, final EnumConfigProperty parProperty) {
        super(parLabel, parProperty);
    }

    @Override
    protected Node initPropertyField() {
        _propertyField.setTextFormatter(getTextFormatter());
        _propertyField.setText(_property.stringV());
        return _propertyField;
    }

    protected abstract TextFormatter getTextFormatter();
}
