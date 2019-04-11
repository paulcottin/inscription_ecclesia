package org.ecclesiacantic.model.data_manager;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.archi.EnumDataType;
import org.ecclesiacantic.model.data.beans.participant.Participant;
import org.ecclesiacantic.model.data.beans.participant.Pays;
import org.ecclesiacantic.model.data.groupe_evangelisation.ARegion;
import org.ecclesiacantic.model.data.groupe_evangelisation.RegionManager;
import org.ecclesiacantic.model.data.groupe_evangelisation.SoloGeographique;
import org.ecclesiacantic.model.data_manager.bean.ChoraleManager;
import org.ecclesiacantic.model.data_manager.bean.ParticipantManager;
import org.ecclesiacantic.model.data_manager.bean.PaysManager;
import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.utils.parser.helper.exception.ObjectInstanciationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SoloGeographiqueManager extends ADataManager<SoloGeographique> {

    static private SoloGeographiqueManager _instance;

    static public final SoloGeographiqueManager getInstance() {
        if (_instance == null) {
            _instance = new SoloGeographiqueManager();
        }
        return _instance;
    }

    public SoloGeographiqueManager() {
        super(EnumConfigProperty.INPUT_F_SOLO_GEO, EnumDataType.SOLO_GEOGRAPHIQUE);
    }

    @Override
    public void reset() {

    }

    @Override
    protected SoloGeographique convertStringMapToObject(Map<EnumDataColumImport, String> parStringMapHeaderValue) throws ObjectInstanciationException {
        final String locGroupeEvangelisationId =
                stringV(parStringMapHeaderValue,EnumDataColumImport.GE_ID).trim().isEmpty() ?
                        ChoraleManager.getInstance().getNullData().getIdGroupeEvangelisation()
                        :
                        stringV(parStringMapHeaderValue,EnumDataColumImport.GE_ID)
                ;
        final Pays locPays = PaysManager.getInstance().get(
                stringV(parStringMapHeaderValue,EnumDataColumImport.GE_PAYS)
        );
        final ARegion locRegion = RegionManager.getInstance().getRegion(
                stringV(parStringMapHeaderValue,EnumDataColumImport.GE_DEPARTEMENT_OR_ARRONDISSEMENT),
                locPays
        );
        return new SoloGeographique(
                locRegion,
                locGroupeEvangelisationId
        );
    }

    public final List<Participant> getSoloParticipantFrom(final List<ARegion> parRegions) {
        final List<Participant> locReturnList = new ArrayList<>();
        for (final ARegion locRegion : parRegions) {
            locReturnList.addAll(getSoloParticipantFrom(locRegion));
        }
        return locReturnList;
    }

    public final List<Participant> getSoloParticipantFrom(final ARegion parRegion) {
        final List<Participant> locReturnList = new ArrayList<>();
        for (final Participant locParticipant : ParticipantManager.getInstance().getAllData()) {
            if (locParticipant.isChoraleAffilie() || !locParticipant.isChante()) {
                continue;
            }

            if (locParticipant.getRegion().equals(parRegion)) {
                locReturnList.add(locParticipant);
            }
        }
        return locReturnList;
    }
}
