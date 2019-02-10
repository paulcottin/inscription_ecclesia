package org.ecclesiacantic.utils;

import org.ecclesiacantic.model.data.beans.creneaux.Disponibilite;
import org.ecclesiacantic.model.data.beans.creneaux.DisponibiliteBuilder;
import org.ecclesiacantic.model.data.beans.creneaux.EnumCreneau;
import org.ecclesiacantic.model.data.archi.EnumDataColumImport;

import java.util.Map;

public class DispoUtils {

    /**
     * Pour toutes les entrées d'une map avec header des fichiers CSV, crée une disponibilite
     *
     * Les colonnes de créneaux doivent avoir été reliées aux colonnes du CSV dans EnumCreneau
     *
     * @param parMap : map provenant d'un fichier csv de données
     * @return : Une disponibilité.
     */
    static final public Disponibilite getDisponibiliteFromStringMap(final Map<EnumDataColumImport, String> parMap) {
        final DisponibiliteBuilder locDisponibiliteBuilder = new DisponibiliteBuilder();
        for (final EnumCreneau locCreneau : EnumCreneau.values()) {
            for (final EnumDataColumImport locDataColum : parMap.keySet()) {
                if (locCreneau.match(locDataColum)) {
                    locDisponibiliteBuilder.setDisponibilite(locCreneau, parMap.get(locDataColum).equals("1"));
                }
            }
        }
        return locDisponibiliteBuilder.build();
    }
}
