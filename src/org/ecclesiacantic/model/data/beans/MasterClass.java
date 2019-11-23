package org.ecclesiacantic.model.data.beans;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.archi.itf.INamedObject;
import org.ecclesiacantic.model.data.beans.creneaux.Disponibilite;
import org.ecclesiacantic.model.data.beans.creneaux.EnumCreneau;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MasterClass implements INamedObject, Comparable<MasterClass>{

    static private final Pattern ID_MC_PATTERN = Pattern.compile("(^[0-9]+).*");

    private final String _id;
    private final String _name;
    private final Disponibilite _disponibilite;
    private final int _diviserEn;
    private int _minSalleCapacity, _mediumSize;
    //Correspond au nombre de fois que la Mc a été demandée dans les 3 premiers choix.
    private int _score;
    private final List<Salle> _imposedSalleList;

    public MasterClass(final String parName, final Disponibilite parDisponibilite) {
        this(parName, parDisponibilite, 1, null);
    }

    public MasterClass(final String parName, final Disponibilite parDisponibilite,
                       final int parDiviserEn, final List<Salle> parImposedSalleList) {
        _name = parName;
        final Matcher locMatcher = ID_MC_PATTERN.matcher(parName);
        if (locMatcher.matches()) {
            _id = locMatcher.group(1);
        } else {
            System.err.println(String.format("Impossible de trouver l'id de la MC %s", parName));
            _id = "-1";
        }
        _disponibilite = parDisponibilite;
        _diviserEn = parDiviserEn;
        _minSalleCapacity = -1; //Not initialised yet
        _score = 0;
        _mediumSize = -1;
        _imposedSalleList = parImposedSalleList;
    }

    public final boolean isDispo(final EnumCreneau parCreneau) {
        return _disponibilite.isDispo(parCreneau);
    }

    public final int getNumberOfDispo() {
        return _disponibilite.numberOfDispo();
    }

    public final void computeMinSalleCapacity(final int parTotalMcInVoeux) {
        _minSalleCapacity = (parTotalMcInVoeux / getNumberOfDispo()) / getDiviserEn();
    }

    public final void computeMediumSize() {
        final int locMinPartSession = EnumConfigProperty.MIN_PART_NB_MC.intV();
        for (int locDiviseur = 3; locDiviseur > 0; locDiviseur--) {
            _mediumSize = (_score / locDiviseur );
            if (_mediumSize >= locMinPartSession)  {
                break;
            }
        }
    }

    public final List<Salle> getImposedSalleList() {
        return _imposedSalleList;
    }

    public final List<Salle> getImposedSalleList(final EnumCreneau parCreneau) {
        final List<Salle> locDispoSalleList = new ArrayList<>();
        for (final Salle locSalle : _imposedSalleList) {
            if (locSalle.isDispo(parCreneau)) {
                locDispoSalleList.add(locSalle);
            }
        }
        return locDispoSalleList;
    }

    @Override
    public final String getName() {
        return _name;
    }

    public final String getId() {
        return _id;
    }

    public final Disponibilite getDisponibilite() {
        return _disponibilite;
    }

    @Override
    public String toString() {
        return getName();
    }

    public final int getMinSalleCapacity() {
        return _minSalleCapacity / _diviserEn;
    }

    public final int getDiviserEn() {
        return _diviserEn;
    }

    public final int getSessionMediumSize() {
        if (_mediumSize < 0) {
            computeMediumSize();
        }
        return _mediumSize;
    }

    public final int getScore() {
        return _score;
    }

    public final void setScore(final int parScore) {
        this._score = parScore;
        computeMediumSize();
    }

    @Override
    public int compareTo(final MasterClass parOtherMasterClass) {
        return Integer.compare(getScore(), parOtherMasterClass.getScore());
    }

    @Override
    public boolean equals(final Object parObj) {
        if (parObj == null || !(parObj instanceof MasterClass)) {
            return false;
        }
        final MasterClass locOther = (MasterClass) parObj;
        return Objects.equals(locOther.getId(), getId());
    }
}
