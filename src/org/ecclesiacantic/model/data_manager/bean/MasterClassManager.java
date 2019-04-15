package org.ecclesiacantic.model.data_manager.bean;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.archi.EnumDataType;
import org.ecclesiacantic.model.data.beans.Evenement;
import org.ecclesiacantic.model.data.beans.MasterClass;
import org.ecclesiacantic.model.data.beans.Salle;
import org.ecclesiacantic.model.data.beans.creneaux.Disponibilite;
import org.ecclesiacantic.model.data.beans.participant.Participant;
import org.ecclesiacantic.model.data_manager.ADataManager;
import org.ecclesiacantic.model.data_manager.EvenementManager;
import org.ecclesiacantic.utils.DispoUtils;
import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.utils.StringUtils;
import org.ecclesiacantic.utils.parser.NumberUtils;
import org.ecclesiacantic.utils.parser.helper.exception.ObjectInstanciationException;

import java.util.*;

public class MasterClassManager extends ADataManager<MasterClass> {

    static private MasterClassManager _instance;

    static public final MasterClassManager getInstance() {
        if (_instance == null) {
            _instance = new MasterClassManager();
        }
        return _instance;
    }

    private int _lessPopularMcLimit, _hugeSallesMcLimit;
    private final List<MasterClass> _lessPopularMcList, _hugeSallesMcList;
    private final Set<MasterClass> _notAllCreneauxMcSet;
    private final Map<Participant, List<MasterClass>> _lessPopularMcChoiceByParticipant, _hugeSallesForMcByParticipant, _notAllCreneauxMcDispoByParticipant;

    private MasterClassManager() {
        super(EnumConfigProperty.INPUT_F_MC, EnumDataType.MASTERCLASS);
        _lessPopularMcLimit = EnumConfigProperty.LESS_POP_MC_NB.intV();
        _hugeSallesMcLimit = EnumConfigProperty.HUGE_SALLE_NB.intV();
        _lessPopularMcList = new ArrayList<>(_lessPopularMcLimit);
        _hugeSallesMcList = new ArrayList<>(_hugeSallesMcLimit);
        _lessPopularMcChoiceByParticipant = new HashMap<>();
        _hugeSallesForMcByParticipant = new HashMap<>();
        _notAllCreneauxMcSet = new HashSet<>();
        _notAllCreneauxMcDispoByParticipant = new HashMap<>();
    }

    @Override
    public void reset() {
        _hugeSallesMcList.clear();
        _lessPopularMcList.clear();
        _notAllCreneauxMcSet.clear();
        _hugeSallesForMcByParticipant.clear();
        _lessPopularMcChoiceByParticipant.clear();
        _notAllCreneauxMcDispoByParticipant.clear();
    }

    @Override
    protected MasterClass convertStringMapToObject(final Map<EnumDataColumImport, String> parStringMapHeaderValue) throws ObjectInstanciationException {
        final Disponibilite locDisponibilite = DispoUtils.getDisponibiliteFromStringMap(parStringMapHeaderValue);
        final String locImposedSalleName = stringV(parStringMapHeaderValue,EnumDataColumImport.MC_SALLE_IMPOSE);

        final List<Salle> locImposedSalleList;
        if (!StringUtils.isNullOrEmpty(locImposedSalleName)) {
            locImposedSalleList = new ArrayList<>(1);
            if (locImposedSalleName.contains(";")) {
                for (final String locSalleName : locImposedSalleName.split(";")) {
                    locImposedSalleList.add(SalleManager.getInstance().get(locSalleName));
                }
            } else {
                locImposedSalleList.add(SalleManager.getInstance().get(locImposedSalleName));
            }
        } else {
            locImposedSalleList = null;
        }
        final String locDiviserEn = stringV(parStringMapHeaderValue,EnumDataColumImport.MC_DIVISER_EN);
        return new MasterClass(
                stringV(parStringMapHeaderValue,EnumDataColumImport.MC_NAME),
                locDisponibilite,
                StringUtils.isNullOrEmpty(locDiviserEn) ? 1 :NumberUtils.convertFieldToInt(locDiviserEn),
                locImposedSalleList
        );
    }

    /**
     * En fonction des demandes des participants et des disponibilités des masterclasse
     * définir le nombre minimum de place que doit faire la salle où la MC aura lieu pour que tous les
     * participants puissent avoir dans leurs n premiers choix.
     */
    public final void initMinSalleCapacityForEachMc() {
        //Défini le besoin avec une map contenant le lien MC => nb totales de demandes dans les n premiers voeux
        final int locNbVoeuxConsidered = 3;
        final Map<MasterClass, Integer> locPopularityMap = new HashMap<>();

        for (final Participant locParticipant : ParticipantManager.getInstance().getAllData()) {
            for (int locIndex = 0; locIndex < locNbVoeuxConsidered; locIndex++) {
                final MasterClass locTmpMc = locParticipant.getVoeu(locIndex);
                if (locPopularityMap.containsKey(locTmpMc)) {
                    final int locCurrentSum = locPopularityMap.get(locTmpMc);
                    locPopularityMap.put(locTmpMc, locCurrentSum + 1);
                } else {
                    locPopularityMap.put(locTmpMc, 1);
                }
            }
        }

        //On fixe le nombre min en fonction du nombre de creneau où une MC est dispo
        for (final Map.Entry<MasterClass, Integer> locEntry : locPopularityMap.entrySet()) {
            locEntry.getKey().computeMinSalleCapacity(locEntry.getValue());
        }
    }

    /**
     * En fonction des demandes des participants et des disponibilités des masterclasse
     * définir le nombre minimum de place que doit faire la salle où la MC aura lieu pour que tous les
     * participants puissent avoir dans leurs n premiers choix.
     */
    public final void computeMasterClassScore() {
        //Défini le besoin avec une map contenant le lien MC => nb totales de demandes dans les n premiers voeux
        final int locNbVoeuxConsidered = EnumConfigProperty.NB_VOEUX_CONSIDERED.intV();
        final Map<MasterClass, Integer> locPopularityMap = new HashMap<>();

        for (final Participant locParticipant : ParticipantManager.getInstance().getAllData()) {
            for (int locIndex = 0; locIndex < locNbVoeuxConsidered; locIndex++) {
                final MasterClass locTmpMc = locParticipant.getVoeu(locIndex);
                if (locPopularityMap.containsKey(locTmpMc)) {
                    final int locCurrentSum = locPopularityMap.get(locTmpMc);
                    locPopularityMap.put(locTmpMc, locCurrentSum + 1);
                } else {
                    locPopularityMap.put(locTmpMc, 1);
                }
            }
        }

        //On fixe le score en fonction du nombre de fois que la MC a été demandée ds les 3iers choix.
        for (final Map.Entry<MasterClass, Integer> locEntry : locPopularityMap.entrySet()) {
            locEntry.getKey().setScore(locEntry.getValue());
        }
    }

    /**
     * Si le participant a dans ses voeux une des MCs les moins demandées alors on la lui donne
     * @param parParticipant
     * @return la MC ou null si pas dans ses voeux
     */
    public final List<MasterClass> getLessPopularMc(final Participant parParticipant) {
        if (!_lessPopularMcChoiceByParticipant.containsKey(parParticipant)) {
            _lessPopularMcChoiceByParticipant.put(parParticipant, new ArrayList<>());
        }
        final List<MasterClass> locReturnList = new ArrayList<>();
        for (final MasterClass locVoeu : parParticipant.getVoeux()) {
            if (getLessPopularMcList().contains(locVoeu)) {
                if (_lessPopularMcChoiceByParticipant.get(parParticipant).contains(locVoeu)) {
                    continue;
                } else {
                    _lessPopularMcChoiceByParticipant.get(parParticipant).add(locVoeu);
                    locReturnList.add(locVoeu);
                }
            }
        }
        return locReturnList;
    }

    public final boolean isInLessPopularMcList(final MasterClass parMasterclass) {
        return getLessPopularMcList().contains(parMasterclass);
    }

    /**
     * Donne la liste des X MC les moins choisies par les participant
     * X est défini par la propriété less_popular_mc_number dans la config
     * @return
     */
    public final List<MasterClass> getLessPopularMcList() {
        if (_lessPopularMcList.size() > 0) {
            return _lessPopularMcList;
        }

        for (int locCpt = 0; locCpt < _lessPopularMcLimit; locCpt++) {
            int locMin = 1000;
            MasterClass locMinMc = null;
            for (final MasterClass locMc : _dataMap.values()) {
                if (_lessPopularMcList.contains(locMc)) {
                    continue;
                }
                if (locMc.getScore() < locMin) {
                    locMin = locMc.getScore();
                    locMinMc = locMc;
                }
            }
            _lessPopularMcList.add(locMinMc);
        }
        return _lessPopularMcList;
    }

    /**
     * Si le participant a dans ses voeux une des MCs dont les salles sont les plus grandes alors on la lui donne
     * @param parParticipant
     * @return la MC ou list.empty() si pas dans ses voeux
     */
    public final List<MasterClass> getHugeSalleForMc(final Participant parParticipant) {
        if (!_hugeSallesForMcByParticipant.containsKey(parParticipant)) {
            _hugeSallesForMcByParticipant.put(parParticipant, new ArrayList<>());
        }
        final List<MasterClass> locReturnList = new ArrayList<>();
        for (final MasterClass locVoeu : parParticipant.getVoeux()) {
            if (getHugeSallesMcList().contains(locVoeu)) {
                if (_hugeSallesForMcByParticipant.get(parParticipant).contains(locVoeu)) {
                    continue;
                } else {
                    _hugeSallesForMcByParticipant.get(parParticipant).add(locVoeu);
                    locReturnList.add(locVoeu);
                }
            }
        }
        return locReturnList;
    }

    /**
     * Donne la liste des X MC dont les salles sont les plus grandes
     * X est défini par la propriété huge_salles_mc_number dans la config
     * @return
     */
    public final List<MasterClass> getHugeSallesMcList() {
        if (_hugeSallesMcList.size() > 0) {
            return _hugeSallesMcList;
        }

        //Initialise la liste des totals de places des salles des MCs
        final Map<MasterClass, Integer> locAllMapValues = new HashMap<>(_dataMap.size());
        for (final MasterClass locMc : _dataMap.values()) {
            locAllMapValues.put(locMc, EvenementManager.getInstance().getTotalSallesCapacityOf(locMc));
        }

        //Récupère les X max
        for (int locCpt = 0; locCpt < _hugeSallesMcLimit; locCpt++) {
            int locMax = 0;
            MasterClass locMaxMc = null;
            for (final Map.Entry<MasterClass, Integer> locEntry : locAllMapValues.entrySet()) {
                if (_hugeSallesMcList.contains(locEntry.getKey())) {
                    continue;
                }
                if (locEntry.getValue() > locMax) {
                    locMax = locEntry.getValue();
                    locMaxMc = locEntry.getKey();
                }
            }
            _hugeSallesMcList.add(locMaxMc);
        }
        return _hugeSallesMcList;
    }

    /**
     * Si le participant a dans ses voeux une des MCs dont tous les créneaux ne sont pas dispo alors on la lui donne
     * @param parParticipant
     * @return la MC ou list.empty() si pas dans ses voeux
     */
    public final List<MasterClass> getNotAllCreneauxDispoFor(final Participant parParticipant) {
        if (!_notAllCreneauxMcDispoByParticipant.containsKey(parParticipant)) {
            _notAllCreneauxMcDispoByParticipant.put(parParticipant, new ArrayList<>());
        }
        final List<MasterClass> locReturnList = new ArrayList<>();
        for (final MasterClass locVoeu : parParticipant.getVoeux()) {
            if (getNotAllDispoMcSet().contains(locVoeu)) {
                if (_notAllCreneauxMcDispoByParticipant.get(parParticipant).contains(locVoeu)) {
                    continue;
                } else {
                    _notAllCreneauxMcDispoByParticipant.get(parParticipant).add(locVoeu);
                    locReturnList.add(locVoeu);
                }
            }
        }
        return locReturnList;
    }

    /**
     * Donne la liste des MC qui ne sont pas dispos à tous les créneaux (à cause de la MC ou de la salle)
     * @return
     */
    public final Set<MasterClass> getNotAllDispoMcSet() {
        if (_notAllCreneauxMcSet.size() > 0) {
            return _notAllCreneauxMcSet;
        }

        for (final Evenement locEvt : EvenementManager.getInstance().getAllEvents()) {
            if (!locEvt.isSalleDispo(locEvt.getCreneau()) || locEvt.getMasterclass().isDispo(locEvt.getCreneau())) {
                _notAllCreneauxMcSet.add(locEvt.getMasterclass());
            }
        }
        return _notAllCreneauxMcSet;
    }

    public final void setLessPopularMcLimit(final int parLessPopularMcLimit) {
        this._lessPopularMcLimit = parLessPopularMcLimit;
    }

    public final void setHugeSallesMcLimit(final int parHugeSallesMcLimit) {
        this._hugeSallesMcLimit = parHugeSallesMcLimit;
    }
}
