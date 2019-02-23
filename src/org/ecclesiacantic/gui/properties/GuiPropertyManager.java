package org.ecclesiacantic.gui.properties;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GuiPropertyManager {

    static private GuiPropertyManager _instance;

    static public final GuiPropertyManager getInstance() {
        if (_instance == null) {
            _instance = new GuiPropertyManager();
        }
        return _instance;
    }

    private final Set<IPropertyField> _properties;

    private GuiPropertyManager() {
        _properties = new HashSet<>();
    }

    public final void register(final IPropertyField parPropertyField) {
        _properties.add(parPropertyField);
    }

    public final void storeAllProperties() {
        for (final IPropertyField locPropertyField : _properties) {
            locPropertyField.store();
        }
    }
}
