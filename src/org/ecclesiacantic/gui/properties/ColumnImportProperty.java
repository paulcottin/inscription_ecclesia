package org.ecclesiacantic.gui.properties;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.ecclesiacantic.model.data.archi.EnumDataColumImport;

public class ColumnImportProperty implements IPropertyField {

    private final EnumDataColumImport _column;
    private final RadioButton _radioButton;
    private final TextField _textField;

    public ColumnImportProperty(final EnumDataColumImport parColumn) {
        _column = parColumn;
        _radioButton = new RadioButton("Actif");
        _textField = new TextField(_column.getHeaderName());
        GuiPropertyManager.getInstance().register(this);
    }

    public final HBox toHbox() {
        if (_column.isActive()) {
            _radioButton.setSelected(true);
        }
        return new HBox(15, _radioButton, new Label(_column.name()), _textField);
    }

    @Override
    public void store() {
        _column.setHeaderName(_textField.getText());
        _column.setActive(_radioButton.isSelected());
    }
}
