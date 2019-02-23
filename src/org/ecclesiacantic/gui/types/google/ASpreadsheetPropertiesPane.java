package org.ecclesiacantic.gui.types.google;

import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.google.GoogleSpreadsheetConfig;
import org.ecclesiacantic.gui.properties.TextFieldProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public abstract class ASpreadsheetPropertiesPane extends TitledPane {

    protected final String _title;
    protected final EnumConfigProperty _googleIdProperty;
    protected final GoogleSpreadsheetConfig _googleConfig;
    protected final Collection<TextFieldProperty> _fieldsProperty;

    protected ASpreadsheetPropertiesPane(final String parTitle, final EnumConfigProperty parGoogleIdProperty) {
        super();
        _title = parTitle;
        _googleIdProperty = parGoogleIdProperty;
        _googleConfig = EnumConfigProperty.googleConfigV(parGoogleIdProperty);
        setText(_title);
        _fieldsProperty = new ArrayList<>(3);
        initProperties();
        displayProperties();
    }

    private final void initProperties() {
        final TextFieldProperty locGoogleIdField = new TextFieldProperty("Google ID : ", _googleIdProperty);
        final TextFieldProperty locRangeField = new TextFieldProperty("Range de données (au format Fichier inscrits 15/11 00h30!A1:AS)", EnumConfigProperty.property(_googleConfig.getDataRangeKey()));
        final TextFieldProperty locFileField = new TextFieldProperty("Nom du fichier à enregistrer en local", EnumConfigProperty.property(_googleConfig.getResultFileNameKey()));
        _fieldsProperty.addAll(Arrays.asList(locGoogleIdField, locRangeField, locFileField));
    }

    private final void displayProperties() {
        final VBox locVBox = new VBox(10);
        for (final TextFieldProperty locTextFieldProperty : _fieldsProperty) {
            locVBox.getChildren().add(locTextFieldProperty.toHbox());
        }
        setContent(new BorderPane(locVBox));
    }
}
