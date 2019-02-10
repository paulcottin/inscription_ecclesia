package org.ecclesiacantic.model.data.archi;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.google.GoogleSpreadsheetConfig;
import org.ecclesiacantic.google.GoogleSpreadsheetConfigManager;

import java.util.Arrays;
import java.util.List;

public enum EnumDataType {

    MASTERCLASS {
        public final List<EnumDataColumImport> getDataFileHeader() {
            return Arrays.asList(
                    EnumDataColumImport.MC_NAME,
                    EnumDataColumImport.MC_CRENEAU_1,
                    EnumDataColumImport.MC_CRENEAU_2,
                    EnumDataColumImport.MC_CRENEAU_3,
                    EnumDataColumImport.MC_DIVISER_EN,
                    EnumDataColumImport.MC_SALLE_IMPOSE
            );
        }

        @Override
        public final EnumDataColumImport getHeaderId() {
            return EnumDataColumImport.MC_NAME;
        }

        @Override
        public String getTypeName() {
            return "MasterClass";
        }

        @Override
        public GoogleSpreadsheetConfig getGoogleConfig() {
            return GoogleSpreadsheetConfigManager.getInstance().get("masterclasse");
        }
    },

    SALLE {
        @Override
        public final List<EnumDataColumImport> getDataFileHeader() {
            return Arrays.asList(
                    EnumDataColumImport.S_LOCALISATION,
                    EnumDataColumImport.S_NAME,
                    EnumDataColumImport.S_REPERE,
                    EnumDataColumImport.S_CAPACITY,
                    EnumDataColumImport.S_CRENEAU_1,
                    EnumDataColumImport.S_CRENEAU_2,
                    EnumDataColumImport.S_CRENEAU_3
//                    EnumDataColumImport.S_INFOS_SUPP,
//                    EnumDataColumImport.S_COMMENTAIRES
            );
        }

        @Override
        public final EnumDataColumImport getHeaderId() {
            return EnumDataColumImport.S_NAME;
        }

        @Override
        public final String getTypeName() {
            return "Salle";
        }

        @Override
        public GoogleSpreadsheetConfig getGoogleConfig() {
            return GoogleSpreadsheetConfigManager.getInstance().get("salle");
        }
    },

    PARTICIPANT {
        @Override
        public final List<EnumDataColumImport> getDataFileHeader() {
            return Arrays.asList(
                    EnumDataColumImport.P_PRENOM,
                    EnumDataColumImport.P_NOM,
                    EnumDataColumImport.P_CIVILITE,
                    EnumDataColumImport.P_TARIF,
                    EnumDataColumImport.P_PRIXPAYE,
                    EnumDataColumImport.P_CODEBARRE,
                    EnumDataColumImport.P_DATECOMMANDE,
                    EnumDataColumImport.P_HEURECOMMANDE,
                    EnumDataColumImport.P_NUMERO_BILLET,
                    EnumDataColumImport.P_EMAIL,
                    EnumDataColumImport.P_DATE_NAISSANCE,
                    EnumDataColumImport.P_AGE,
                    EnumDataColumImport.P_TELEPHONE,
                    EnumDataColumImport.P_CODE_POSTAL,
                    EnumDataColumImport.P_PAYS,
                    EnumDataColumImport.P_AUTRES_INFOS,
                    EnumDataColumImport.P_BESOIN_HEBERGEMENT,
                    EnumDataColumImport.P_HEBERGE_AVEC,
                    EnumDataColumImport.P_PEUT_HERBERGER,
                    EnumDataColumImport.P_STATION_METRO,
                    EnumDataColumImport.P_PEUT_ACCUEILLIR,
                    EnumDataColumImport.P_VOEU1,
                    EnumDataColumImport.P_VOEU2,
                    EnumDataColumImport.P_VOEU3,
                    EnumDataColumImport.P_VOEU4,
                    EnumDataColumImport.P_VOEU5,
                    EnumDataColumImport.P_PUPITRE,
                    EnumDataColumImport.P_CHORALE,
                    EnumDataColumImport.P_CHORALE_NN_REF,
                    EnumDataColumImport.P_NE_SOUHAITE_PAS_CHANTER,
                    EnumDataColumImport.P_MESSAGE,
                    EnumDataColumImport.IS_PARTICIPANT_2016,
                    EnumDataColumImport.PAR_PARTICIPANT_2016,
                    EnumDataColumImport.TYPE_SERVICE,
                    EnumDataColumImport.DIOCESE,
                    EnumDataColumImport.CHORALE_1,
                    EnumDataColumImport.MEDIA,
                    EnumDataColumImport.RESEAUX_SOCIAUX,
                    EnumDataColumImport.BOUCHE_OREILLE,
                    EnumDataColumImport.AIDE_OFFICES,
                    EnumDataColumImport.DIOCESE_SNPLS,
                    EnumDataColumImport.P_GROUPE_EVANGELISATION,
                    EnumDataColumImport.P_GROUPE_CONCERT
            );
        }

        @Override
        public final EnumDataColumImport getHeaderId() {
            return EnumDataColumImport.P_EMAIL;
        }

        @Override
        public final String getTypeName() {
            return "Participant";
        }

        @Override
        public GoogleSpreadsheetConfig getGoogleConfig() {
            return GoogleSpreadsheetConfigManager.getInstance().get("participant");
        }
    },

    TARIF {
        @Override
        public final List<EnumDataColumImport> getDataFileHeader() {
            return Arrays.asList(
                    EnumDataColumImport.P_TARIF,
                    EnumDataColumImport.P_PRIXPAYE
            );
        }

        @Override
        public final EnumDataColumImport getHeaderId() {
            return EnumDataColumImport.P_TARIF;
        }

        @Override
        public final String getTypeName() {
            return "Tarif";
        }

        @Override
        public GoogleSpreadsheetConfig getGoogleConfig() {
            return GoogleSpreadsheetConfigManager.getInstance().get("participant");
        }
    },

    CHORALE {
        @Override
        public final List<EnumDataColumImport> getDataFileHeader() {
            return Arrays.asList(
                    EnumDataColumImport.C_NOM,
                    EnumDataColumImport.C_IS_REFERENCE,
                    EnumDataColumImport.C_ID_GROUPE_EVAN
            );
        }

        @Override
        public final EnumDataColumImport getHeaderId() {
            return EnumDataColumImport.C_NOM;
        }

        @Override
        public final String getTypeName() {
            return "Chorale";
        }

        @Override
        public GoogleSpreadsheetConfig getGoogleConfig() {
            return GoogleSpreadsheetConfigManager.getInstance().get("chorale");
        }
    },

    PAYS {
        @Override
        public List<EnumDataColumImport> getDataFileHeader() {
            return Arrays.asList(EnumDataColumImport.P_PAYS);
        }

        @Override
        public EnumDataColumImport getHeaderId() {
            return EnumDataColumImport.P_PAYS;
        }

        @Override
        public String getTypeName() {
            return "Pays";
        }

        @Override
        public GoogleSpreadsheetConfig getGoogleConfig() {
            return GoogleSpreadsheetConfigManager.getInstance().get("participant");
        }
    },

    VOEU {
        @Override
        public final List<EnumDataColumImport> getDataFileHeader() {
            return Arrays.asList(
                    EnumDataColumImport.P_VOEU1,
                    EnumDataColumImport.P_VOEU2,
                    EnumDataColumImport.P_VOEU3,
                    EnumDataColumImport.P_VOEU4
            );
        }

        @Override
        public final EnumDataColumImport getHeaderId() {
            return EnumDataColumImport.P_VOEU1;
        }

        @Override
        public final String getTypeName() {
            return "Voeu";
        }

        @Override
        public GoogleSpreadsheetConfig getGoogleConfig() {
            return GoogleSpreadsheetConfigManager.getInstance().get("participant");
        }
    },

    SOLO_GEOGRAPHIQUE {
        @Override
        public final List<EnumDataColumImport> getDataFileHeader() {
            return Arrays.asList(
                    EnumDataColumImport.GE_ID,
                    EnumDataColumImport.GE_DEPARTEMENT_OR_ARRONDISSEMENT,
                    EnumDataColumImport.GE_LIMITE,
                    EnumDataColumImport.GE_PAYS
            );
        }

        @Override
        public final EnumDataColumImport getHeaderId() {
            return EnumDataColumImport.GE_ID;
        }

        @Override
        public final String getTypeName() {
            return "Solo Géographique";
        }

        @Override
        public GoogleSpreadsheetConfig getGoogleConfig() {
            return GoogleSpreadsheetConfigManager.getInstance().get("solo_geographique");
        }
    },

    GROUPE_CONCERT {
        @Override
        public final List<EnumDataColumImport> getDataFileHeader() {
            if (EnumConfigProperty.IS_COMPUTE_GE_REPART.boolV()) {
                return Arrays.asList(
                        EnumDataColumImport.GC_GROUPE_CONCERT_ID,
                        EnumDataColumImport.GC_GROUPE_EVANGELISATION_ID
                );
            } else {
                return Arrays.asList(
                        EnumDataColumImport.P_GROUPE_CONCERT
                );
            }
        }

        @Override
        public final EnumDataColumImport getHeaderId() {
            if (EnumConfigProperty.IS_COMPUTE_GE_REPART.boolV()) {
                return EnumDataColumImport.GC_GROUPE_CONCERT_ID;
            } else {
                return EnumDataColumImport.P_GROUPE_CONCERT;
            }
        }

        @Override
        public final String getTypeName() {
            return "Groupe de concert";
        }

        @Override
        public GoogleSpreadsheetConfig getGoogleConfig() {
            if (EnumConfigProperty.IS_COMPUTE_GE_REPART.boolV()) {
                return GoogleSpreadsheetConfigManager.getInstance().get("groupe_concert");
            } else {
                return PARTICIPANT.getGoogleConfig();
            }
        }
    };

    EnumDataType() {
    }

    public abstract List<EnumDataColumImport> getDataFileHeader();
    public abstract EnumDataColumImport getHeaderId();
    public abstract String getTypeName();
    public abstract GoogleSpreadsheetConfig getGoogleConfig();
}
