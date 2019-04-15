package org.ecclesiacantic.gui.properties;

import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import org.ecclesiacantic.config.ConfigManager;
import org.ecclesiacantic.config.EnumConfigProperty;

public class BooleanFieldProperty extends AFieldProperty {

    private boolean _response;
    private final String _trueButtonString, _falseButtonString;

    public BooleanFieldProperty(final String parLabel, final EnumConfigProperty parProperty, final String parTrueButtonString, final String parFalseButtonString) {
        super(parLabel, parProperty);
        _response = parProperty.boolV();
        _trueButtonString = parTrueButtonString;
        _falseButtonString = parFalseButtonString;
    }

    @Override
    protected Node initPropertyField() {
        final ToggleButton locTrueBtn = new ToggleButton(_trueButtonString);
        final ToggleButton locFalseBtn = new ToggleButton(_falseButtonString);
        locTrueBtn.setSelected(_response);
        locFalseBtn.setSelected(!_response);

        final ToggleGroup locToggleGroup = new ToggleGroup();
        locTrueBtn.setToggleGroup(locToggleGroup);
        locFalseBtn.setToggleGroup(locToggleGroup);

        locToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            _response = !_response;
            if (_property.isSaveOnGuiChange()) {
                store();
            }
        });

        return new HBox(5, locTrueBtn, locFalseBtn);
    }

    @Override
    public void store() {
        ConfigManager.getInstance().setProperty(_property.getKey(), _response ? "1" : "0");
    }
}
