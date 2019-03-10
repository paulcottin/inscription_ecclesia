package org.ecclesiacantic.model.data_manager;

import org.ecclesiacantic.model.data.archi.EnumDataType;

import java.util.HashMap;
import java.util.Map;

public final class AllDataManager {

    static private AllDataManager _instance;

    static public final AllDataManager getInstance() {
        if (_instance == null) {
            _instance = new AllDataManager();
        }
        return _instance;
    }

    private final Map<EnumDataType, ADataManager> _managers;

    private AllDataManager() {
        _managers = new HashMap<>();
    }

    public final void register(final ADataManager parDataManager) {
        _managers.put(parDataManager.getType(), parDataManager);
    }

    public final ADataManager get(final EnumDataType parDataType) {
        return _managers.get(parDataType);
    }
}
