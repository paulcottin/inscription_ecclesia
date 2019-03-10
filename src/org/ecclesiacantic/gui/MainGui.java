package org.ecclesiacantic.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.ecclesiacantic.RunAlgo;
import org.ecclesiacantic.config.ConfigManager;
import org.ecclesiacantic.gui.properties.GuiPropertyManager;
import org.ecclesiacantic.gui.types.Console;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

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
        locRoot.setCenter(new VBox(20,
                new BorderPane(new HBox(15, initConfigButton(), initMappingButton())),
                new ConfigAlgoPane(),
                new BorderPane(initLaunchAlgo())
        ));
    }

    private final Button initConfigButton() {
        final Button locConfigBtn = new Button("Configuration");
        locConfigBtn.setOnAction(event -> {
            final ConfigPane locConfigPane = new ConfigPane();
            final Stage locConfigStage = new Stage();
            locConfigStage.setTitle("Configuration");
            locConfigStage.setScene(locConfigPane);
            locConfigStage.show();
        });

        return locConfigBtn;
    }

    private final Button initMappingButton() {
        final Button locMappingBtn = new Button("Mapping");
        locMappingBtn.setOnAction(event -> {
            final MappingPane locMappingPane = new MappingPane();
            final Stage locConfigStage = new Stage();
            locConfigStage.setTitle("Mapping");
            locConfigStage.setScene(locMappingPane);
            locConfigStage.show();
        });

        return locMappingBtn;
    }

    private final Pane initLaunchAlgo() {
        final Button locLaunchBtn = new Button("Lancer l'algorithme");
        locLaunchBtn.setOnAction(event -> {
            GuiPropertyManager.getInstance().storeAllProperties();
            final RunAlgo locRunAlgo = new RunAlgo();
            ConfigManager.getInstance().writeStandardProperties();
            new Thread(locRunAlgo::run).start();
        });


        final TextArea locTextArea = new TextArea();
        locTextArea.setMinHeight(200.0);
        redirectOutputsToTextArea(locTextArea);

        return new VBox(15, new BorderPane(locLaunchBtn), locTextArea);
    }

    private final void redirectOutputsToTextArea(final TextArea parTextArea) {
        final Console console = new Console(parTextArea);
        try {
            final PrintStream ps = new PrintStream(console, true, "UTF-8");
            System.setOut(ps);
            System.setErr(ps);
        } catch (UnsupportedEncodingException parE) {
            parE.printStackTrace();
        }
    }

    public final void show() {
        _stage.setScene(this);
        _stage.show();
    }
}
