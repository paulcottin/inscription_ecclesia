package org.ecclesiacantic.model.data_manager.bean;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.archi.EnumDataType;
import org.ecclesiacantic.model.data.beans.participant.Pays;
import org.ecclesiacantic.model.data_manager.ADataManager;
import org.ecclesiacantic.model.data.archi.EnumDataColumImport;

import java.util.Map;

public class PaysManager extends ADataManager<Pays> {

    static private PaysManager _instance;

    static public final PaysManager getInstance(){
        if (_instance == null) {
            _instance = new PaysManager();
        }
        return _instance;
    }

    public PaysManager() {
        super(EnumConfigProperty.INPUT_F_PARTICIPANT, EnumDataType.PAYS, false);
    }

    @Override
    public void reset() {

    }

    @Override
    protected Pays convertStringMapToObject(Map<EnumDataColumImport, String> parStringMapHeaderValue) {
        return new Pays(parStringMapHeaderValue.get(EnumDataColumImport.P_PAYS));
    }
}
