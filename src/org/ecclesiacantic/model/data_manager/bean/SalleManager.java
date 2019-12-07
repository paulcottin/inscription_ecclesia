package org.ecclesiacantic.model.data_manager.bean;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.model.data.archi.EnumDataType;
import org.ecclesiacantic.model.data.beans.Evenement;
import org.ecclesiacantic.model.data.beans.MasterClass;
import org.ecclesiacantic.model.data.beans.Salle;
import org.ecclesiacantic.model.data.beans.creneaux.Disponibilite;
import org.ecclesiacantic.model.data.beans.creneaux.EnumCreneau;
import org.ecclesiacantic.model.data_manager.ADataManager;
import org.ecclesiacantic.model.data_manager.EvenementManager;
import org.ecclesiacantic.utils.DispoUtils;
import org.ecclesiacantic.utils.parser.helper.exception.ObjectInstanciationException;

import java.util.*;

public class SalleManager extends ADataManager<Salle> {

    static private SalleManager _instance;

    static public final SalleManager getInstance() {
        if (_instance == null) {
            _instance = new SalleManager();
        }
        return _instance;
    }

    private SalleManager() {
        super(EnumConfigProperty.INPUT_F_SALLE, EnumDataType.SALLE);
    }

    public final List<Salle> getSallesDispoAt(final EnumCreneau parCreneau) {
        final List<Salle> locReturnList = new ArrayList<>();
        for (final Salle locSalle : _dataMap.values()) {
            if (locSalle.isDispo(parCreneau)) {
                locReturnList.add(locSalle);
            }
        }
        return locReturnList;
    }

    /**
     * Si une salle a déjà été attribuée à un evt de la mm MC et si cette même salle est toujours dispo
     * alors on renvoie cette salle. Sinon on renvoie la salle qui correspond le mieux aux capacités
     * de la MC
     * @param parEvenement
     * @return
     */
    public final List<Salle> getSalleForEvenement(final Evenement parEvenement) {
        // Si l'événement a déjà toutes ses salles, il vire
        if (parEvenement.getSalleList().size() == parEvenement.getMasterclass().getDiviserEn()) {
            return parEvenement.getSalleList();
        }

        final Collection<Evenement> locEvtForMc = EvenementManager.getInstance().getNextAvailablesMcs(
                parEvenement.getMasterclass(), parEvenement.getCreneau()
        ).values();

        final int locNbSalleByMc = parEvenement.getMasterclass().getDiviserEn();
        int locNbSalleCurrent = 0;
        final Set<Salle> locReturnSet = new HashSet<>(locNbSalleByMc);
        for (final Salle locSalle : getAlreadyDefinedSalleSet(new ArrayList<>(locEvtForMc))) {
            if (locSalle.isDispo(parEvenement.getCreneau()) &&
                    !parEvenement.getSalleList().contains(locSalle)) {
                locReturnSet.add(locSalle);
                locNbSalleCurrent++;
                if (locNbSalleCurrent == locNbSalleByMc) {
                    return new ArrayList<>(locReturnSet);
                }
            }
        }

        while (locReturnSet.size() < locNbSalleByMc) {
            locReturnSet.add(getBestSalleForMcBasedOnCapacity(
                    parEvenement.getMasterclass(),
                    parEvenement.getCreneau(),
                    locReturnSet,
                    false
            ));
        }
        return new ArrayList<>(locReturnSet);
    }

    /**
     * Renvoie la salle qui correspond le mieux à la capacité de la MC (en fonction du
     * plus petit écart positif entre le score de la MC et la capacité et disponibilité de la salle)
     * @param parMasterclass
     * @return
     */
    private final Salle getBestSalleForMcBasedOnCapacity(final MasterClass parMasterclass,
                                                         final EnumCreneau parCreneau,
                                                         final Set<Salle> parToExcludeSalleList,
                                                         final boolean parAcceptNegativeDelta) {
        final Salle locResultSalle = getBestSalleForMcBasedOnCapacityHelper(parMasterclass, parCreneau, parToExcludeSalleList, parAcceptNegativeDelta);
        if (locResultSalle != null) {
            parToExcludeSalleList.add(locResultSalle);
            return locResultSalle;
        }

        EnumCreneau locCurrentCreneau = parCreneau.next();
        while (!locCurrentCreneau.equals(parCreneau)) {
            final Salle locSalle = getBestSalleForMcBasedOnCapacityHelper(parMasterclass, parCreneau, parToExcludeSalleList, parAcceptNegativeDelta);
            if (locSalle != null) {
                parToExcludeSalleList.add(locSalle);
                return locSalle;
            } else {
                locCurrentCreneau = locCurrentCreneau.next();
            }
        }
        final Salle locSurpopSalle = parAcceptNegativeDelta ? null : getBestSalleForMcBasedOnCapacity(parMasterclass, parCreneau, parToExcludeSalleList, true);
        if (locSurpopSalle != null) {
            parToExcludeSalleList.add(locSurpopSalle);
            return locSurpopSalle;
        } else {
            System.err.println(String.format("Impossible de trouver une salle correspondant à la MC %s à %s",
                    parMasterclass, parCreneau));
            return null;
        }
    }

    private final Salle getBestSalleForMcBasedOnCapacityHelper(final MasterClass parMasterclass,
                                                         final EnumCreneau parCreneau,
                                                         final Set<Salle> parToExcludeSalleList,
                                                         final boolean parAcceptNegativeDelta) {
        final List<Salle> locToExcludeSalleList = new ArrayList<>();
        if (parToExcludeSalleList != null) {
            locToExcludeSalleList.addAll(parToExcludeSalleList);
        }

        final Set<Salle> locSalleDispo = new HashSet<>();
        final Set<Salle> locAlreadyDefinedSalleList = getAlreadyDefinedSalleSet();
        for (final Salle locSalle : _dataMap.values()) {
            if (locToExcludeSalleList.contains(locSalle) ||
                    locAlreadyDefinedSalleList.contains(locSalle)) {
                continue;
            }

            if (locSalle.isDispo(parCreneau)) {
                locSalleDispo.add(locSalle);
            }
        }

        int locMinDelta = 1000;
        Salle locMinSalle = null;
        for (final Salle locSalle : locSalleDispo) {
            final int locRealDelta = locSalle.getCapacite() - parMasterclass.getSessionMediumSize();
            final int locDelta = parAcceptNegativeDelta ? Math.abs(locRealDelta) : locRealDelta;
            if (locDelta >= 0 && locDelta < locMinDelta) {
                locMinDelta = locDelta;
                locMinSalle = locSalle;
            }
        }

        if (locMinSalle != null) {
            return locMinSalle;
        } else {
            return null;
        }
    }

    private final Set<Salle> getAlreadyDefinedSalleSet() {
        return getAlreadyDefinedSalleSet(null);
    }

    /**
     * Renvoie une liste de salle qui sont déjà attribuées à un événement
     * @param parFiltreEvt : Permet de filtrer sur le nombre d'événement à parcourir. Si null on prend tous les événements
     * @return
     */
    private final Set<Salle> getAlreadyDefinedSalleSet(final List<Evenement> parFiltreEvt) {
        final List<Evenement> locFiltreEvt = parFiltreEvt != null ? parFiltreEvt : EvenementManager.getInstance().getAllEvents();
        final Set<Salle> locAlreadyChoosedSalles = new HashSet<>();
        for (final Evenement locEvt : locFiltreEvt) {
            locAlreadyChoosedSalles.addAll(locEvt.getSalleList());
        }
        return locAlreadyChoosedSalles;
    }

    @Override
    public void reset() {

    }

    @Override
    protected Salle convertStringMapToObject(Map<EnumDataColumImport, String> parStringMapHeaderValue) throws ObjectInstanciationException {
        final Disponibilite locDisponibilite = DispoUtils.getDisponibiliteFromStringMap(parStringMapHeaderValue);

        return new Salle(
                stringV(parStringMapHeaderValue,EnumDataColumImport.S_NAME),
                intV(parStringMapHeaderValue,EnumDataColumImport.S_CAPACITY),
                locDisponibilite,
                stringV(parStringMapHeaderValue,EnumDataColumImport.S_REPERE)
                );
    }
}
