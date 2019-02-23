package org.ecclesiacantic.config;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ConfigManager {

    static private ConfigManager _instance;

    static public final ConfigManager getInstance() {
        if (_instance == null) {
            _instance = new ConfigManager();
        }
        return _instance;
    }

    final private Properties _properties;

    private ConfigManager() {
        _properties = new Properties();
        initStandardProperties();
    }

    private final void initStandardProperties() {
        //input file path
        try (final FileReader locConfigReader = new FileReader("config.properties")){
            _properties.load(locConfigReader);
        } catch (final IOException parE) {
            parE.printStackTrace();
        }
    }

    public final void writeStandardProperties() {
        //input file path
        try (final FileWriter locConfigWriter = new FileWriter("config.properties")){
            _properties.store(locConfigWriter, "");
        } catch (final IOException parE) {
            parE.printStackTrace();
        }
    }

    public final String getProperty(final String parPropertyName) {
        if (parPropertyName != null) {
            if (_properties.containsKey(parPropertyName)) {
                return _properties.getProperty(parPropertyName);
            } else {
                System.err.println(String.format("Impossible de trouver la propriété %s", parPropertyName));
                return null;
            }
        } else {
            System.err.println("La clef de la propriété demandée est null");
            return null;
        }
    }

    public final void setProperty(final String parPropertyName, final String parPropertyValue) {
        if (parPropertyName != null) {
            if (_properties.containsKey(parPropertyName)) {
                _properties.setProperty(parPropertyName, parPropertyValue);
            } else {
                System.err.println(String.format("Impossible de trouver la propriété %s", parPropertyName));
            }
        } else {
            System.err.println("La clef de la propriété demandée est null");
        }
    }
}
