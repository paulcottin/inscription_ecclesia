package org.ecclesiacantic.model.data.groupe_evangelisation;

import org.ecclesiacantic.model.data.beans.participant.Pays;

/**
 * Permet de définir une région = un département / arrondissement ou pays étranger
 */
public abstract class ARegion {

    private final Pays _pays;
    private final String _departement;

    public ARegion(final Pays parPays, final String parDepartement) {
        this._pays = parPays;
        this._departement = parDepartement;
    }

    public abstract String getRegionKey();

    public final Pays getPays() {
        return _pays;
    }

    public final String getDepartement() {
        return _departement;
    }

    @Override
    public boolean equals(final Object parObj) {
        if (parObj instanceof ARegion) {
            final ARegion locOther = (ARegion) parObj;
            return locOther.getRegionKey().equals(getRegionKey());
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return getRegionKey();
    }
}
