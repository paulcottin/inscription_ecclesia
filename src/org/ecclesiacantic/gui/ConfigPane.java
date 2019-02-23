package org.ecclesiacantic.gui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.ecclesiacantic.gui.properties.GuiPropertyManager;
import org.ecclesiacantic.gui.types.google.GooglePropertiesPane;
import org.ecclesiacantic.gui.types.local.LocalFilePropertiesPane;

public class ConfigPane extends Scene {

    public ConfigPane() {
        super(new BorderPane(), 900, 700);
        initComponents();
    }

    private final void initComponents() {
        final BorderPane locRoot = ((BorderPane) getRoot());

        final VBox locVBox = new VBox(15, new GooglePropertiesPane(), new LocalFilePropertiesPane(), initSaveButton());


        locRoot.setCenter(locVBox);
    }

    private final Button initSaveButton() {

        final Button locButton = new Button("Enregistrer");
        locButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (EventHandler<Event>) parEvent -> {
            GuiPropertyManager.getInstance().storeAllProperties();
            ((Stage) (((Button) parEvent.getSource()).getScene().getWindow())).close();
        });
        return locButton;
    }
}
