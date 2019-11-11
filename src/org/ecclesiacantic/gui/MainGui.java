package org.ecclesiacantic.gui;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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
import org.ecclesiacantic.gui.helpers.ParsingAlert;
import org.ecclesiacantic.gui.properties.GuiPropertyManager;
import org.ecclesiacantic.gui.types.Console;
import org.ecclesiacantic.utils.parser.helper.exception.AParseException;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class MainGui extends Scene {

    private final Stage _stage;
    private Button _configBtn, _mappingBtn;

    public MainGui(Stage parStage) {
        super(new BorderPane(), 700, 800);
        this._stage = parStage;
        _configBtn = new Button("Configuration");
        _mappingBtn = new Button("Mapping");
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
        _configBtn.setOnAction(event -> {
            final ConfigPane locConfigPane = new ConfigPane();
            final Stage locConfigStage = new Stage();
            locConfigStage.setTitle("Configuration");
            locConfigStage.setScene(locConfigPane);
            locConfigStage.show();
        });

        return _configBtn;
    }

    private final Button initMappingButton() {
        _mappingBtn.setOnAction(event -> {
            final MappingPane locMappingPane = new MappingPane();
            final Stage locConfigStage = new Stage();
            locConfigStage.setTitle("Mapping");
            locConfigStage.setScene(locMappingPane);
            locConfigStage.show();
        });

        return _mappingBtn;
    }

    private final Pane initLaunchAlgo() {
        final Button locLaunchBtn = new Button("Lancer l'algorithme");

        final TextArea locTextArea = new TextArea();
        locTextArea.setMinHeight(200.0);
        MessageProvider.getInstance().setStringProperty(locTextArea.textProperty());
//        redirectOutputsToTextArea(locTextArea);

        locLaunchBtn.setOnAction((ActionEvent event) -> {
            locLaunchBtn.setDisable(true);
            _configBtn.setDisable(true);
            _mappingBtn.setDisable(true);

            GuiPropertyManager.getInstance().storeAllProperties();
            ConfigManager.getInstance().writeStandardProperties();

            final Service<Void> locRunService = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            locTextArea.setText("");
                            final RunAlgo locRunAlgo = new RunAlgo();
                            try {
                                locRunAlgo.run();
                            } catch (final AParseException parE) {
                                if (parE.getCause() != null) {
                                    parE.getCause().printStackTrace();
                                } else {
                                    parE.printStackTrace();
                                }
                                Platform.runLater(() -> {
                                    final ParsingAlert locAlert = new ParsingAlert(parE);
                                    locAlert.showAndWait();
                                    locLaunchBtn.setDisable(false);
                                    _configBtn.setDisable(false);
                                    _mappingBtn.setDisable(false);
                                });
                            } catch (final Throwable parThrowable) {
                                parThrowable.printStackTrace();
                            } finally {
                                Platform.runLater(() -> {
                                    locLaunchBtn.setDisable(false);
                                    _configBtn.setDisable(false);
                                    _mappingBtn.setDisable(false);
                                });
                            }
                            return null;
                        }
                    };
                }
            };

            locRunService.start();
        });

        return new VBox(15, new BorderPane(locLaunchBtn), locTextArea);
    }

    /**
     * Attention, cela ralentit énormément le programme
     * @param parTextArea
     */
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
