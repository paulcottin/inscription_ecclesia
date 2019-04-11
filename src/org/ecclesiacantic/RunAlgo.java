package org.ecclesiacantic;

import org.ecclesiacantic.config.ConfigManager;
import org.ecclesiacantic.model.data.GroupeConcert;
import org.ecclesiacantic.model.data.beans.Evenement;
import org.ecclesiacantic.model.data.groupe_evangelisation.GroupeEvangelisation;
import org.ecclesiacantic.model.data_manager.*;
import org.ecclesiacantic.model.data_manager.bean.*;
import org.ecclesiacantic.model.repartition.Repartition;
import org.ecclesiacantic.model.repartition.RepartitionManager;
import org.ecclesiacantic.model.statistic.EnumStatistics;
import org.ecclesiacantic.model.statistic.StatisticManager;
import org.ecclesiacantic.utils.parser.helper.exception.AParseException;

public class RunAlgo {

    public RunAlgo() {
    }

    public final void run() throws AParseException {

        //initialisation des propriétés
        ConfigManager.getInstance();

        //Lecture des différentes données avant celles des utilisateurs.
        SalleManager.getInstance().parseDataFile();
        MasterClassManager.getInstance().parseDataFile();
        ChoraleManager.getInstance().parseDataFile();
        TarifManager.getInstance().parseDataFile();
        PaysManager.getInstance().parseDataFile();
        SoloGeographiqueManager.getInstance().parseDataFile();

        ParticipantManager.getInstance().parseDataFile();
        //Fin du chargement des données depuis les fichiers.

        StatisticManager.getInstance().callStandaloneStatistic(EnumStatistics.SAME_VOEUX_FOR_PARTICIPANT);

        // Lancement de l'algo de répartition :
        RepartitionManager.getInstance().runRepartitions();

        EvenementManager.getInstance().exportDataToCSV();

        RepartitionManager.getInstance().exportBadgesData();
        GroupeEvangelisationManager.getInstance().exportDataToCSV();
        BadgeManager.getInstance().exportDataToCSV();

        StatisticManager.getInstance().computeStatistics();
        StatisticManager.getInstance().printResults();

        GroupeEvangelisationManager.getInstance().printMailingList();
        GroupeEvangelisationManager.getInstance().exportParticipantGroupeERelation();

        EvenementManager.getInstance().exportMasterclassData();
        EvenementManager.getInstance().exportSallePopulation();

        RepartitionManager.getInstance().printResult();

        System.out.println("fin");
    }
}
