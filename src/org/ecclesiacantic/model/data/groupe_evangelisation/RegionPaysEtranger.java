package org.ecclesiacantic.model.data.groupe_evangelisation;

import org.ecclesiacantic.model.data.beans.participant.Pays;

public class RegionPaysEtranger extends ARegion {


    public RegionPaysEtranger(final Pays parPays, String parCodePostal) {
        super(parPays, parCodePostal);
    }

    @Override
    public String getRegionKey() {
        return getPays().getName();
    }
}
