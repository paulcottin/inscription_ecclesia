package org.ecclesiacantic.model.data.beans.participant;

import org.ecclesiacantic.model.data.archi.itf.INamedObject;

public class Tarif implements INamedObject {

    final String _tarifName;
    final double _prix;

    public Tarif(final String parTarifName, final double parPrix) {
        this._tarifName = parTarifName;
        this._prix = parPrix;
    }

    @Override
    public String getName() {
        return _tarifName;
    }

    @Override
    public String toString() {
        return getName();
    }
}
