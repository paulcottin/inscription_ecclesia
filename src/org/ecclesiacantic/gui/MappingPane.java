package org.ecclesiacantic.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.ecclesiacantic.gui.properties.ColumnImportProperty;
import org.ecclesiacantic.gui.properties.GuiPropertyManager;
import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.model.data.archi.EnumDataColumnImportList;
import org.ecclesiacantic.config.OverrideColumnNameManager;

public class MappingPane extends Scene {

    public MappingPane() {
        super(new BorderPane(), 900, 700);
        initComponents();
    }

    private final void initComponents() {
        final BorderPane locRoot = ((BorderPane) getRoot());
        final ScrollPane locScrollPane = new ScrollPane();
        locScrollPane.setFitToHeight(true);
        locScrollPane.setFitToWidth(true);

        final VBox locVBox = new VBox(15);
        for (final EnumDataColumnImportList locColumnImportList : EnumDataColumnImportList.values()) {
            locVBox.getChildren().add(initHelper(locColumnImportList.getName(), locColumnImportList));
        }

        locScrollPane.setContent(locVBox);

        locRoot.setCenter(locScrollPane);
        locRoot.setBottom(new BorderPane(initSaveBtn()));
    }

    private final TitledPane initHelper(final String parTitle, final EnumDataColumnImportList parEnumDataColumnImportList) {
        final TitledPane locTitledPane = new TitledPane();
        locTitledPane.setText(parTitle);
        locTitledPane.setExpanded(false);

        final VBox locVBox = new VBox(10);
        for (final EnumDataColumImport locColumnImport : parEnumDataColumnImportList.getColumns()) {
            locVBox.getChildren().add(new ColumnImportProperty(locColumnImport).toHbox());
        }

        locTitledPane.setContent(locVBox);

        return locTitledPane;
    }

    private final Button initSaveBtn() {
        final Button locButton = new Button("Enregistrer");
        locButton.setOnAction(parEvent -> {
            GuiPropertyManager.getInstance().storeAllProperties();
            ((Stage) (((Button) parEvent.getSource()).getScene().getWindow())).close();
        });
        return locButton;
    }
}
