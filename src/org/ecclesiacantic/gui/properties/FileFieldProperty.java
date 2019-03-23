package org.ecclesiacantic.gui.properties;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.ecclesiacantic.config.ConfigManager;
import org.ecclesiacantic.config.EnumConfigProperty;

import java.io.File;

public class FileFieldProperty extends TextFieldProperty {

    private File _selectedFile;

    public FileFieldProperty(final String parLabel, final EnumConfigProperty parProperty) {
        super(parLabel, parProperty);
        _selectedFile = parProperty.fileV();
    }

    @Override
    protected Node initPropertyField() {
        final TextField locTextField = new TextField(_selectedFile == null ? "" : _selectedFile.getName());
        final Button locButton = new Button("...");
        locButton.setOnAction(parEvent -> {
            final File locInitFile = _selectedFile == null || !_selectedFile.exists() ? new File(".") : _selectedFile;
            final FileChooser locFileChooser = new FileChooser();
            if (locInitFile.isDirectory()) {
                locFileChooser.setInitialDirectory(locInitFile);
            } else {
                locFileChooser.setInitialDirectory(locInitFile.getParentFile());
                locFileChooser.setInitialFileName(locInitFile.getName());
            }
            locFileChooser.setTitle(_label);
            final File locResult = locFileChooser.showOpenDialog(((Button)parEvent.getSource()).getScene().getWindow());
            if (locResult == null) {
                _selectedFile = new File(".");
                return;
            }
            _selectedFile = locResult;
            locTextField.setText(locResult.getName());
        });

        return new HBox(15, locTextField, locButton);
    }

    @Override
    public void store() {
        ConfigManager.getInstance().setProperty(_property.getKey(), _selectedFile.getAbsolutePath());
    }
}
