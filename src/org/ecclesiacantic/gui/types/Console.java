package org.ecclesiacantic.gui.types;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;

public class Console extends OutputStream {

    private TextArea _output;

    public Console(final TextArea parTextArea) {
        this._output = parTextArea;
    }

    @Override
    public void write(int i) throws IOException {
        Platform.runLater(() -> {
            final String locCurrentValue = _output.textProperty().getValue();
            _output.textProperty().setValue(locCurrentValue + String.valueOf((char) i));
        });
    }
}
