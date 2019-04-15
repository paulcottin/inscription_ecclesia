package org.ecclesiacantic.gui;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.ecclesiacantic.config.ConfigManager;
import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.gui.helpers.ParsingAlert;
import org.ecclesiacantic.gui.properties.ColumnImportProperty;
import org.ecclesiacantic.gui.properties.GuiPropertyManager;
import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.model.data.archi.EnumDataColumnImportList;
import org.ecclesiacantic.config.OverrideColumnNameManager;
import org.ecclesiacantic.model.data.archi.EnumDataType;
import org.ecclesiacantic.model.data_manager.AllDataManager;
import org.ecclesiacantic.utils.parser.helper.exception.AParseException;

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
        for (final EnumDataType locDataType : EnumDataType.values()) {
            final EnumDataColumnImportList locImportList = locDataType.getColumnImportList();
            if (locImportList == null) {
                continue;
            }
            locVBox.getChildren().add(initHelper(locDataType));
        }

        locScrollPane.setContent(locVBox);

        locRoot.setCenter(locScrollPane);
        locRoot.setBottom(new HBox(50, initSaveBtn(), initCloseButton()));
    }

    private final TitledPane initHelper(final EnumDataType parDataType) {
        final TitledPane locTitledPane = new TitledPane();
        locTitledPane.setText(parDataType.getTypeName());
        locTitledPane.setExpanded(false);

        final VBox locVBox = new VBox(10);
        for (final EnumDataColumImport locColumnImport : parDataType.getColumnImportList().getColumns()) {
            locVBox.getChildren().add(new ColumnImportProperty(locColumnImport).toHbox());
        }

        final Button locCheckButton = new Button("Test");
        locCheckButton.setOnAction(event -> {
            Platform.runLater(() -> locCheckButton.setDisable(true));
            GuiPropertyManager.getInstance().storeAllProperties();
            ConfigManager.getInstance().writeStandardProperties();
            new Thread(() -> {
                try {
                    AllDataManager.getInstance().get(parDataType).testDataFile();
                    Platform.runLater(() -> {
                        final Alert locAlert = new Alert(Alert.AlertType.INFORMATION);
                        locAlert.setHeaderText("Parsing du fichier terminé en succès");
                        locAlert.setTitle("Test du parsing d'un fichier de données");
                        locAlert.showAndWait();
                    });
                } catch (final AParseException parE) {
                    Platform.runLater(() -> new ParsingAlert(parE).showAndWait());
                } finally {
                    locCheckButton.setDisable(false);
                }
            }).start();
        });

        locTitledPane.setContent(new HBox(20, locVBox, locCheckButton));

        return locTitledPane;
    }

    private final Button initSaveBtn() {
        return initCloseButtonHelper("Enregistrer", false);
    }

    private final Button initCloseButton() {
        return initCloseButtonHelper("Fermer", true);
    }

    private final Button initCloseButtonHelper(final String parButtonText, final boolean parClose) {
        final Button locButton = new Button(parButtonText);
        locButton.setOnAction(parEvent -> {
            GuiPropertyManager.getInstance().storeAllProperties();
            if (parClose) {
                ((Stage) (((Button) parEvent.getSource()).getScene().getWindow())).close();
            }
        });
        return locButton;
    }
}
