package org.ecclesiacantic.model.data.archi;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.google.GoogleSpreadsheetConfig;
import org.ecclesiacantic.google.GoogleSpreadsheetConfigManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum EnumDataType {

    MASTERCLASS {
        @Override
        public EnumDataColumnImportList getColumnImportList() {
            return EnumDataColumnImportList.MASTERCLASS;
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
        public EnumDataColumnImportList getColumnImportList() {
            return EnumDataColumnImportList.SALLE;
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
        public EnumDataColumnImportList getColumnImportList() {
            return EnumDataColumnImportList.PARTICIPANT;
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

    CHORALE {

        @Override
        public EnumDataColumnImportList getColumnImportList() {
            return EnumDataColumnImportList.CHORALE;
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
        public List<EnumDataColumImport> getFilteredColumns() {
            return Collections.singletonList(EnumDataColumImport.P_PAYS);
        }

        @Override
        public EnumDataColumnImportList getColumnImportList() {
            return null;
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
            return PARTICIPANT.getGoogleConfig();
        }
    },

    VOEU {
        @Override
        public final List<EnumDataColumImport> getFilteredColumns() {
            return Arrays.asList(
                    EnumDataColumImport.P_VOEU1,
                    EnumDataColumImport.P_VOEU2,
                    EnumDataColumImport.P_VOEU3,
                    EnumDataColumImport.P_VOEU4
            );
        }

        @Override
        public EnumDataColumnImportList getColumnImportList() {
            return null;
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
            return PARTICIPANT.getGoogleConfig();
        }
    },

    SOLO_GEOGRAPHIQUE {
        @Override
        public final List<EnumDataColumImport> getFilteredColumns() {
            return Arrays.asList(
                    EnumDataColumImport.GE_ID,
                    EnumDataColumImport.GE_DEPARTEMENT_OR_ARRONDISSEMENT,
                    EnumDataColumImport.GE_LIMITE,
                    EnumDataColumImport.GE_PAYS
            );
        }

        @Override
        public EnumDataColumnImportList getColumnImportList() {
            return EnumDataColumnImportList.SOLOS_GEOGRAPHIQUES;
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
        public final List<EnumDataColumImport> getFilteredColumns() {
            if (!EnumConfigProperty.IS_COMPUTE_GE_REPART.boolV()) {
                return Collections.singletonList(EnumDataColumImport.P_GROUPE_CONCERT);
            }
            return super.getFilteredColumns();
        }

        @Override
        public EnumDataColumnImportList getColumnImportList() {
            if (EnumConfigProperty.IS_COMPUTE_GE_REPART.boolV()) {
                return EnumDataColumnImportList.GROUPE_CONCERT;
            }
            return null;
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

    public abstract EnumDataColumnImportList getColumnImportList();
    public abstract EnumDataColumImport getHeaderId();
    public abstract String getTypeName();
    public abstract GoogleSpreadsheetConfig getGoogleConfig();

    public List<EnumDataColumImport> getFilteredColumns() {
        return Collections.emptyList();
    }

    public final List<EnumDataColumImport> getDataFileHeader() {
        final List<EnumDataColumImport> locList = new ArrayList<>();
        final EnumDataColumnImportList locColumnsList = getColumnImportList();
        if (locColumnsList != null) {
            locList.addAll(locColumnsList.getColumns());
        }
        locList.addAll(getFilteredColumns());
        return locList;
    }
}
