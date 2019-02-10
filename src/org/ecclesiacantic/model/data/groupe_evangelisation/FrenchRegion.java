package org.ecclesiacantic.model.data.groupe_evangelisation;

import org.ecclesiacantic.model.data.beans.participant.Pays;

public class FrenchRegion extends ARegion {

    public FrenchRegion(final Pays parPays, String parDepartement) {
        super(parPays, parDepartement);
    }

    @Override
    public String getRegionKey() {
        return getDepartement();
    }
}
