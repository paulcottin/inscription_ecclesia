package org.ecclesiacantic;

import javafx.application.Application;
import javafx.stage.Stage;
import org.ecclesiacantic.config.ConfigManager;
import org.ecclesiacantic.gui.MainGui;
import org.ecclesiacantic.model.data_manager.GroupeConcertManager;
import org.ecclesiacantic.model.data_manager.SoloGeographiqueManager;
import org.ecclesiacantic.model.data_manager.bean.*;

public class MainApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage parPrimaryStage) {
        
        // inscription des managers de données
        SalleManager.getInstance();
        MasterClassManager.getInstance();
        TarifManager.getInstance();
        ChoraleManager.getInstance();
        PaysManager.getInstance();
        SoloGeographiqueManager.getInstance();
        GroupeConcertManager.getInstance();

        ParticipantManager.getInstance();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> ConfigManager.getInstance().writeStandardProperties()));
        
        final MainGui locGui = new MainGui(parPrimaryStage);
        locGui.show();
    }
}
