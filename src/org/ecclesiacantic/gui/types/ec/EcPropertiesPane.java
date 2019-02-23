package org.ecclesiacantic.gui.types.ec;

import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.gui.properties.BooleanFieldProperty;
import org.ecclesiacantic.gui.properties.IntFieldProperty;
import org.ecclesiacantic.gui.properties.TextFieldProperty;

public class EcPropertiesPane extends TitledPane {

    public EcPropertiesPane() {
        super();
        setText("Properiété propres à l'édition d'Ecclesia cantique");
        setExpanded(false);
        setContent(new VBox(10,
                new IntFieldProperty("Nombre de places réservées pour les volontaires par salle", EnumConfigProperty.NB_MARGE_PLACE_SALLE).toHbox(),
                new IntFieldProperty("Nombre de voeux total par participants", EnumConfigProperty.NB_MAX_VOEUX).toHbox(),
                new IntFieldProperty("Nombre de voeux à prendre en compte", EnumConfigProperty.NB_VOEUX_CONSIDERED).toHbox(),
                new TextFieldProperty("Marqueur de vrai dans Excel (X, oui, ...)", EnumConfigProperty.BOOLEAN_MARK).toHbox(),
                new BooleanFieldProperty("Faut-il avantager les personnes par date d'inscription ?", EnumConfigProperty.IS_USE_SORTED_PARTICIPANTS, "Oui", "Non").toHbox()
        ));
    }
}