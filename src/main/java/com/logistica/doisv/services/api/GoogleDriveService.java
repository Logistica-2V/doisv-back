package com.logistica.doisv.services.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.logistica.doisv.configuration.GoogleDriveConfig;

@Service
public class GoogleDriveService {
    private static String pastaId = null;
   
    public static String salvarArquivoDrive(MultipartFile arquivo, Long idItem, String nomePasta) throws IOException, GeneralSecurityException {
        Drive driveService = GoogleDriveConfig.getDriveService();

        // Verifica se a pasta Loja está criada
        if (pastaId == null) {
            pastaId = obterOuCriarPasta(driveService, nomePasta);
        }

        // Verifica se já existe um arquivo com o id
        String arquivoExistenteId = buscarArquivoPorNome(driveService, idItem.toString());

        // Metadados do arquivo
        File metadadosArquivo = new File();

        ByteArrayInputStream inputStream  = new ByteArrayInputStream(arquivo.getBytes());

        File arquivoSalvo;
        if (arquivoExistenteId != null) {
            // Atualiza arquivo existente
            arquivoSalvo = driveService.files().update(arquivoExistenteId, metadadosArquivo, new com.google.api.client.http.InputStreamContent(
                            arquivo.getContentType(), inputStream))
                    .setFields("id, webViewLink")
                    .execute();
        } else {
            // Cria novo arquivo
            metadadosArquivo.setName(idItem.toString());
            metadadosArquivo.setParents(Collections.singletonList(pastaId));
            arquivoSalvo = driveService.files().create(metadadosArquivo, new com.google.api.client.http.InputStreamContent(
                            arquivo.getContentType(),
                            inputStream
                    ))
                    .setFields("id, webViewLink")
                    .execute();
        }

        // Retorna URL do arquivo salvo
        return arquivoSalvo.getWebViewLink();
    }

    private static String obterOuCriarPasta(Drive driveService, String nomePasta) throws IOException {
        FileList resultado = driveService.files().list()
                .setQ("name='"+ nomePasta +"' and mimeType='application/vnd.google-apps.folder' and trashed=false")
                .setSpaces("drive")
                .execute();

        // Se já existe, retorna o ID
        if (!resultado.getFiles().isEmpty()) {
            return resultado.getFiles().get(0).getId();
        }

        // Se não existe, cria a pasta
        File pasta = new File();
        pasta.setName(nomePasta);
        pasta.setMimeType("application/vnd.google-apps.folder");

        File pastaCriada = driveService.files().create(pasta)
                .setFields("id")
                .execute();

        return pastaCriada.getId();
    }

    private static String buscarArquivoPorNome(Drive driveService, String nomeArquivo) throws IOException {
        FileList resultado = driveService.files().list()
                .setQ("name='" + nomeArquivo + "' and '" + pastaId + "' in parents and trashed=false")
                .setSpaces("drive")
                .execute();

        return resultado.getFiles().isEmpty() ? null : resultado.getFiles().get(0).getId();
    }
}
