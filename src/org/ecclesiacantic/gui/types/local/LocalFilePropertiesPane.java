package org.ecclesiacantic.gui.types.local;

import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.gui.properties.FileFieldProperty;
import org.ecclesiacantic.gui.properties.TextFieldProperty;

public class LocalFilePropertiesPane extends TitledPane {

    public LocalFilePropertiesPane() {
        super();
        setText("Fichiers locaux");
        setExpanded(false);
        setContent(new VBox(10,
                initLocalSourceFilesPane(),
                initLocalExportFilesPane()
        ));
    }

    private final TitledPane initLocalSourceFilesPane() {
        final TitledPane locTitledPane = new TitledPane();
        locTitledPane.setText("Fichiers sources locaux");

        locTitledPane.setContent(new VBox(10,
                new FileFieldProperty("Fichier d'import des participants", EnumConfigProperty.INPUT_F_PARTICIPANT).toHbox(),
                new FileFieldProperty("Fichier d'import des master classes", EnumConfigProperty.INPUT_F_MC).toHbox(),
                new FileFieldProperty("Fichier d'import des salles", EnumConfigProperty.INPUT_F_SALLE).toHbox(),
                new FileFieldProperty("Fichier d'import des chorales", EnumConfigProperty.INPUT_F_CHORALE).toHbox(),
                new FileFieldProperty("Fichier d'import des solos géographiques", EnumConfigProperty.INPUT_F_SOLO_GEO).toHbox(),
                new FileFieldProperty("Fichier d'import des groupes de concert", EnumConfigProperty.INPUT_F_GROUP_CONCERT).toHbox()
                )
        );

        return locTitledPane;
    }

    private final TitledPane initLocalExportFilesPane() {
        final TitledPane locTitledPane = new TitledPane();
        locTitledPane.setText("Fichiers d'export des résultats");

        locTitledPane.setContent(new VBox(10,
                        new TextFieldProperty("Fichier d'export des salles par MC", EnumConfigProperty.OUTPUT_F_SALLE).toHbox(),
                        new TextFieldProperty("Fichier d'export des badges", EnumConfigProperty.OUTPUT_F_BADGE).toHbox(),
                        new TextFieldProperty("Fichier d'export des groupes d'évangélisation", EnumConfigProperty.OUTPUT_F_G_EVAN).toHbox(),
                        new TextFieldProperty("Fichier d'export des groupes de concert", EnumConfigProperty.OUTPUT_F_G_CONCERT).toHbox(),
                        new TextFieldProperty("Fichier d'export des mailing list", EnumConfigProperty.OUTPUT_F_MAILING_L).toHbox(),
                        new TextFieldProperty("Fichier d'export des nb de participants par MC", EnumConfigProperty.OUTPUT_F_NB_PART_BY_CRENEAU).toHbox(),
                        new TextFieldProperty("Fichier d'export des participants par groupe d'évangélisation", EnumConfigProperty.OUTPUT_F_G_EVAN_PART_RELATION).toHbox(),
                        new TextFieldProperty("Fichier d'export des feuilles d'appel de MC", EnumConfigProperty.OUTPUT_D_MC_CHECKLIST).toHbox()
                )
        );

        return locTitledPane;
    }
}
