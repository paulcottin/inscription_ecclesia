package org.ecclesiacantic.model.data.archi;

public enum EnumDataColumnExport {

    CRENEAU("Créneau"),
    CRENEAU1("Créneau 1"),
    CRENEAU2("Créneau 2"),
    CRENEAU3("Créneau 3"),
    SALLE("Salle"),
    S_CAPACITE("Capacité de la salle"),
    MASTERCLASSE("MasterClasse"),
    MASTERCLASSE_ID("Id de MasterClasse"),
    NB_PARTICIPANTS("Nb participants"),
    NB_PARTICIPANTS_TOTAL("Nb participants total"),
    NB_FIRST_3_VOEUX("Nb de fois dans les 3 1er voeux"),
    NB_ALL_VOEUX("Nb de fois dans tous les voeux"),
    P_PRENOM("Prénom"),
    P_NOM("Nom"),
    P_NUM_BILLET("Numéro de billet"),
    P_EMAIL("Email"),
    P_NUM_BAGAGERIE("Numéro de bagagerie"),
    GE_ID("Groupe ID"),
    GE_DEPT_OR_ARROND("Dept ou arrondissement"),
    GE_CHORALE_REF("Chorale référente"),
    GE_PARTICIPANTS("Membres du groupe"),
    GROUPE_EVANGELISATION_ID("Groupe évangélisation"),
    GROUPE_CONCERT("Groupe de concert"),
    ECART_TYPE("Ecart type"),
    ECART_TYPE_PRECIS("Ecart type précis")
    ;

    final private String _headerName;
    EnumDataColumnExport(final String parDataHeader) {
        _headerName = parDataHeader;
    }


    @Override
    public String toString() {
        return _headerName;
    }
}
