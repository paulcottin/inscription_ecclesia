package org.ecclesiacantic.model.data.beans;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.archi.itf.INamedObject;
import org.ecclesiacantic.model.data.beans.creneaux.Disponibilite;
import org.ecclesiacantic.model.data.beans.creneaux.EnumCreneau;

public class Salle implements INamedObject, Comparable<Salle> {

    static private final int MARGE_NB_PERSONNE = EnumConfigProperty.NB_MARGE_PLACE_SALLE.intV();

    private final String _localisation, _nomSalle;
    private final int _capacite, _fullCapacite;
    private final Disponibilite _disponibilite;
    private final String _infosComplementaire, _commentaires, _repere;
    private int _capaciteTotale;

    public Salle(final String parLocalisation, final String parNomSalle,
                 final int parCapacite, final Disponibilite parDisponibilite, final String parRepere) {
        this._localisation = parLocalisation;
        this._nomSalle = parNomSalle;
        this._fullCapacite = parCapacite;
        this._capacite = _fullCapacite - MARGE_NB_PERSONNE;
        this._disponibilite = parDisponibilite;
        this._repere = parRepere;
        this._infosComplementaire = "";
        this._commentaires = "";
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

    public final String getLocalisation() {
        return _localisation;
    }

    public final String getNomSalle() {
        return _nomSalle;
    }

    public final int getCapacite() {
        return _capacite;
    }

    public final Disponibilite getDisponibilite() {
        return _disponibilite;
    }

    public final String getInfosComplementaire() {
        return _infosComplementaire;
    }

    public final String getCommentaires() {
        return _commentaires;
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
