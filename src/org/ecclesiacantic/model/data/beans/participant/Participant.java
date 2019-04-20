package org.ecclesiacantic.model.data.beans.participant;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.Badge;
import org.ecclesiacantic.model.data.beans.Evenement;
import org.ecclesiacantic.model.data.archi.itf.INamedObject;
import org.ecclesiacantic.model.data.beans.MasterClass;
import org.ecclesiacantic.model.data.beans.creneaux.EnumCreneau;
import org.ecclesiacantic.model.data.groupe_evangelisation.ARegion;
import org.ecclesiacantic.model.data.groupe_evangelisation.GroupeEvangelisation;
import org.ecclesiacantic.model.data_manager.EvenementManager;
import org.ecclesiacantic.model.data_manager.GroupeConcertManager;
import org.ecclesiacantic.model.data_manager.GroupeEvangelisationManager;
import org.ecclesiacantic.model.data_manager.bean.ChoraleManager;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Participant implements INamedObject, Comparable<Participant> {

    static private final Pattern GROUPE_ID_PATTERN = Pattern.compile("(^[0-9]+) .*");

    final private String _prenom, _nom;
    final private EnumCivilite _civilite;
    final private Tarif _tarif;
    final private ARegion _region;
    final private String _codeBarre, _numBillet, _email, _telephone;
    final private Date _inscriptionDate, _dateNaissance;
    final private int _age;
    final private String _autreInfos, _message, _diocese, _codePostal;
    final private boolean _needHerbegement, canHebergeOther, _chante, _is2016Participant, aideOffices;
    final private Hebergement _hebergement;
    final private LinkedList<MasterClass> _voeux;
    final private EnumPupitre _pupitre;
    final private Chorale _chorale;
    final private Map<EnumConnaitPar, Boolean> _connaitParList;
    final private List<Evenement> _evenements;
    private String _groupeEvangelisationId, _groupeEvangelisationString, _groupeConcertId;
    private int _numBagagerie;
    private Badge _badge;

    public Participant(final String parPrenom, final String parNom, final EnumCivilite parCivilite,
                       final Tarif parTarif, final String parCodeBarre, final String parNumBillet,
                       final String parEmail, final String parTelephone, final ARegion parRegion,
                       final String parCodePostal,
                       final Date parInscriptionDate, final Date parDateNaissance,
                       final int parAge, final String parAutreInfos, final String parMessage, final String parDiocese,
                       final boolean parNeedHerbegement, final boolean parCanHebergeOther, final boolean parNeChantePas,
                       final boolean parIs2016Participant, final boolean parAideOffices, final Hebergement parHebergement,
                       final LinkedList<MasterClass> parVoeux, final EnumPupitre parPupitre, final Chorale parChorale,
                       final Map<EnumConnaitPar, Boolean> parConnaitParList,
                       final String parGroupeEvangelisationId, final String parGroupeConcertId) {
        this._prenom = parPrenom;
        this._nom = parNom;
        this._civilite = parCivilite;
        this._tarif = parTarif;
        this._codeBarre = parCodeBarre;
        this._numBillet = parNumBillet;
        this._email = parEmail;
        this._telephone = parTelephone;
        this._region = parRegion;
        this._codePostal = parCodePostal;
        this._inscriptionDate = parInscriptionDate;
        this._dateNaissance = parDateNaissance;
        this._age = parAge;
        this._autreInfos = parAutreInfos;
        this._message = parMessage;
        this._diocese = parDiocese;
        this._needHerbegement = parNeedHerbegement;
        this.canHebergeOther = parCanHebergeOther;
        this._chante = !parNeChantePas;
        this._is2016Participant = parIs2016Participant;
        this.aideOffices = parAideOffices;
        this._hebergement = parHebergement;
        this._voeux = parVoeux;
        this._pupitre = parPupitre;
        this._chorale = parChorale;
        this._connaitParList = parConnaitParList;
        this._groupeEvangelisationString = parGroupeEvangelisationId;
        final Matcher locMatcher = GROUPE_ID_PATTERN.matcher(parGroupeEvangelisationId);
        if (locMatcher.matches()) {
            _groupeEvangelisationId = locMatcher.group(1);
        } else {
            _groupeEvangelisationId = "-1";
        }
        this._groupeConcertId = parGroupeConcertId;
        this._evenements = new ArrayList<>();
        _badge = null;
        _numBagagerie = -1;
    }

    @Override
    public String getName() {
        return _numBillet;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", _numBillet, _email);
    }

    /**
     * retourne le voeu en fonction de la priorité 0 = 1er voeu
     * @param parPriority commence à 0
     * @return le voeu demandé si trouvé ou celui qui s'en rapproche le plus
     */
    public final MasterClass getVoeu(final int parPriority) {
        if (_voeux.size() > parPriority && _voeux.get(parPriority) != null) {
            return _voeux.get(parPriority);
        } else {
            throw new ArrayIndexOutOfBoundsException(String.format("On a cherché dans tous les voeux du participant %s", this));
        }
    }

    /**
     *
     * @param parCreneau
     * @return l'événement prévu pour le participant courant au créneau indiqué, null sinon
     */
    public final Evenement getEvenement(final EnumCreneau parCreneau) {
        for (final Evenement locEvt : _evenements) {
            if (locEvt.getCreneau().equals(parCreneau)) {
                return locEvt;
            }
        }
        return null;
    }

    /**
     *
     * @param parMasterClass
     * @return Le creneau auquel la masterclass est prévu pour le participant, null sinon
     */
    public final EnumCreneau getCreneau(final MasterClass parMasterClass) {
        for (final Evenement locEvt : _evenements) {
            if (locEvt.getMasterclass().equals(parMasterClass) ) {
                return locEvt.getCreneau();
            }
        }
        return null;
    }

    public final boolean isAllEvtScheduled() {
        return _evenements.size() == EnumCreneau.getNumberOfCreneaux();
    }

    public final boolean isEvenementCompatible(final Evenement parEvenement) {
        return isEvenementCompatible(parEvenement, true);
    }

    public final boolean isEvenementCompatible(final Evenement parEvenement, final boolean parPrintError) {
        if (_evenements.size() >= EnumCreneau.getNumberOfCreneaux()) {
            if (parPrintError) {
                System.err.println(String.format("Le participant %s a déjà %d créneaux réservés : %s",
                        this.toString(), EnumCreneau.getNumberOfCreneaux(), _evenements.toString())
                );
            }
            return false;
        }

        //L'événement est déjà plein
        if (parEvenement.isFull()) {
            return false;
        }

        final EnumCreneau locEvtCreneau = parEvenement.getCreneau();
        final MasterClass locMasterClass = parEvenement.getMasterclass();

        // On vérifie qu'il n'y a rien de planifié à ce créneau
        final Evenement locEvtSameCreneau = getEvenement(parEvenement.getCreneau());
        if (locEvtSameCreneau != null) {
            if (parPrintError) {
                System.err.println(String.format("Le participant %s a déjà quelque chose de prévu (%s) au créneau %s",
                        this.toString(), locEvtSameCreneau.toString(), locEvtCreneau.toString())
                );
            }
            return false;
        }

        //On vérifie que la masterclass n'a pas déjà été choisie
        final EnumCreneau locCreneauSameMc = getCreneau(locMasterClass);
        if (locCreneauSameMc != null) {
            return false;
        }

        //On vérifie que la salle est dispo
        if (!parEvenement.isSalleDispo(parEvenement.getCreneau())) {
            if (parPrintError) {
                System.err.println(String.format("La salle %s n'est pas disponible pour la MC %s au créneau %s",
                        parEvenement.getSallesString(), parEvenement.getMasterclass(), parEvenement.getCreneau()));
            }
            return false;
        }
        return true;
    }

    public final boolean addEvenementIfCompatible(final Evenement parEvenement) {
        if (isEvenementCompatible(parEvenement)) {
            _evenements.add(parEvenement);
            return true;
        }
        return false;
    }

    public final void removeEvenement(final Evenement parEvenement) {
        if (_evenements.contains(parEvenement)) {
            _evenements.remove(parEvenement);
        } else {
            System.err.println(String.format("L'événement %s n'a pas été enregistré pour %s",
                    parEvenement.toString(), this.toString()));
        }
    }

    /**
     * Donne la priorité d'un participant pour une masterclass donnée.
     * @param parMasterClass
     * @return plus le chiffre est grand plus la priorité est faible
     */
    public final int getPriorityOf(final MasterClass parMasterClass) {
        int locIndex = 0;
        for (final MasterClass locMc : _voeux) {
            if (locMc.equals(parMasterClass)) {
                return locIndex;
            }
        }
        return 1000;
    }

    public final EnumCreneau nextAvailableCreneau(final EnumCreneau parCurrentCreneau,
                                                  final boolean parForward) {
        EnumCreneau locTmpCreneau = parCurrentCreneau.forward(parForward);
        for (int locEvtCpt = 0; locEvtCpt < EnumCreneau.getNumberOfCreneaux(); locEvtCpt++) {
            if (getEvenement(locTmpCreneau) == null) {
                return locTmpCreneau;
            } else {
                locTmpCreneau = locTmpCreneau.forward(parForward);
            }
        }
        return null;
    }

    private final Badge generateBadge() {
        final Map<EnumCreneau, Evenement> locEvtsMap = new HashMap<>(_evenements.size());
        for (final Evenement locEvt : _evenements) {
            locEvtsMap.put(locEvt.getCreneau(), locEvt);
        }
        return new Badge(this, locEvtsMap);
    }

    public final Badge getBadge() {
        if (_badge == null) {
            _badge = generateBadge();
        }
        return _badge;
    }

    public final String getGroupeEvangelisationId() {
        return _groupeEvangelisationId;
    }

    public final String getGroupeEvangelisationString() {
        return _groupeEvangelisationString;
    }

    public final int getGroupeConcertId() {
        final GroupeEvangelisation locNotFoundGroup = GroupeEvangelisationManager.getInstance().getNullData();
        if (EnumConfigProperty.IS_COMPUTE_GE_REPART.boolV()) {
            if (_groupeEvangelisationId != null && !locNotFoundGroup.getId().equals(_groupeEvangelisationId)) {
                return GroupeConcertManager.getInstance().getGroupeConcertOf(_groupeEvangelisationId).getId();
            } else {
                if (_chante) {
                    System.err.println(String.format("Le groupe d'évangélisation du participant %s est null", this));
                }
                return Integer.parseInt(locNotFoundGroup.getId());
            }
        } else {
            if (_chante) {
                return Integer.parseInt(_groupeConcertId);
            } else {
                return Integer.parseInt(locNotFoundGroup.getId());
            }
        }
    }

    public final void setGroupeEvangelisationId(final String parGroupeEvangelisationId) {
        this._groupeEvangelisationId = parGroupeEvangelisationId;
    }

    public final boolean isChoraleAffilie() {
        return !ChoraleManager.getInstance().getNullData().equals(_chorale);
    }

    public final String getPrenom() {
        return _prenom;
    }

    public final String getNom() {
        return _nom;
    }

    public final EnumCivilite getCivilite() {
        return _civilite;
    }

    public final Tarif getTarif() {
        return _tarif;
    }

    public final String getCodeBarre() {
        return _codeBarre;
    }

    public final String getNumBillet() {
        return _numBillet;
    }

    public final String getEmail() {
        return _email;
    }

    public final String getTelephone() {
        return _telephone;
    }

    public final ARegion getRegion() {
        return _region;
    }

    public final String getCodePostal() {
        return _codePostal;
    }

    public final Date getInscriptionDate() {
        return _inscriptionDate;
    }

    public final Date getDateNaissance() {
        return _dateNaissance;
    }

    public final int getAge() {
        return _age;
    }

    public final String getAutreInfos() {
        return _autreInfos;
    }

    public final String getMessage() {
        return _message;
    }

    public final String getDiocese() {
        return _diocese;
    }

    public final boolean isNeedHerbegement() {
        return _needHerbegement;
    }

    public final boolean isCanHebergeOther() {
        return canHebergeOther;
    }

    public final boolean isChante() {
        return _chante;
    }

    public final boolean is2016Participant() {
        return _is2016Participant;
    }

    public final boolean isAideOffices() {
        return aideOffices;
    }

    public final Hebergement getHebergement() {
        return _hebergement;
    }

    public final LinkedList<MasterClass> getVoeux() {
        return _voeux;
    }

    public final EnumPupitre getPupitre() {
        return _pupitre;
    }

    public final Chorale getChorale() {
        return _chorale;
    }

    public final Map<EnumConnaitPar, Boolean> getConnaitParList() {
        return _connaitParList;
    }

    public final List<Evenement> getEvenements() {
        return _evenements;
    }

    public final void resetEvenements() {
        final Iterator<Evenement> locIterator = _evenements.iterator();
        while (locIterator.hasNext()) {
            removeEvenement(locIterator.next());
        }
        for (final Evenement locEvt : EvenementManager.getInstance().getAllEvents()) {
            locEvt.getParticipants().remove(this);
        }

    }

    public final List<MasterClass> getAttribuedMasterClasses() {
        final List<MasterClass> locMcList = new ArrayList<>();
        for (final Evenement locEvt : getEvenements()) {
            locMcList.add(locEvt.getMasterclass());
        }
        return locMcList;
    }

    @Override
    public int compareTo(final Participant parOtherParticipant) {
        return _numBillet.compareTo(parOtherParticipant.getNumBillet());
    }

    public final int getNumBagagerie() {
        return _numBagagerie;
    }

    public final void setNumBagagerie(final int parNumBagagerie) {
        this._numBagagerie = parNumBagagerie;
    }
}
