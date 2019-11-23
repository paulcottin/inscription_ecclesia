package org.ecclesiacantic.gui.types;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Console extends OutputStream {

    private TextArea _output;
    private List<Byte> _bytes;

    public Console(final TextArea parTextArea) {
        this._output = parTextArea;
        _bytes = new ArrayList<>();
    }

    @Override
    public void write(final int i) throws IOException {
        _bytes.add((byte)i);
        System.out.write(i);
        Platform.runLater(this::update);
    }

    @Override
    public void write(final byte[] i) {
        for (byte b : i) {
            _bytes.add(b);
            System.out.write(b);
        }
        Platform.runLater(this::update);
    }

    @Override
    public void flush() throws IOException {
        super.flush();
        Platform.runLater(this::update);
    }

    private final void update() {
        final byte[] locArray = new byte[_bytes.size()];
        int q = 0;
        for (final Byte locCurrent : _bytes) {
            locArray[q] = locCurrent;
            q++;
        }
        try {
            _output.setText(new String(locArray, "UTF-8"));
        } catch (final UnsupportedEncodingException parE) {
            parE.printStackTrace();
        }
    }
}
