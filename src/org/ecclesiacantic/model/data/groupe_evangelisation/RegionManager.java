package org.ecclesiacantic.model.data.groupe_evangelisation;

import org.ecclesiacantic.model.data.beans.participant.Pays;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RegionManager {

    static private RegionManager _instance;

    static public final RegionManager getInstance() {
        if (_instance == null) {
            _instance = new RegionManager();
        }
        return _instance;
    }

    private final Map<String, ARegion> _regions;

    public RegionManager() {
        _regions = new HashMap<>();
    }

    public final ARegion getRegion(final String parCodePostal, final Pays parPays) {
        if (parCodePostal == null) {
            System.err.println("Le code postal est null");
            return null;
        }

        final ARegion locRegion = constructRegion(parCodePostal, parPays);
        final ARegion locReturnRegion = _regions.get(locRegion.getRegionKey());
        if (locReturnRegion != null) {
            return locReturnRegion;
        } else {
            _regions.put(locRegion.getRegionKey(), locRegion);
            return locRegion;
        }
    }

    /**
     * @param parCodePostal
     * @return une région en fonction du pays et du code postal
     */
    private final ARegion constructRegion(final String parCodePostal, final Pays parPays) {
        // Code postal français
        if (parPays.isFrance()) {
            if (parCodePostal.startsWith("75")) {
                //Pour éviter les problèmes 75016 - 75116
                return new FrenchRegion(
                        parPays,
                        String.format("750%s", parCodePostal.substring(3))
                );
            } else {
                final StringBuilder locStringBuilder = new StringBuilder();
                for (int locI = 2; locI < parCodePostal.length(); locI++) {
                    locStringBuilder.append('0');
                }
                return new FrenchRegion(
                        parPays,
                        String.format("%s%s", parCodePostal.substring(0,2), locStringBuilder.toString())
                );
            }
        } else {
            // Code postal étranger
            return new RegionPaysEtranger(
                    parPays,
                    parCodePostal
            );
        }
    }

    public final Collection<ARegion> getAllRegions() {
        return _regions.values();
    }
}
