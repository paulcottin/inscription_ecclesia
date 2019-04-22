package org.ecclesiacantic.model.statistic;

public enum EnumStatistics {

    REPARTITION_SEXE("RepartitionFilleGarconStatistic"),

    SATISFACTION_VOEUX("SatisfactionVoeuxStatisic"),

    OCCUPATION_CRENEAUX("OccupationCreneauStatistic"),

    SALLES_BY_MC("NbSalleByMasterclassStatistic", false, true),

    SALLE_SURPOPULTION("SalleSurpopulationStatistic"),

    SUMMARY_GROUPE_EVANGELISATION("RepartitionGroupeEvangelisationStatistic", false, true),

    REPARTITIONS_GROUPE_CONCERT("GroupeConcertParticipationStatistic", false, true),

    NO_CHANTS("NoChantStatistic"),

    PAYS_NON_GERE("PaysNonGereStatistic"),

    BAD_CODE_POSTAL("NoGoodCodePostalStatistic"),

    HIDDEN_PARTICIPANTS("NotFoundParticipantStatistic"),

    SAME_VOEUX_FOR_PARTICIPANT("CheckVoeuxDifferentsParParticipantStatistic", true, true),

    SALLE_OR_MC_NO_DISPO("CheckIncompatibiliteDispoSalleInscritsStatistic"),

    SAME_INSCRIPTION_PARTICIPANT("CheckNoSameMcParticipantStatistic"),

    NB_PERSONNE_PAR_VOIX("NombrePersonneParVoixStatistic")
    ;

    static private final String STAT_CLASS_PACKAGE_NAME = "statistics";

    private final String _classPath;
    private final boolean _isStandaloneStatistic, _toConsole;

    EnumStatistics(final String parClassName) {
        this(parClassName, false, false);
    }

    /**
     * Permet de d'instancier et appeler automatiquement une classe de statistique
     * à la fin de la répartition
     * @param parClassName : Le nom de la classe, elle doit se trouver dans le package statistics
     * @param parIsStandaloneStatistic : si vrai alors cette stat n'est pas appelé avec toutes les autres
     *                                 à la fin de la répartition mais prévue pour être appelée à part.
     */
    EnumStatistics(final String parClassName, final boolean parIsStandaloneStatistic, final boolean parToConsole) {
        _classPath = String.format("%s.%s.%s",
                getClass().getPackage().getName(),
                STAT_CLASS_PACKAGE_NAME,
                parClassName);
        _isStandaloneStatistic = parIsStandaloneStatistic;
        _toConsole = parToConsole;
    }

    public final String getClasspath() {
        return _classPath;
    }

    public final boolean isStandaloneStatistic() {
        return _isStandaloneStatistic;
    }

    public final boolean isToConsole() {
        return _toConsole;
    }
}
