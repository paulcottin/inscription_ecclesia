package org.ecclesiacantic.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.ecclesiacantic.config.EnumConfigProperty;
import org.ecclesiacantic.utils.parser.CsvUtils;
import org.ecclesiacantic.utils.parser.FileUtils;
import org.ecclesiacantic.utils.parser.NumberUtils;
import org.ecclesiacantic.utils.parser.helper.exception.AParseException;
import org.ecclesiacantic.utils.parser.helper.exception.CsvParseException;
import org.ecclesiacantic.utils.parser.helper.exception.GoogleException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpreadSheetDownloader {
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME =
            "Inscription EC 2017";

    /**
     * Directory to store user credentials for this application.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/sheets.googleapis.com-java-quickstart");

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     * <p>
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/sheets.googleapis.com-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (final Throwable parT) {
            parT.printStackTrace();
            System.exit(1);
        }
    }

    static private SpreadSheetDownloader _instance;

    static public final SpreadSheetDownloader getInstance() {
        if (_instance == null) {
            try {
                _instance = new SpreadSheetDownloader();
            } catch (final IOException parE) {
                System.err.println("Impossible de se connecter à Google pour récupérer les infos");
                parE.printStackTrace();
            }
        }
        return _instance;
    }

    private final File _clientSecret, _exportGoogleFolder;
    private final String _apiUserEmail;
    private final Credential _credentials;
    private final Sheets _sheetService;

    private SpreadSheetDownloader() throws IOException {
        _clientSecret = new File("client_secret.json");
        _apiUserEmail = EnumConfigProperty.API_EMAIL.stringV();
        _exportGoogleFolder = new File(String.format("data_export%s%s%s",
                File.separator, NumberUtils.horodate(), File.separator)
        );
        _exportGoogleFolder.mkdirs();

        _credentials = authorize();
        _sheetService = getSheetsService();
    }

    public final File downloadSheet(final String parTypeName, final GoogleSpreadsheetConfig parConfig) throws AParseException {
        System.out.println(String.format(
                "Téléchargement d'un fichier depuis Google\n\tFeuille %s pour les %s",
                parConfig.getGoogleId(),
                parConfig.getConfigKey()
                )
        );
        final ValueRange locResponse = new ValueRange();
        try {
            locResponse.putAll(
                    _sheetService.spreadsheets().values()
                    .get(parConfig.getGoogleId(), parConfig.getRange())
                    .execute()
            );
        } catch (final IOException parE) {
            throw new GoogleException(parTypeName, parConfig.getGoogleId(), parE);
        }

        final List<List<Object>> locGoogleValues = locResponse.getValues();

        final File locCsvResultFile = new File(String.format("%s%s%s",
                _exportGoogleFolder, File.separator, parConfig.getResultCsvFilename()));
        try {
            if (!FileUtils.isFileExist(_exportGoogleFolder)) {
                _exportGoogleFolder.mkdirs();
            }
            CsvUtils.export(locCsvResultFile, convertObjectToStringList(locGoogleValues));
        } catch (final IOException parE) {
            throw new CsvParseException(parTypeName, parE);
        }
        return locCsvResultFile;
    }

    private final List<List<String>> convertObjectToStringList(final List<List<Object>> parList) {
        final List<List<String>> locReturnList = new ArrayList<>(parList.size());
        for (final List<Object> locList : parList) {
            final List<String> locListString = new ArrayList<>(locList.size());
            for (final Object locObj : locList) {
                locListString.add(locObj.toString());
            }
            locReturnList.add(locListString);
        }
        return locReturnList;
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    private final Credential authorize() throws IOException {
        // Load client secrets.
        final InputStream locInputStream = new FileInputStream(_clientSecret);
        final GoogleClientSecrets locClientSecret = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(locInputStream));

        // Build flow and trigger user authorization request.
        final GoogleAuthorizationCodeFlow locFlow =
                new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, locClientSecret, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        final Credential locCredentials = new AuthorizationCodeInstalledApp(
                locFlow, new LocalServerReceiver()).authorize(_apiUserEmail);
        System.out.println(String.format("Credentials saved to %s", DATA_STORE_DIR.getAbsolutePath()));
        return locCredentials;
    }

    /**
     * Build and return an authorized Sheets API client service.
     *
     * @return an authorized Sheets API client service
     * @throws IOException
     */
    private Sheets getSheetsService() throws IOException {
        authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, _credentials)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}