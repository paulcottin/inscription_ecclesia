package org.ecclesiacantic.model.data.beans.creneaux;

import java.util.HashMap;
import java.util.Map;

public class Disponibilite {

    final private Map<EnumCreneau, Boolean> _dispo;

    public Disponibilite() {
        _dispo = new HashMap<>();
    }

    final public boolean isDispo(final EnumCreneau parCreneau) {
        return _dispo.get(parCreneau);
    }

    final public void setDispo(final EnumCreneau parCreneau, final boolean parIsDispo) {
        _dispo.put(parCreneau, parIsDispo);
    }

    public final int numberOfDispo() {
        int locNumberOfDispo = 0;
        for (final EnumCreneau locCreneau : EnumCreneau.values()) {
            if (isDispo(locCreneau)) {
                locNumberOfDispo++;
            }
        }
        return locNumberOfDispo;
    }

    public final Map<EnumCreneau, Boolean> getDisponibilite() {
        return _dispo;
    }
}
