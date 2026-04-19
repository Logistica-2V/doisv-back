package com.logistica.doisv.configuration;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class GoogleDriveConfig {
    private static final String CAMINHO_TOKENS = "tokens";
    private static final GsonFactory GSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Arrays.asList("https://www.googleapis.com/auth/drive.file");

    private static String clientIdStatic;
    private static String clientSecretStatic;

    private static Drive driveInstance;

    @Value("${google.oauth.client-id}")
    public void setClientId(String clientId) {
        GoogleDriveConfig.clientIdStatic = clientId;
    }

    @Value("${google.oauth.client-secret}")
    public void setClientSecret(String clientSecret) {
        GoogleDriveConfig.clientSecretStatic = clientSecret;
    }

    @PostConstruct
    public void inicializar() {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            GoogleClientSecrets.Details details = new GoogleClientSecrets.Details();
            details.setClientId(clientIdStatic);
            details.setClientSecret(clientSecretStatic);

            GoogleClientSecrets clientSecrets = new GoogleClientSecrets().setWeb(details);

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, GSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(CAMINHO_TOKENS)))
                    .setAccessType("offline")
                    .build();

            LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                    .setHost("localhost")
                    .setPort(63595)
                    .build();

            Credential credencial = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

            driveInstance = new Drive.Builder(HTTP_TRANSPORT, GSON_FACTORY, credencial)
                    .setApplicationName("Google Drive API Java")
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Falha ao inicializar conexão com Google Drive", e);
        }
    }

    public static Drive getDriveService() {
        return driveInstance;
    }
}
