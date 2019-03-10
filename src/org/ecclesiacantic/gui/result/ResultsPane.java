package org.ecclesiacantic.gui.result;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.ecclesiacantic.model.repartition.RepartitionManager;
import org.ecclesiacantic.model.repartition.RepartitionResult;

import java.util.List;

public class ResultsPane extends Scene {

    public ResultsPane(final List<RepartitionResult> parResults) {
        super(new BorderPane(), 600, 300);
        final ObservableList<ResultValue> locResultList = FXCollections.observableArrayList(new ResultValueBuilder(parResults).build());

        final Label locLabel = new Label("Quel résultat voulez-vous sauvegarder ?");

        final TableView<ResultValue> locTableView = new TableView<>();
        locTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        locTableView.setEditable(false);
        locTableView.setItems(locResultList);
        locTableView.getSelectionModel().clearSelection();

        final TableColumn<ResultValue, Integer> locLessUsedNb = new TableColumn<>("less used");
        locLessUsedNb.setCellValueFactory(new PropertyValueFactory<>("lessUsedNb"));
        final TableColumn<ResultValue, Integer> locNoOccupiedPart = new TableColumn<>("Nb participant non occupés à tous les créneaux");
        locNoOccupiedPart.setCellValueFactory(new PropertyValueFactory<>("noOccupiedPart"));
        final TableColumn<ResultValue, Double> locFullFactor = new TableColumn<>("Taux de remplissage premier passage");
        locFullFactor.setCellValueFactory(new PropertyValueFactory<>("fullFactor"));
        final TableColumn<ResultValue, Double> locEcartType = new TableColumn<>("Ecart type");
        locEcartType.setCellValueFactory(new PropertyValueFactory<>("sommeEcartType"));

        locTableView.getColumns().addAll(locLessUsedNb, locNoOccupiedPart, locFullFactor, locEcartType);

        final Button locValidButton = new Button("Sauvegarder");

        locValidButton.setOnAction(event -> {
            RepartitionManager.getInstance().saveResults(parResults.get(locTableView.getSelectionModel().getSelectedIndex()));
            ((Stage) getWindow()).close();
        });
        locValidButton.setDisable(true);

        locTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            locValidButton.setDisable(newValue == null);
        });

        ((BorderPane) getRoot()).setCenter(new VBox(20, locLabel, locTableView, new BorderPane(locValidButton)));
    }

    public final void show() {
        final Stage locStage = new Stage();
        locStage.setTitle("Sauvegarde des résultats");
        locStage.setScene(this);
        locStage.show();
    }
}
