package org.ecclesiacantic.model.data.beans.creneaux;

public class DisponibiliteBuilder {

    private final Disponibilite _dispo;

    public DisponibiliteBuilder() {
        _dispo = new Disponibilite();
    }

    final public DisponibiliteBuilder setDisponibilite(final EnumCreneau parCreneau, final boolean parIsDispo) {
        _dispo.setDispo(parCreneau, parIsDispo);
        return this;
    }

    final public Disponibilite build() {
        return _dispo;
    }
}
