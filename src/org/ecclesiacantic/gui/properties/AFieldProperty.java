package org.ecclesiacantic.gui.properties;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.ecclesiacantic.config.EnumConfigProperty;

public abstract class AFieldProperty implements IPropertyField{

    protected final String _label;
    protected final EnumConfigProperty _property;

    public AFieldProperty(final String parLabel, final EnumConfigProperty parProperty) {
        _label = parLabel;
        _property = parProperty;
        GuiPropertyManager.getInstance().register(this);
    }

    protected abstract Node initPropertyField();

    public final HBox toHbox() {
        final Label locPropertyLbl = new Label(_label);

        return new HBox(15, locPropertyLbl, initPropertyField());
    }
}
