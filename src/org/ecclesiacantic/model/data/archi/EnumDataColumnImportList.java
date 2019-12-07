package org.ecclesiacantic.model.data.archi;

import java.util.Arrays;
import java.util.Collection;

import static org.ecclesiacantic.model.data.archi.EnumDataColumImport.*;

public enum EnumDataColumnImportList {
    
    MASTERCLASS {
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
    
    SALLE {
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
    
    PARTICIPANT {
        @Override
        public Collection<EnumDataColumImport> getColumns() {
            return Arrays.asList(
                    P_PRENOM,
                    P_NOM,
                    P_CIVILITE,
                    P_NUMERO_BILLET,
                    P_EMAIL,
                    P_CODE_POSTAL,
                    P_PAYS,
                    P_VOEU1,
                    P_VOEU2,
                    P_VOEU3,
                    P_VOEU4,
                    P_VOEU5,
                    P_PUPITRE,
                    P_CHORALE,
                    P_NE_SOUHAITE_PAS_CHANTER,
                    P_GROUPE_EVANGELISATION,
                    P_GROUPE_CONCERT
            );
        }
    }, 
    
    CHORALE {
        @Override
        public Collection<EnumDataColumImport> getColumns() {
            return Arrays.asList(
                    C_NOM,
                    C_IS_REFERENCE,
                    C_ID_GROUPE_EVAN
            );
        }
    }, 
    
    GROUPE_EVANGELISATION {
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
    
    GROUPE_CONCERT {
        @Override
        public Collection<EnumDataColumImport> getColumns() {
            return Arrays.asList(
                    GC_GROUPE_EVANGELISATION_ID,
                    GC_GROUPE_CONCERT_ID
            );
        }
    },

    SOLOS_GEOGRAPHIQUES {
        @Override
        public Collection<EnumDataColumImport> getColumns() {
            return Arrays.asList(
                    GE_ID,
                    GE_DEPARTEMENT_OR_ARRONDISSEMENT,
                    GE_LIMITE,
                    GE_PAYS
            );
        }
    }
    ;

    public abstract Collection<EnumDataColumImport> getColumns();
}
