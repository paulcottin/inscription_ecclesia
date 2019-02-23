package org.ecclesiacantic.gui;

import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.gui.properties.BooleanFieldProperty;
import org.ecclesiacantic.gui.properties.DoubleFieldProperty;
import org.ecclesiacantic.gui.properties.IntFieldProperty;

public class ConfigAlgoPane extends BorderPane{

    public ConfigAlgoPane() {
        super();
        setCenter(new VBox(10,
                initGeneralProperties(),
                initTypeAlgo(),
                initAlgoRanges()
        ));

    }

    private final TitledPane initGeneralProperties() {
        final TitledPane locPane = new TitledPane();
        locPane.setText("Propriétés générales");

        locPane.setContent(new VBox(5,
                new IntFieldProperty("Nb salles beaucoup plus grandes que les autres", EnumConfigProperty.HUGE_SALLE_NB).toHbox(),
                new IntFieldProperty("Nb de MC très peu populaires", EnumConfigProperty.LESS_POP_MC_NB).toHbox()
        ));
        return locPane;
    }

    private final TitledPane initTypeAlgo() {
        final TitledPane locTitledPane = new TitledPane();
        locTitledPane.setText("Type d'exécution");
        locTitledPane.setContent(new VBox(10,
                new IntFieldProperty("Nb min de participants à une MC pour considérer qu'elle est remplie", EnumConfigProperty.MIN_PART_NB_MC).toHbox(),
                new BooleanFieldProperty("Prendre en compte les participants qui ont des voeux mal remplis ?", EnumConfigProperty.IS_SKIP_MALFORMED_VOEUX, "Oui", "Non").toHbox(),
                new BooleanFieldProperty("Calculer la répartition des groupes d'évangélisations ?", EnumConfigProperty.IS_COMPUTE_GE_REPART, "Oui", "Non").toHbox()
        ));
        return locTitledPane;
    }

    private final TitledPane initAlgoRanges() {
        final TitledPane locTitledPane = new TitledPane();
        locTitledPane.setText("Bornes pour faire tourner l'algorithme");
        locTitledPane.setContent(new VBox(10,
                new Label("Ces propriétés permettent de faire boucler l'algorithme pour trouver ensuite la meilleure répartition"),
                new IntFieldProperty("Nb min de participants à une MC pour qu'on considère qu'elle soit peu populaire", EnumConfigProperty.LESS_USED_PART).toHbox(),
                new IntFieldProperty("Nb max de participants à une MC pour qu'on considère qu'elle soit peu populaire", EnumConfigProperty.MAX_USED_PART).toHbox(),
                new DoubleFieldProperty("Taux min de remplissage d'une MC au premier passage avant de considérer qu'elle est pleine", EnumConfigProperty.MIN_FULL_FACTOR).toHbox(),
                new DoubleFieldProperty("Taux max de remplissage d'une MC au premier passage avant de considérer qu'elle est pleine", EnumConfigProperty.MAX_FULL_FACTOR).toHbox()
                ));
        return locTitledPane;
    }
}
