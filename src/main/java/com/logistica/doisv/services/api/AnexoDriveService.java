package com.logistica.doisv.services.api;

import com.logistica.doisv.dto.ArquivoDTO;
import com.logistica.doisv.entities.AnexoSolicitacao;
import com.logistica.doisv.entities.Solicitacao;
import com.logistica.doisv.entities.enums.Status;
import com.logistica.doisv.repositories.SolicitacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnexoDriveService {

    private final SolicitacaoRepository solicitacaoRepository;

    @Async
    public void processarUploadAnexos(List<ArquivoDTO> anexos, Solicitacao solicitacao) throws GeneralSecurityException, IOException {
        for (ArquivoDTO dto : anexos) {
            MultipartFile novoArquivo = new MockMultipartFile(dto.nome(), dto.nome(), dto.tipoConteudo(),
                    new ByteArrayInputStream(dto.conteudo()));

            String url = GoogleDriveService.salvarArquivoDrive(novoArquivo, solicitacao.getId() + "_" + anexos.indexOf(dto),
                    solicitacao.getClass().getSimpleName());

            solicitacao.getAnexos().add(AnexoSolicitacao.builder()
                    .urlImagem(url.split("/")[5])
                    .solicitacao(solicitacao)
                    .status(Status.ATIVO)
                    .build());
        }

        solicitacaoRepository.save(solicitacao);
    }
}
