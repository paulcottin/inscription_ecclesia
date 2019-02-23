package org.ecclesiacantic.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.ecclesiacantic.RunAlgo;

public class MainGui extends Scene {

    private final Stage _stage;

    public MainGui(Stage parStage) {
        super(new BorderPane(), 700, 800);
        this._stage = parStage;
        initComponents();
    }

    private final void initComponents() {
        _stage.setTitle("Inscription Ecclesia Cantic");
        final BorderPane locRoot = (BorderPane) getRoot();
        locRoot.setTop(new BorderPane(initConfigButton()));
        locRoot.setCenter(new ConfigAlgoPane());
        locRoot.setBottom(new BorderPane(initLaunchAlgoButton()));

    }

    private final Button initConfigButton() {
        final Button loConfigBtn = new Button("Configuration");
        loConfigBtn.setOnAction(event -> {
            final ConfigPane locConfigPane = new ConfigPane();
            final Stage locConfigStage = new Stage();
            locConfigStage.setTitle("Configuration");
            locConfigStage.setScene(locConfigPane);
            locConfigStage.show();
        });

        return loConfigBtn;
    }

    private final Button initLaunchAlgoButton() {
        final Button loConfigBtn = new Button("Lancer l'algorithme");
        loConfigBtn.setOnAction(event -> {
            final RunAlgo locRunAlgo = new RunAlgo();
            locRunAlgo.run();
        });

        return loConfigBtn;
    }

    public final void show() {
        _stage.setScene(this);
        _stage.show();
    }
}
