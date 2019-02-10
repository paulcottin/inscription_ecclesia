package org.ecclesiacantic.model.data_manager;

import javafx.util.Pair;
import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.beans.Evenement;
import org.ecclesiacantic.model.data.beans.MasterClass;
import org.ecclesiacantic.model.data.beans.Salle;
import org.ecclesiacantic.model.data.beans.creneaux.EnumCreneau;
import org.ecclesiacantic.model.data.beans.participant.Participant;
import org.ecclesiacantic.model.data_manager.bean.MasterClassManager;
import org.ecclesiacantic.model.data_manager.bean.ParticipantManager;
import org.ecclesiacantic.model.data.archi.EnumDataColumnExportList;
import org.ecclesiacantic.utils.parser.FileUtils;

import java.io.File;
import java.util.*;

public class EvenementManager implements IExportableManager{

    static private EvenementManager _instance;
    static public final EvenementManager getInstance() {
        if (_instance == null) {
            _instance = new EvenementManager();
        }
        return _instance;
    }

    private final List<Evenement> _evenements;
    private int _cptRepartition;
    private final Map<EnumCreneau, Integer> _creneauUsage;
    private final Map<Pair<EnumCreneau, MasterClass>, Integer> _creneauMCUsage;
    private final Map<MasterClass, Integer> _mcTotalSallesCapacity;
    private double _sommeEcartType;

    private EvenementManager() {
        _evenements = new ArrayList<>();
        initScheduledEvenements();
        _creneauUsage = new HashMap<>(3);
        _creneauMCUsage = new HashMap<>();
        _mcTotalSallesCapacity = new HashMap<>();
        reset();
    }

    /**
     * En fonction du planning, on prévoit ici les evénements (MC + creneaux)
     * La répartition des salles et des participants se fera ailleur.
     */
    private final void initScheduledEvenements() {
        //On initialise par défaut un événement MC par creneau
        for (final EnumCreneau locCreneau : EnumCreneau.values()) {
            for (final MasterClass locAvailableMc : MasterClassManager.getInstance().getAllData()) {
                if (locAvailableMc.isDispo(locCreneau)) {
                    _evenements.add(new Evenement(locCreneau, locAvailableMc));
                }
            }
        }
    }

    public final void reset() {
        _cptRepartition = 0;
        _creneauUsage.clear();
        _creneauMCUsage.clear();
        _cptRepartition=0;


        for (final EnumCreneau locCreneau : EnumCreneau.values()) {
            _creneauUsage.put(locCreneau, 0);
        }

        for (final MasterClass locMc : MasterClassManager.getInstance().getAllData()) {
            for (final Map.Entry<EnumCreneau, Boolean> locDisponibiliteEntry : locMc.getDisponibilite().getDisponibilite().entrySet()) {
                if (locDisponibiliteEntry.getValue() && locMc.isDispo(locDisponibiliteEntry.getKey())) {
                    _creneauMCUsage.put(new Pair<>(locDisponibiliteEntry.getKey(), locMc), 0);
                }
            }
        }
        _mcTotalSallesCapacity.clear();

        for (final Evenement locEvt : _evenements) {
            locEvt.getParticipants().clear();
            locEvt.clearParticipantRepartitionInSalles();
        }
    }

    public final Evenement get(final MasterClass parMasterClass, final EnumCreneau parCreneau) {
        return get(parMasterClass, parCreneau, true);
    }

    private final Evenement get(final MasterClass parMasterClass, final EnumCreneau parCreneau, final boolean parPrintError) {
        for (final Evenement locEvenement : _evenements) {
            if (locEvenement.getCreneau().equals(parCreneau) &&
                    locEvenement.getMasterclass().equals(parMasterClass)) {
                return locEvenement;
            }
        }
        if (parPrintError) {
            System.err.println(String.format("Impossible de trouver un événement avec la MC %s et le creneau %s",
                    parMasterClass, parCreneau));
        }
        return null;
    }

    public final List<Evenement> getEvenementsFor(final MasterClass parMasterclass) {
        final List<Evenement> locReturnList = new ArrayList<>();
        for (final Evenement locEvt : _evenements) {
            if (locEvt.getMasterclass().equals(parMasterclass)) {
                locReturnList.add(locEvt);
            }
        }
        return locReturnList;
    }

    /**
     * Permet de récupérer pour un participant donné, le prochain créneau de disponible pour son choix
     * ou le prochain événement correspondant à son choix + 1 si il est impossible de trouver un créneau
     * suivant satisfaisant
     * @param parParticipant
     * @param parMasterclass
     * @param parCurrentPartPriority
     * @return l'événement trouvé ou null si le participant a déjà tout ses créneaux de pris
     */

    public final Evenement getAvailableEvtOrNextPartChoice(final Participant parParticipant,
                                                           final MasterClass parMasterclass,
                                                           final int parCurrentPartPriority,
                                                           final boolean parIsForceAttribution) {
        return getAvailableEvtOrNextPartChoice(parParticipant, parMasterclass, parCurrentPartPriority, getLessUsedCreneau(parMasterclass), parIsForceAttribution);
    }

    public final Evenement getAvailableEvtOrNextPartChoice(final Participant parParticipant,
                                                           final MasterClass parMasterclass,
                                                           final int parCurrentPartPriority) {
        return getAvailableEvtOrNextPartChoice(parParticipant, parMasterclass, parCurrentPartPriority, getLessUsedCreneau(parMasterclass), false);
    }

    public final Evenement getAvailableEvtOrNextPartChoice(final Participant parParticipant,
                                                           final MasterClass parMasterclass,
                                                           final int parCurrentPartPriority,
                                                           final EnumCreneau parCreneau,
                                                           final boolean parIsForcedAttribution) {
        // Si il existe une même MC ultérieurement de dispo alors on affecte le participant à celle-ci
        final TreeMap<Integer, Evenement> locNextAvailablesEvts = getNextAvailablesMcs(parMasterclass, parCreneau);
        for (final Evenement locNextAvailableEvt : locNextAvailablesEvts.values()) {
            if (parParticipant.isEvenementCompatible(locNextAvailableEvt, false)) {
                _cptRepartition++;
                return locNextAvailableEvt;
            }
        }

        // Sinon on prend le choix de priorité suivant pour le participant suivant ses disponibilités
        final EnumCreneau locNextAvailableCreneauForPart = parParticipant.nextAvailableCreneau(parCreneau,
                _cptRepartition % 2 == 0);
        if (locNextAvailableCreneauForPart != null) {
            Evenement locReturnedEvt= null;
            try {
                locReturnedEvt = get(
                        parParticipant.getVoeu(parCurrentPartPriority),
                        locNextAvailableCreneauForPart,
                        false
                );
            } catch (final IndexOutOfBoundsException parE) {
                System.err.println(parE.getMessage());
                return null;
            }

            if (locReturnedEvt != null) {
                _cptRepartition++;
                return locReturnedEvt;
            } else {
                // Si on ne trouve pas alors on va chercher encore plus loin
                // dans les voeux du participant

                if (parIsForcedAttribution) {
                    return null;
                }

                return getAvailableEvtOrNextPartChoice(
                        parParticipant,
                        parMasterclass,
                        parCurrentPartPriority + 1
                );
            }
        } else {
            //Le participant a tous ses créneaux de pris
            return null;
        }
    }

    /**
     *
     * @param parMasterClass la masterclasse recherchée
     * @param parCreneau : Le créneau actuel
     * @return Une liste d'evt avec la MC à un créneau ultérieur, vide sinon
     */
    public final TreeMap<Integer, Evenement> getNextAvailablesMcs(final MasterClass parMasterClass, final EnumCreneau parCreneau) {
        final TreeMap<Integer, Evenement> locPriorityReturnSet = new TreeMap<>();
        EnumCreneau locNextCreneau = parCreneau;
        int locIndex = 0;
        while (locIndex < EnumCreneau.getNumberOfCreneaux()) {
            final Evenement locNextEvenement = get(parMasterClass, locNextCreneau, false);
            if (locNextEvenement != null && locNextEvenement.isSalleDispo(locNextCreneau) && parMasterClass.isDispo(locNextCreneau)) {
                locPriorityReturnSet.put(locNextEvenement.getParticipantsNumber(), locNextEvenement);
            }
            locNextCreneau = locNextCreneau.next();
            locIndex++;
        }
        return locPriorityReturnSet;
    }

    public final void addCreneauUsage(final EnumCreneau parCreneau, final MasterClass parMasterclass) {
        int locCurrentUsage = _creneauUsage.get(parCreneau);
        _creneauUsage.put(parCreneau, ++locCurrentUsage);
        locCurrentUsage = _creneauMCUsage.get(retrievePairFromValues(parCreneau, parMasterclass));
        _creneauMCUsage.put(retrievePairFromValues(parCreneau, parMasterclass), ++locCurrentUsage);
    }

    private final Pair<EnumCreneau, MasterClass> retrievePairFromValues(final EnumCreneau parCreneau, final MasterClass parMasterclass) {
        for (final Pair<EnumCreneau, MasterClass> locKey : _creneauMCUsage.keySet()) {
            if (locKey.getKey().equals(parCreneau) && locKey.getValue().equals(parMasterclass)) {
                return locKey;
            }
        }
        return null;
    }

    public final void removeCreneauUsage(final EnumCreneau parCreneau, final MasterClass parMasterclass) {
        int locCurrentUsage = _creneauUsage.get(parCreneau);
        _creneauUsage.put(parCreneau, --locCurrentUsage);
        locCurrentUsage = _creneauMCUsage.get(retrievePairFromValues(parCreneau, parMasterclass));
        _creneauMCUsage.put(retrievePairFromValues(parCreneau, parMasterclass), --locCurrentUsage);
    }

    private final EnumCreneau getLessUsedCreneau() {
        int locMin = 10000;
        EnumCreneau locMinUsageCreneau = EnumCreneau.CRENEAU1;
        for (final Map.Entry<EnumCreneau, Integer> locEntry : _creneauUsage.entrySet()) {
            if (locEntry.getValue() < locMin) {
                locMin = locEntry.getValue();
                locMinUsageCreneau = locEntry.getKey();
            }
        }
        return locMinUsageCreneau;
    }

    private final EnumCreneau getLessUsedCreneau(final MasterClass parMasterclass) {
        int locMin = 10000;
        EnumCreneau locMinUsageCreneau = EnumCreneau.CRENEAU1;
        for (final Map.Entry<Pair<EnumCreneau, MasterClass>, Integer> locEntry : _creneauMCUsage.entrySet()) {
            if (!locEntry.getKey().getValue().isDispo(locEntry.getKey().getKey())) {
                continue;
            }
            for (final EnumCreneau locCreneau : EnumCreneau.values()) {
                if (!locEntry.getKey().getKey().equals(locCreneau)) {
                    continue;
                }
                if (locEntry.getKey().getValue().equals(parMasterclass)) {
                    if (locEntry.getValue() < locMin) {
                        locMin = locEntry.getValue();
                        locMinUsageCreneau = locCreneau;
                    }
                }
            }
        }
        return locMinUsageCreneau;
    }

    public final int getTotalSallesCapacityOf(final MasterClass parMasterclass) {
        if (_mcTotalSallesCapacity.keySet().contains(parMasterclass)) {
            return _mcTotalSallesCapacity.get(parMasterclass);
        }

        int locTotal = 0;
        for (final Evenement locEvt : _evenements) {
            if (!locEvt.getMasterclass().equals(parMasterclass)) {
                continue;
            }

            locTotal += locEvt.getTotalCapacite();
        }
        _mcTotalSallesCapacity.put(parMasterclass, locTotal);
        return locTotal;
    }

    public final List<Evenement> getAllEvents() {
        return _evenements;
    }

    public final List<Evenement> getAllEvents(final EnumCreneau parCreneau) {
        final List<Evenement> locReturnList = new ArrayList<>();
        for (final Evenement locEvt : _evenements) {
            if (locEvt.getCreneau().equals(parCreneau)) {
                locReturnList.add(locEvt);
            }
        }
        return locReturnList;
    }

    public final void setFullFactor(final double parFullFactor) {
        for (final Evenement locEvt : _evenements) {
            locEvt.setFullFactor(parFullFactor);
        }
    }

    /**
     * Exporte le resultat de la repartition dans des fichiers CSV
     */
    @Override
    public final void exportDataToCSV() {
        final List<List<String>> locData = new ArrayList<>();
        //Export des evenement
        locData.add(EnumDataColumnExportList.PARTICIPANT_MC_SALLE.asListString());
        for (final Evenement locEvenement : _evenements) {
            locData.add(locEvenement.exportDataToCSV());
        }

        //Génération du fichier
        final File locOutputFile = EnumConfigProperty.OUTPUT_F_SALLE.fileV();
        FileUtils.writeCsv(locOutputFile, locData);

    }

    public final void exportMasterclassData(){
//        TODO : Sortir directement du format Excel et pas du CSV
        final File locParentDir = EnumConfigProperty.OUTPUT_D_MC_CHECKLIST.fileV();
        locParentDir.mkdirs();

        for (final MasterClass locMc : MasterClassManager.getInstance().getAllData()) {
            for (final Evenement locEvt : getEvenementsFor(locMc)) {
                for (final Map.Entry<Salle, List<List<String>>> locEntry : locEvt.exportMasterclassData().entrySet()) {
                    final String locFileName = String.format("%s - %s - %s",
                            locEvt.getCreneau(), locMc, locEntry.getKey().getName()
                    ).replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
                    final File locFile = new File(String.format("%s%s%s.csv",
                            locParentDir.getAbsolutePath(),
                            File.separator,
                            locFileName)
                    );
                    FileUtils.writeCsv(locFile, locEntry.getValue());
                }
            }
        }
    }

    public final void exportSallePopulation() {
        exportSallePopulation(true);
    }

    public final List<List<String>> exportSallePopulation(final boolean parWriteFile) {
        _sommeEcartType = 0;
        final List<List<String>> locData = new ArrayList<>();
        locData.add(EnumDataColumnExportList.MC_SALLES_CRENEAU_NB_PARTICIPANTS.asListString());
        for (final MasterClass locMc : MasterClassManager.getInstance().getAllData()) {
            final List<String> locDataLine = new ArrayList<>();
            locDataLine.add(locMc.getName());
            locDataLine.add(locMc.getImposedSalleList().toString());
            int locTotalCapacity = -1, locTotalParticipantsNumber = 0;
            final List<String> locCreneauxData = new ArrayList<>(locMc.getNumberOfDispo());
            for (final EnumCreneau locCreneau : EnumCreneau.values()) {
                final Evenement locEvt = get(locMc, locCreneau, false);
                if (locEvt == null || locEvt.getParticipantsNumber() == 0) {
                    locCreneauxData.add("");
                } else {
                    locTotalCapacity = locEvt.getTotalCapacite();
                    locCreneauxData.add(String.valueOf(locEvt.getParticipantsNumber()));
                    locTotalParticipantsNumber += locEvt.getParticipantsNumber();
                }
            }
            locDataLine.add(String.valueOf(locTotalCapacity));
            locDataLine.addAll(locCreneauxData);
            locDataLine.add(String.valueOf(locTotalParticipantsNumber));
            locDataLine.add(String.valueOf(ParticipantManager.getInstance().getToConsiderFirstPartChoicesStat(locMc)));
            locDataLine.add(String.valueOf(ParticipantManager.getInstance().getAllPartChoicesStat(locMc)));
            final double locEcartType = computeEcartTypeModule(getEvenementsFor(locMc));
            _sommeEcartType += locEcartType;
            final int locEcartTypeInt = (int) Math.floor(locEcartType);
            locDataLine.add(String.valueOf(locEcartTypeInt));
            locDataLine.add(String.valueOf(locEcartType));
            locData.add(locDataLine);
        }

        if (parWriteFile) {
            FileUtils.writeCsv(EnumConfigProperty.OUTPUT_F_NB_PART_BY_CRENEAU.fileV(), locData);
        }

        return locData;
    }

    private final double computeEcartTypeModule(final List<Evenement> parEvenements) {
        int locTotalParticipants = 0;
        int locTotalRealEvtsNumber = 0;
        for (final Evenement locEvt : parEvenements) {
            if (locEvt.isSalleDispo(locEvt.getCreneau()) && locEvt.getMasterclass().isDispo(locEvt.getCreneau()) && locEvt.getParticipantsNumber() > 0) {
                locTotalRealEvtsNumber++;
            }
        }

        for (final Evenement locEvt : parEvenements) {
            if (locEvt.isSalleDispo(locEvt.getCreneau()) && locEvt.getMasterclass().isDispo(locEvt.getCreneau()) && locEvt.getParticipantsNumber() > 0) {
                locTotalParticipants += locEvt.getParticipantsNumber();
            }
        }
        double locMoyenne = (double) locTotalParticipants / (double) locTotalRealEvtsNumber;

        double locVariance = 0.0;
        for (final Evenement locEvt : parEvenements) {
            if (locEvt.isSalleDispo(locEvt.getCreneau()) && locEvt.getMasterclass().isDispo(locEvt.getCreneau()) && locEvt.getParticipantsNumber() > 0) {
                locVariance += Math.pow(locEvt.getParticipantsNumber() - locMoyenne, 2);
            }
        }
        locVariance = locVariance / (double) locTotalRealEvtsNumber;
        return 100 * Math.sqrt(locVariance) / locMoyenne;
    }

    public final double getSommeEcartType() {
        return _sommeEcartType;
    }
}
