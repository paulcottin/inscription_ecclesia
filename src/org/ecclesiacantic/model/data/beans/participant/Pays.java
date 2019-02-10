package org.ecclesiacantic.model.data.beans.participant;

import org.ecclesiacantic.model.data.archi.itf.INamedObject;

public class Pays implements INamedObject {

    private final String _name;

    public Pays(final String parName) {
        _name = parName;
    }

    public final boolean isFrance() {
        return _name.equals("France");
    }

    @Override
    public final String getName() {
        return _name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
