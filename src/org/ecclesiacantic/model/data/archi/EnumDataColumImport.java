package org.ecclesiacantic.model.data.archi;

public enum EnumDataColumImport {
    //MasterClass
    MC_NAME("Numéro et titre de la masterclasse"),
    MC_CRENEAU_1("Créneau 1"),
    MC_CRENEAU_2("Créneau 2"),
    MC_CRENEAU_3("Créneau 3"),
    MC_DIVISER_EN("Diviser en"),
    MC_SALLE_IMPOSE("Salle imposée"),

    //Salles
    S_NAME("Salle"),
    S_CAPACITY("Capacité (pl.ass)"),
    S_CRENEAU_1("Créneau 1\n(25/11 14h-15h15)"),
    S_CRENEAU_2("Créneau 2\n(26/11 9h-10h15)"),
    S_CRENEAU_3("Créneau 3\n(26/11 10h30-11h45)"),
    S_REPERE("Escalier/repère lieux badge"),

    //Participant
    P_PRENOM("Prénom"),
    P_NOM("Nom"),
    P_CIVILITE("Civilité - #4"),
    P_NUMERO_BILLET("Billet"),
    P_EMAIL("E-mail"),
    P_CODE_POSTAL("Code postal - #8"),
    P_PAYS("Pays - #16"),
    P_VOEU1("Vœu n°1 - #14802"),
    P_VOEU2("Vœu n°2 - #14805"),
    P_VOEU3("Vœu n°3 - #14808"),
    P_VOEU4("Vœu n°4 - #14811"),
    P_VOEU5("Vœu n°5 - #14814"),
    P_PUPITRE("Pupitre - #14820"),
    P_CHORALE("Chorale - #14823"),
    P_NE_SOUHAITE_PAS_CHANTER("Je ne souhaite pas chanter durant le week-end - #14831"),
    P_GROUPE_EVANGELISATION("Groupe chant missionnaire"),
    P_GROUPE_CONCERT("Groupe concert"),

    // Chorale
    C_NOM("Nom chorale"),
    C_IS_REFERENCE("Chorale référente"),
    C_ID_GROUPE_EVAN("Groupe missionnaire"),

    //Groupes d'évangelisation
    GE_ID("Groupe missionnaire"),
    GE_PAYS("Pays"),
    GE_DEPARTEMENT_OR_ARRONDISSEMENT("Département (CP pour Paris)"),
    GE_LIMITE("Nb limite participants"),
    GE_CHORALE_REF_NAME("Nom chorale"),
    GE_IS_CHORALE_REF("Chorale référente"),

    // Groupe de concert
    GC_GROUPE_EVANGELISATION_ID("id groupe d'evangelisation"),
    GC_GROUPE_CONCERT_ID("groupe de concert")
    ;

    private String _headerName;
    private boolean _active, _changed, _maybeEmpty;

    EnumDataColumImport(final String parDataHeader) {
        _headerName = parDataHeader;
        _active = true;
        _changed = false;
        _maybeEmpty = false;
    }

    static public final EnumDataColumImport valueFromJson(final String parName) {
        try {
            return valueOf(parName);
        } catch (final IllegalArgumentException parE) {
            return null;
        }
    }

    public String getHeaderName() {
        return _headerName;
    }

    public void setHeaderName(final String parHeaderName) {
        _headerName = parHeaderName;
        _changed = true;
    }

    public boolean isActive() {
        return _active;
    }

    public void setActive(final boolean parActive) {
        _active = parActive;
        _changed = true;
    }

    public boolean isChanged() {
        return _changed;
    }

    public boolean isMaybeEmpty() {
        return _maybeEmpty;
    }

    public void setMaybeEmpty(final boolean parMaybeEmpty) {
        _maybeEmpty = parMaybeEmpty;
    }

    @Override
    public String toString() {
        return _headerName;
    }
}
