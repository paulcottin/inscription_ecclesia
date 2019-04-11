package org.ecclesiacantic.model.data_manager.bean;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.archi.EnumDataType;
import org.ecclesiacantic.model.data.beans.participant.Tarif;
import org.ecclesiacantic.model.data_manager.ADataManager;
import org.ecclesiacantic.model.data.archi.EnumDataColumImport;
import org.ecclesiacantic.utils.parser.NumberUtils;
import org.ecclesiacantic.utils.parser.helper.exception.ObjectInstanciationException;

import java.util.Map;

public class TarifManager extends ADataManager<Tarif> {

    static private TarifManager _instance;

    static final public TarifManager getInstance() {
        if (_instance == null) {
            _instance = new TarifManager();
        }
        return _instance;
    }

    private TarifManager() {
        super(EnumConfigProperty.INPUT_F_PARTICIPANT, EnumDataType.TARIF, false);
    }

    @Override
    public void preProcessingDataFile() {
        ParticipantManager.getInstance().tempProcessingDataFile(_propertyDataFile);
    }

    @Override
    public void reset() {

    }

    @Override
    protected Tarif convertStringMapToObject(Map<EnumDataColumImport, String> parStringMapHeaderValue) throws ObjectInstanciationException {
        return new Tarif(
                stringV(parStringMapHeaderValue,EnumDataColumImport.P_TARIF),
                NumberUtils.convertFieldToDouble(stringV(parStringMapHeaderValue,EnumDataColumImport.P_PRIXPAYE))
        );
    }
}
