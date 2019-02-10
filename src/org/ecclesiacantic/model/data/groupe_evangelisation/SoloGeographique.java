package org.ecclesiacantic.model.data.groupe_evangelisation;

import org.ecclesiacantic.model.data.archi.itf.INamedObject;

public class SoloGeographique implements INamedObject {

    private final ARegion _region;
    private final String _idGroupeEvangelisation;

    public SoloGeographique(final ARegion parRegion, final String parIdGroupeEvangelisation) {
        this._region = parRegion;
        this._idGroupeEvangelisation = parIdGroupeEvangelisation;
    }

    @Override
    public String getName() {
        return _region.getRegionKey();
    }

    public final ARegion getRegion() {
        return _region;
    }

    public final String getIdGroupeEvangelisation() {
        return _idGroupeEvangelisation;
    }
}
