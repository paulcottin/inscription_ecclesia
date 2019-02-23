package org.ecclesiacantic.gui.properties;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;
import org.ecclesiacantic.config.EnumConfigProperty;

public class IntFieldProperty extends TextFieldProperty {

    public IntFieldProperty(final String parLabel, final EnumConfigProperty parProperty) {
        super(parLabel, parProperty);
    }

    @Override
    protected Node initPropertyField() {
        final TextField locTextField = new TextField();
        locTextField.setTextFormatter(getTextFormatter());
        locTextField.setText(_property.stringV());
        return locTextField;
    }

    protected TextFormatter getTextFormatter() {
        return new TextFormatter<>(new IntegerStringConverter());
    }
}
