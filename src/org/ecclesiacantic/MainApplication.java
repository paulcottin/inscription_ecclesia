package org.ecclesiacantic;

import javafx.application.Application;
import javafx.stage.Stage;
import org.ecclesiacantic.gui.MainGui;

public class MainApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage parPrimaryStage) {
        final MainGui locGui = new MainGui(parPrimaryStage);
        locGui.show();
    }
}
