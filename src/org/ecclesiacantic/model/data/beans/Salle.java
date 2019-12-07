package org.ecclesiacantic.model.data.beans;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.archi.itf.INamedObject;
import org.ecclesiacantic.model.data.beans.creneaux.Disponibilite;
import org.ecclesiacantic.model.data.beans.creneaux.EnumCreneau;

public class Salle implements INamedObject, Comparable<Salle> {

    // FIXME : si la valeur change, elle ne sera pas pris en compte tant que l'algo n'est pas redémarré
    static private final int MARGE_NB_PERSONNE = EnumConfigProperty.NB_MARGE_PLACE_SALLE.intV();

    private final String _nomSalle;
    private final int _capacite;
    private final Disponibilite _disponibilite;
    private final String _repere;
    private int _capaciteTotale;

    public Salle(final String parNomSalle, final int parCapacite, final Disponibilite parDisponibilite, final String parRepere) {
        this._nomSalle = parNomSalle;
        this._capacite = parCapacite - MARGE_NB_PERSONNE;
        this._disponibilite = parDisponibilite;
        this._repere = parRepere;
        this._capaciteTotale = -1;
    }

    public final int getCapaciteTotale() {
        if (_capaciteTotale == -1) {
            _capaciteTotale = _capacite * _disponibilite.numberOfDispo();
        }
        return _capaciteTotale;
    }

    public final boolean isDispo(final EnumCreneau parCreneau) {
        return _disponibilite.isDispo(parCreneau);
    }

    @Override
    public String getName() {
        return getNomSalle();
    }

    @Override
    public String toString() {
        return getName();
    }

    public final String getNomSalle() {
        return _nomSalle;
    }

    public final int getCapacite() {
        return _capacite;
    }

    public final String getRepere() {
        return _repere;
    }

    /**
     *
     * @param parOther une autre salle
     * @return > 1 si this.capacite > other.capacite
     */
    @Override
    public int compareTo(final Salle parOther) {
        return Integer.compare(getCapaciteTotale(), parOther.getCapaciteTotale());
    }
}
