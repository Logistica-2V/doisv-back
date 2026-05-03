package com.logistica.doisv.configuration;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.UserCredentials;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleDriveConfig {
    private static final GsonFactory GSON_FACTORY = GsonFactory.getDefaultInstance();
    private static Drive driveInstance;

    @PostConstruct
    public void inicializar() {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            String clientId = System.getenv("GOOGLE_CLIENT_ID");
            String clientSecret = System.getenv("GOOGLE_CLIENT_SECRET");
            String refreshToken = System.getenv("GOOGLE_REFRESH_TOKEN");

            if (clientId == null || clientSecret == null || refreshToken == null) {
                throw new RuntimeException("Credenciais do Google Drive (Client ID, Secret ou Refresh Token) não configuradas nas Variáveis de Ambiente.");
            }

            UserCredentials credentials = UserCredentials.newBuilder()
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .setRefreshToken(refreshToken)
                    .build();

            driveInstance = new Drive.Builder(HTTP_TRANSPORT, GSON_FACTORY, new HttpCredentialsAdapter(credentials))
                    .setApplicationName("Google Drive API Logistica")
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Falha ao inicializar conexão com Google Drive", e);
        }
    }

    public static Drive getDriveService() {
        return driveInstance;
    }
}