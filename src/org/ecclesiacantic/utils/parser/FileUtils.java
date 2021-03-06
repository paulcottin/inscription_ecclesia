package org.ecclesiacantic.utils.parser;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileUtils {

    static public final String extractStringUtf8(final File parFile) {
        return extractString(parFile, StandardCharsets.UTF_8);
    }

    static public final String extractStringFromExcel(final File parFile) {
        return extractString(parFile, StandardCharsets.ISO_8859_1);
    }

    static public final String extractString(final File parFile, final Charset parCharset) {
        try (final BufferedReader locBf = new BufferedReader(new BufferedReader(new InputStreamReader(new FileInputStream(parFile), parCharset)))) {
            final StringBuilder locTextBuilder = new StringBuilder();
            String locLine = null;
            while ((locLine = locBf.readLine()) != null) {
                locTextBuilder.append(locLine)
                        .append("\n");
            }
            return locTextBuilder.toString();
        } catch(final IOException parE) {
            System.err.println(String.format("Erreur lors de la lecture du fichier %s", parFile));
            parE.printStackTrace();
            return null;
        }
    }

    static public final void write(final File parFile, final String parStringToWrite) {
        try (final BufferedWriter locBf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(parFile), StandardCharsets.ISO_8859_1))) {
            locBf.write(parStringToWrite);
            locBf.flush();
        } catch(final IOException parE) {
            System.err.println(String.format("Erreur lors de l'écriture du fichier %s", parFile));
            parE.printStackTrace();
        }
    }

    static public final void writeCsv(final File parFile, final List<List<String>> parData) {
        try {
            CsvUtils.export(parFile, parData);
        } catch (final IOException parE) {
            parE.printStackTrace();
        }
    }

    static public final boolean isFileExist(final File parFile) {
        return parFile != null && parFile.exists();
    }

    static public final boolean removeFolder(final File parFile) {
        if (!isFileExist(parFile)) {
            return false;
        }
        return removeFolderHelper(parFile) && parFile.delete();
    }

    static private final boolean removeFolderHelper(final File parFile) {
        final File[] locFiles = parFile.listFiles();
        if (locFiles == null) {
            return false;
        }
        boolean locIsOk;
        for (final File locFile : locFiles) {
            if (locFile.isDirectory()) {
                locIsOk = removeFolderHelper(locFile);
            } else {
                locIsOk = locFile.delete();
            }
            if (!locIsOk) {
                return false;
            }
        }
        return true;
    }
}
