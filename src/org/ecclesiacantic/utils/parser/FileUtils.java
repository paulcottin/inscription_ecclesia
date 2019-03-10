package org.ecclesiacantic.utils.parser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileUtils {

    static public final String extractString(final File parFile) {
        try (final BufferedReader locBf = new BufferedReader(new BufferedReader(new InputStreamReader(new FileInputStream(parFile), StandardCharsets.UTF_8)))) {
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
        try (final BufferedWriter locBf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(parFile), StandardCharsets.UTF_8))) {
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
}
