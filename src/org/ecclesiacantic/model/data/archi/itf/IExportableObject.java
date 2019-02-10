package org.ecclesiacantic.model.data.archi.itf;

import java.util.List;

public interface IExportableObject extends INamedObject {

    public List<String> exportDataToCSV();
}
