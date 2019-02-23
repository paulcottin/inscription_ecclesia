package org.ecclesiacantic.gui.types.google;

import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.gui.properties.TextFieldProperty;

public class GooglePropertiesPane extends TitledPane {

    public GooglePropertiesPane() {
        super();
        setText("Propriétés Google");
        setContent(new BorderPane(initPane()));
        setExpanded(false);
    }

    private final VBox initPane() {
        final TextFieldProperty locAccountField = new TextFieldProperty("Adresse mail utilisée pour la connection : ", EnumConfigProperty.API_EMAIL);

        final TitledPane locSpreadsheetGooglePanes = new TitledPane();
        locSpreadsheetGooglePanes.setText("Spreadsheet Google");
        locSpreadsheetGooglePanes.setExpanded(false);
        locSpreadsheetGooglePanes.setContent(new VBox(10,
                new ParticipantGooglePropertiesPane(),
                new MasterclassGooglePropertiesPane(),
                new SalleGooglePropertiesPane(),
                new ChoraleGooglePropertiesPane(),
                new SoloGeographiquesPropertiesPane(),
                new GroupeConcertGooglePropertiesPane()
                ));

        return new VBox(10, locAccountField.toHbox(), locSpreadsheetGooglePanes);
    }
}
