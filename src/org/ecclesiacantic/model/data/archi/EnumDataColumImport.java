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
    S_LOCALISATION("Lieux"),
    S_NAME("Salle"),
    S_CAPACITY("Capacité (pl.ass)"),
    S_CRENEAU_1("Créneau 1\n(25/11 14h-15h15)"),
    S_CRENEAU_2("Créneau 2\n(26/11 9h-10h15)"),
    S_CRENEAU_3("Créneau 3\n(26/11 10h30-11h45)"),
    S_REPERE("Escalier/repère lieux badge"),
//    S_INFOS_SUPP("Infos complémentaires"),
//    S_COMMENTAIRES("Commentaires"),

    //Participant
    P_PRENOM("Prénom"),
    P_NOM("Nom"),
    P_CIVILITE("Civilité"),
    P_TARIF("Tarif"),
    P_PRIXPAYE("Prix payé"),
    P_CODEBARRE("Code-barre"),
    P_DATECOMMANDE("Date commande"),
    P_HEURECOMMANDE("Heure commande"),
    P_NUMERO_BILLET("N° Billet"),
    P_EMAIL("e-mail"),
    P_DATE_NAISSANCE("Date de naissance"),
    P_AGE("Age"),
    P_TELEPHONE("Téléphone"),
    P_CODE_POSTAL("Code postal"),
    P_PAYS("Pays"),
    P_AUTRES_INFOS("Autres informations (régime, etc)"),
    P_BESOIN_HEBERGEMENT("Besoin hébergement"),
    P_HEBERGE_AVEC("Hébergé avec"),
    P_PEUT_HERBERGER("Peut héberger"),
    P_STATION_METRO("Station la plus proche"),
    P_PEUT_ACCUEILLIR("Peut accueillir"),
    P_VOEU1("Voeu n°1"),
    P_VOEU2("Voeu n°2"),
    P_VOEU3("Voeu n°3"),
    P_VOEU4("Voeu n°4"),
    P_VOEU5("Voeu n°5"),
    P_PUPITRE("Pupitre"),
    P_CHORALE("Chorale"),
    P_CHORALE_REF("is_reference"),
    P_CHORALE_NN_REF("Chorale non référencée"),
    P_NE_SOUHAITE_PAS_CHANTER("Ne souhaite pas chanter"),
    P_MESSAGE("Message"),
    IS_PARTICIPANT_2016("Est un participant 2016"),
    PAR_PARTICIPANT_2016("Par un participant 2016"),
    TYPE_SERVICE("Paroisse, communauté religieuse, mouvement d'Eglise, pastorale des jeunes"),
    DIOCESE("Diocèse"),
    CHORALE_1("Par ma chorale"),
    MEDIA("Médias"),
    RESEAUX_SOCIAUX("Réseaux sociaux"),
    BOUCHE_OREILLE("Bouche à oreille"),
    AIDE_OFFICES("Aide aux offices (pour les religieux)"),
    DIOCESE_SNPLS("Diocèse (pour les invités type SNPLS)"),
    P_GROUPE_EVANGELISATION("Groupe chant missionnaire"),
    P_GROUPE_CONCERT("Groupe concert"),

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

    final private String _headerName;
    EnumDataColumImport(final String parDataHeader) {
        _headerName = parDataHeader;
    }


    @Override
    public String toString() {
        return _headerName;
    }
}
