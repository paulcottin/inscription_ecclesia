package org.ecclesiacantic.utils.parser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.ecclesiacantic.model.data.archi.EnumDataColumImport;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvUtils {

    static public final List<Map<EnumDataColumImport, String>> parseDataFile(final File parDataFile, final List<EnumDataColumImport> parDataHeaders) {
        final List<Map<EnumDataColumImport, String>> locReturnList = new ArrayList<>();
        try (final Reader locReader = new FileReader(parDataFile)) {
            final CSVFormat locCsvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader()
                    .withAllowMissingColumnNames()
                    .withQuote('"')
                    .withIgnoreEmptyLines()
                    .withDelimiter(';')
                    .withQuoteMode(QuoteMode.MINIMAL);
            final Iterable<CSVRecord> locRecords = locCsvFormat.parse(locReader);
            for (final CSVRecord locRecord : locRecords) {
                final Map<EnumDataColumImport, String> locTempMap = new HashMap<>(parDataHeaders.size());
                for (final EnumDataColumImport locHeader : parDataHeaders) {
                    if (locHeader.isActive()) {
                        locTempMap.put(locHeader, locRecord.get(locHeader.getHeaderName()));
                    }
                }
                locReturnList.add(locTempMap);
            }
        } catch (final FileNotFoundException parE) {
            System.err.println(String.format("Impossible de trouver le fichier %s", parDataFile.getAbsolutePath()));
            parE.printStackTrace();
        } catch (final IOException parE) {
            System.err.println(String.format("Erreur lors de la lecture du fichier %s", parDataFile.getAbsolutePath()));
            parE.printStackTrace();
        }
        return locReturnList;
    }

    /**
     * Exporte les données en CSV
     * @param parFile : Le fichier à écrire.
     * @param parLines : Les données : Chaque ligne est une liste de champs
     * @throws IOException
     */
    static public final void export(final File parFile, final List<List<String>> parLines) throws IOException {
        final BufferedWriter locBfWriter = new BufferedWriter(new FileWriter(parFile));
        final CSVFormat locCsvFormat = CSVFormat.EXCEL.withFirstRecordAsHeader()
                .withAllowMissingColumnNames()
                .withIgnoreEmptyLines()
                .withDelimiter(';')
                .withQuoteMode(QuoteMode.MINIMAL);
        final CSVPrinter locPrinter = new CSVPrinter(locBfWriter, locCsvFormat);

        for (final List<String> locDataList : parLines) {
            locPrinter.printRecord(locDataList);
        }
        locPrinter.close();
        locBfWriter.close();
    }

    /**
     * Supprime les lignes vides (type ,,,,,0,,,,0,,,,) du fichier CSV
     * @param parFile le fichier CSV
     */
    static public final void cleanEmptyCsvLines(final File parFile) {
        final String locFileContent = FileUtils.extractString(parFile);

        if (locFileContent.split("\n\"*(;|0)+\n").length < 2) {
            // La chaine n'est pas trouvé, on ne retraite pas le fichier
            return;
        }

        final StringBuilder locNewFileContent = new StringBuilder(locFileContent.length()/2);
        for (final String locLine : locFileContent.split("\n")) {
            if (!locLine.matches("^\"*(;|0)+$")) {
                locNewFileContent.append(locLine)
                        .append("\n");
            }
        }
        FileUtils.write(parFile, locNewFileContent.toString());
    }
}
