package org.ecclesiacantic.model.data.archi;

import java.util.Arrays;
import java.util.Collection;

import static org.ecclesiacantic.model.data.archi.EnumDataColumImport.*;

public enum EnumDataColumnImportList {
    
    MASTERCLASS("Master classe") {
        @Override
        public Collection<EnumDataColumImport> getColumns() {
            return Arrays.asList(
                    MC_NAME,
                    MC_CRENEAU_1,
                    MC_CRENEAU_2,
                    MC_CRENEAU_3,
                    MC_DIVISER_EN,
                    MC_SALLE_IMPOSE
            );
        }
    }, 
    
    SALLE("Salle") {
        @Override
        public Collection<EnumDataColumImport> getColumns() {
            return Arrays.asList(
                    S_LOCALISATION,
                    S_NAME,
                    S_CAPACITY,
                    S_CRENEAU_1,
                    S_CRENEAU_2,
                    S_CRENEAU_3,
                    S_REPERE,
                    S_INFOS_SUPP,
                    S_COMMENTAIRES
            );
        }
    }, 
    
    PARTICIPANT("Participant") {
        @Override
        public Collection<EnumDataColumImport> getColumns() {
            return Arrays.asList(
                    P_PRENOM,
                    P_NOM,
                    P_CIVILITE,
                    P_TARIF,
                    P_PRIXPAYE,
                    P_CODEBARRE,
                    P_DATECOMMANDE,
                    P_HEURECOMMANDE,
                    P_NUMERO_BILLET,
                    P_EMAIL,
                    P_DATE_NAISSANCE,
                    P_AGE,
                    P_TELEPHONE,
                    P_CODE_POSTAL,
                    P_PAYS,
                    P_AUTRES_INFOS,
                    P_BESOIN_HEBERGEMENT,
                    P_HEBERGE_AVEC,
                    P_PEUT_HERBERGER,
                    P_STATION_METRO,
                    P_PEUT_ACCUEILLIR,
                    P_VOEU1,
                    P_VOEU2,
                    P_VOEU3,
                    P_VOEU4,
                    P_VOEU5,
                    P_PUPITRE,
                    P_CHORALE,
                    P_CHORALE_REF,
                    P_CHORALE_NN_REF,
                    P_NE_SOUHAITE_PAS_CHANTER,
                    P_MESSAGE,
                    IS_PARTICIPANT_2016,
                    PAR_PARTICIPANT_2016,
                    TYPE_SERVICE,
                    DIOCESE,
                    CHORALE_1,
                    MEDIA,
                    RESEAUX_SOCIAUX,
                    BOUCHE_OREILLE,
                    AIDE_OFFICES,
                    DIOCESE_SNPLS,
                    P_GROUPE_EVANGELISATION,
                    P_GROUPE_CONCERT
            );
        }
    }, 
    
    CHORALE("Chorale") {
        @Override
        public Collection<EnumDataColumImport> getColumns() {
            return Arrays.asList(
                    C_NOM,
                    C_IS_REFERENCE,
                    C_ID_GROUPE_EVAN
            );
        }
    }, 
    
    GROUPE_EVANGELISATION("Groupe d'évangélisation") {
        @Override
        public Collection<EnumDataColumImport> getColumns() {
            return Arrays.asList(
                    GE_ID,
                    GE_PAYS,
                    GE_DEPARTEMENT_OR_ARRONDISSEMENT,
                    GE_LIMITE,
                    GE_CHORALE_REF_NAME,
                    GE_IS_CHORALE_REF
            );
        }
    }, 
    
    GROUPE_CONCERT("Groupe de concert") {
        @Override
        public Collection<EnumDataColumImport> getColumns() {
            return Arrays.asList(
                    GC_GROUPE_EVANGELISATION_ID,
                    GC_GROUPE_CONCERT_ID
            );
        }
    }
    ;

    private final String _name;

    EnumDataColumnImportList(final String parName) {
        _name = parName;
    }

    public abstract Collection<EnumDataColumImport> getColumns();

    public String getName() {
        return _name;
    }
}
