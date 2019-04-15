package org.ecclesiacantic.gui.properties;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.ecclesiacantic.model.data.archi.EnumDataColumImport;

public class ColumnImportProperty implements IPropertyField {

    private final EnumDataColumImport _column;
    private final RadioButton _radioButton, _maybeNull;
    private final TextField _textField;

    public ColumnImportProperty(final EnumDataColumImport parColumn) {
        _column = parColumn;
        _radioButton = new RadioButton("Actif");
        _maybeNull = new RadioButton("Peut être vide");
        _textField = new TextField(_column.getHeaderName());
        GuiPropertyManager.getInstance().register(this);
    }

    public final HBox toHbox() {
        _radioButton.setSelected(_column.isActive());
        _maybeNull.setSelected(_column.isMaybeEmpty());
        return new HBox(15, _radioButton, new Label(_column.name()), _textField, _maybeNull);
    }

    @Override
    public void store() {
        _column.setHeaderName(_textField.getText());
        _column.setActive(_radioButton.isSelected());
        _column.setMaybeEmpty(_maybeNull.isSelected());
    }

    @Override
    public String getEnumRefName() {
        return _column.name();
    }
}
