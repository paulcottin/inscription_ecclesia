package org.ecclesiacantic.gui.properties;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.ecclesiacantic.model.data.archi.itf.ISimpleValueEnum;

public class EnumProperty implements IPropertyField {

    private final String _enumName;
    private final ISimpleValueEnum _column;
    private final TextField _textField;

    public EnumProperty(final Enum parColumn) {
        assert parColumn instanceof ISimpleValueEnum;
        _enumName = parColumn.name();
        _column = (ISimpleValueEnum) parColumn;
        _textField = new TextField(_column.getValue());
        GuiPropertyManager.getInstance().register(this);
    }

    public final HBox toHbox() {
        return new HBox(15, new Label(_enumName), _textField);
    }

    @Override
    public void store() {
        _column.setValue(_textField.getText());
    }

    @Override
    public String getEnumRefName() {
        return _enumName;
    }
}
