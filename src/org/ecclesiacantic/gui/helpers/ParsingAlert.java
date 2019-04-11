package org.ecclesiacantic.gui.helpers;

import javafx.scene.control.Alert;
import org.ecclesiacantic.utils.parser.helper.exception.AParseException;

public class ParsingAlert extends Alert {

    public ParsingAlert(final AParseException parE) {
        super(AlertType.ERROR);
        setTitle("Erreur");
        setHeaderText(parE.getTitle());
        setContentText(parE.getUserMessage());
    }
}
