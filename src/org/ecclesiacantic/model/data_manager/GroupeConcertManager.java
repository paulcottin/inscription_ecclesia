package org.ecclesiacantic.model.data_manager;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.GroupeConcert;
import org.ecclesiacantic.model.data.archi.EnumDataType;
import org.ecclesiacantic.model.data.beans.participant.Participant;
import org.ecclesiacantic.model.data.groupe_evangelisation.GroupeEvangelisation;
import org.ecclesiacantic.model.data_manager.bean.ParticipantManager;
import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.utils.parser.NumberUtils;

import java.util.Map;

public class GroupeConcertManager extends ADataManager<GroupeConcert> {

    private static final int MAX_GROUPE_NUM_BAGAGERIE = 500;

    static private GroupeConcertManager _instance;

    static public final GroupeConcertManager getInstance() {
        if (_instance == null) {
            _instance = new GroupeConcertManager();
        }
        return _instance;
    }

    public GroupeConcertManager() {
        super(EnumConfigProperty.IS_COMPUTE_GE_REPART.boolV() ?
            EnumConfigProperty.INPUT_F_GROUP_CONCERT: EnumConfigProperty.INPUT_F_PARTICIPANT,
                EnumDataType.GROUPE_CONCERT, EnumConfigProperty.IS_COMPUTE_GE_REPART.boolV());
    }

    public final GroupeConcert getGroupeConcertOf(final String parGroupeEvangelisationId) {
        for (final GroupeConcert locGroupeConcert : getAllData()) {
            for (final GroupeEvangelisation locGroupeEvangelisation : locGroupeConcert.getGroupesEvangelisation()) {
                if (locGroupeEvangelisation.getId().equals(parGroupeEvangelisationId)) {
                    return locGroupeConcert;
                }
            }
        }
        return null;
    }

    public final void computeNumBagagerie() {
        int locTmpGroupeFisrtNumBagagerie = 0;
        for (final GroupeConcert locGroupe : _dataMap.values()) {
            locGroupe.computeNumBagagerie(locTmpGroupeFisrtNumBagagerie);
            locTmpGroupeFisrtNumBagagerie += MAX_GROUPE_NUM_BAGAGERIE;
        }

        int locTmpNumBagagerie = locTmpGroupeFisrtNumBagagerie + MAX_GROUPE_NUM_BAGAGERIE;
        for (final Participant locPart : ParticipantManager.getInstance().getAllData()) {
            if (!locPart.isChante()) {
                locPart.setNumBagagerie(locTmpNumBagagerie);
                locTmpNumBagagerie++;
            }
        }
    }

    @Override
    public void reset() {

    }

    @Override
    protected GroupeConcert convertStringMapToObject(final Map<EnumDataColumImport, String> parStringMapHeaderValue) {
        if (EnumConfigProperty.IS_COMPUTE_GE_REPART.boolV()) {
            return convertStringMapToObjectInComputingMode(parStringMapHeaderValue);
        } else {
            return convertStringMapToObjectInReadingMode(parStringMapHeaderValue);
        }
    }

    private final GroupeConcert convertStringMapToObjectInReadingMode(final Map<EnumDataColumImport, String> parStringMapHeaderValue) {
        final GroupeConcert locGroupe;
        if (parStringMapHeaderValue.get(EnumDataColumImport.P_GROUPE_CONCERT).isEmpty()) {
            locGroupe = new GroupeConcert(-1);
        } else {
            locGroupe = new GroupeConcert(NumberUtils.convertFieldToInt(parStringMapHeaderValue.get(EnumDataColumImport.P_GROUPE_CONCERT)));
        }

        if (!EnumConfigProperty.IS_COMPUTE_GE_REPART.boolV()) {
            for (final Participant locPart : ParticipantManager.getInstance().getAllData()) {
                if (!locPart.isChante()) {
                    continue;
                }

                locGroupe.addGroupeEvangelisation(GroupeEvangelisationManager.getInstance().get(locPart.getGroupeEvangelisationId()));
            }
        }
        return locGroupe;
    }

    private final GroupeConcert convertStringMapToObjectInComputingMode(final Map<EnumDataColumImport, String> parStringMapHeaderValue) {
        final String locGroupeConcertId = parStringMapHeaderValue.get(
                EnumDataColumImport.GC_GROUPE_CONCERT_ID
        );
        final String locGroupeEvangelisationId = parStringMapHeaderValue.get(
                EnumDataColumImport.GC_GROUPE_EVANGELISATION_ID
        );

        final GroupeConcert locGroupeConcert;
        if (!exists(locGroupeConcertId)) {
            locGroupeConcert = new GroupeConcert(
                    NumberUtils.convertFieldToInt(locGroupeConcertId)
            );
        } else {
            locGroupeConcert = GroupeConcertManager.getInstance().get(locGroupeConcertId);
        }

        locGroupeConcert.addGroupeEvangelisation(GroupeEvangelisationManager.getInstance().get(locGroupeEvangelisationId));
        return locGroupeConcert;
    }
}
