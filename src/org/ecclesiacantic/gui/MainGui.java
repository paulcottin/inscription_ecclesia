package org.ecclesiacantic.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainGui extends Scene {

    private final Stage _stage;

    public MainGui(Stage parStage) {
        super(new BorderPane(), 700, 800);
        this._stage = parStage;
        initComponents();
    }

    private final void initComponents() {
        _stage.setTitle("Inscription Ecclesia Cantic");
        final Button loConfigBtn = new Button("Configuration");
        loConfigBtn.setOnAction(event -> {
            final ConfigPane locConfigPane = new ConfigPane();
            final Stage locConfigStage = new Stage();
            locConfigStage.setTitle("Configuration");
            locConfigStage.setScene(locConfigPane);
            locConfigStage.show();
        });
        ((BorderPane) getRoot()).setCenter(loConfigBtn);
    }

    public final void show() {
        _stage.setScene(this);
        _stage.show();
    }
}
