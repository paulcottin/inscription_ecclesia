package org.ecclesiacantic.model.data.archi;

import java.util.ArrayList;
import java.util.List;

public enum EnumDataColumnExportList {

    PARTICIPANT_MC_SALLE(
            EnumDataColumnExport.CRENEAU,
            EnumDataColumnExport.SALLE,
            EnumDataColumnExport.S_CAPACITE,
            EnumDataColumnExport.MASTERCLASSE,
            EnumDataColumnExport.NB_PARTICIPANTS
    ),

    CRENEAU_EVT(
            EnumDataColumnExport.MASTERCLASSE_ID
    ),

    BADGES(
            EnumDataColumnExport.P_PRENOM,
            EnumDataColumnExport.P_NOM,
            EnumDataColumnExport.P_NUM_BILLET,
            EnumDataColumnExport.P_NUM_BAGAGERIE,
            EnumDataColumnExport.CRENEAU1,
            EnumDataColumnExport.CRENEAU2,
            EnumDataColumnExport.CRENEAU3,
            EnumDataColumnExport.GROUPE_EVANGELISATION_ID,
            EnumDataColumnExport.GROUPE_CONCERT

    ),

    GROUPE_EVANGELISATION(
            EnumDataColumnExport.GE_ID,
            EnumDataColumnExport.GE_DEPT_OR_ARROND,
            EnumDataColumnExport.GE_CHORALE_REF,
            EnumDataColumnExport.GE_PARTICIPANTS
    ),

    GROUPE_EVANGELISATION_PARTICIPANT_RELATION(
            EnumDataColumnExport.GE_ID,
            EnumDataColumnExport.P_PRENOM,
            EnumDataColumnExport.P_NOM,
            EnumDataColumnExport.P_NUM_BILLET
    ),

    MC_SALLES_CRENEAU_NB_PARTICIPANTS(
            EnumDataColumnExport.MASTERCLASSE,
            EnumDataColumnExport.SALLE,
            EnumDataColumnExport.S_CAPACITE,
            EnumDataColumnExport.CRENEAU1,
            EnumDataColumnExport.CRENEAU2,
            EnumDataColumnExport.CRENEAU3,
            EnumDataColumnExport.NB_PARTICIPANTS_TOTAL,
            EnumDataColumnExport.NB_FIRST_3_VOEUX,
            EnumDataColumnExport.NB_ALL_VOEUX,
            EnumDataColumnExport.ECART_TYPE,
            EnumDataColumnExport.ECART_TYPE_PRECIS
    ),

    PARTICIPANT_NE_FAIT_RIEN_PENDANT_CRENEAU (
            EnumDataColumnExport.P_NUM_BILLET,
            EnumDataColumnExport.P_EMAIL,
            EnumDataColumnExport.CRENEAU,
            EnumDataColumnExport.MASTERCLASSE,
            EnumDataColumnExport.MASTERCLASSE,
            EnumDataColumnExport.MASTERCLASSE
    )
    ;

    private final List<EnumDataColumnExport> _exportList;

    EnumDataColumnExportList(final EnumDataColumnExport... parExportList) {
        _exportList = new ArrayList<>(parExportList.length);
        for (final EnumDataColumnExport locColumn : parExportList) {
            _exportList.add(locColumn);
        }
    }

    public final List<EnumDataColumnExport> asList() {
        return _exportList;
    }

    public final List<String> asListString() {
        final List<String> locReturnList = new ArrayList<>(_exportList.size());
        for (final EnumDataColumnExport locColumn : _exportList) {
            locReturnList.add(locColumn.toString());
        }
        return locReturnList;
    }
}
