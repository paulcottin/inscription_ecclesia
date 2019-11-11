package org.ecclesiacantic.utils.parser.helper.error_content;

import org.ecclesiacantic.utils.parser.helper.AParseErrorContent;

import java.io.File;

public class FileNotFoundError extends AParseErrorContent {

    private final File _file;

    public FileNotFoundError(final File parFile) {
        _file = parFile;
    }

    @Override
    public String getErrorString() {
        return String.format("Le fichier %s n'a pas été trouvé", _file);
    }
}
