package org.ecclesiacantic.gui.properties;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import org.ecclesiacantic.config.ConfigManager;
import org.ecclesiacantic.config.EnumConfigProperty;

public class TextFieldProperty extends AFieldProperty {

    protected final TextField _propertyField;

    public TextFieldProperty(final String parLabel, final EnumConfigProperty parProperty) {
        super(parLabel, parProperty);
        _propertyField = new TextField(_property.stringV());
    }

    @Override
    protected Node initPropertyField() {
        return _propertyField;
    }

    public void store() {
        ConfigManager.getInstance().setProperty(_property.getKey(), _propertyField.getText());
    }
}
