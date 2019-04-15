package org.ecclesiacantic.gui.properties;

import org.ecclesiacantic.config.ConfigManager;
import org.ecclesiacantic.config.OverrideColumnNameManager;

import java.util.*;

public class GuiPropertyManager {

    static private GuiPropertyManager _instance;

    static public final GuiPropertyManager getInstance() {
        if (_instance == null) {
            _instance = new GuiPropertyManager();
        }
        return _instance;
    }

    private final Map<String, IPropertyField> _properties;

    private GuiPropertyManager() {
        _properties = new HashMap<>();
    }

    public final void register(final IPropertyField parPropertyField) {
        _properties.put(parPropertyField.getEnumRefName(), parPropertyField);
    }

    public final void storeAllProperties() {
        for (final IPropertyField locPropertyField : _properties.values()) {
            locPropertyField.store();
        }
        ConfigManager.getInstance().writeStandardProperties();
        OverrideColumnNameManager.getInstance().saveMapping();
    }
}
