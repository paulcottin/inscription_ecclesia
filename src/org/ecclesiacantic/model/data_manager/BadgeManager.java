package org.ecclesiacantic.model.data_manager;

import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.model.data.Badge;
import org.ecclesiacantic.model.data.beans.participant.Participant;
import org.ecclesiacantic.model.data_manager.bean.ParticipantManager;
import org.ecclesiacantic.model.data.archi.EnumDataColumnExportList;
import org.ecclesiacantic.utils.parser.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BadgeManager implements IExportableManager {

    static private BadgeManager _instance;

    static public final BadgeManager getInstance() {
        if (_instance == null) {
            _instance = new BadgeManager();
        }
        return _instance;
    }

    private final List<Badge> _badges;

    private BadgeManager() {
        _badges = new ArrayList<>();
    }

    public final void generateBadges() {
        init();
        for (final Participant locParticipant : ParticipantManager.getInstance().getAllData()) {
            _badges.add(locParticipant.getBadge());
        }
    }

    public final void init() {
        _badges.clear();
    }

    @Override
    public final void exportDataToCSV() {
        exportDataToCSV(false);
    }

    public final List<List<String>> exportDataToCSV(final boolean parWriteFile) {
        final List<List<String>> locExportBadges = new ArrayList<>(_badges.size() + 1);
        locExportBadges.addAll(Arrays.asList(EnumDataColumnExportList.BADGES.asListString()));
        for (final Badge locBadge : _badges) {
            locExportBadges.add(locBadge.exportToCSV());
        }

        FileUtils.writeCsv(EnumConfigProperty.OUTPUT_F_BADGE.fileV(), locExportBadges);

        return locExportBadges;
    }

    public final List<Badge> getBadges() {
        return _badges;
    }
}
