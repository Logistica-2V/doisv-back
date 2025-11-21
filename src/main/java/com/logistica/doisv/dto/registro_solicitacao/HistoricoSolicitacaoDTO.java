package com.logistica.doisv.dto.registro_solicitacao;

import com.logistica.doisv.entities.HistoricoSolicitacao;
import jakarta.validation.constraints.NotBlank;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public record HistoricoSolicitacaoDTO(Long id, String statusAnterior, @NotBlank String statusNovo,
                                      String dataAtualizacao, @NotBlank String observacao) {

    public HistoricoSolicitacaoDTO(HistoricoSolicitacao historico){
        this(historico.getId(),
                historico.getStatusAnterior().getStatusSolicitacao(),
                historico.getStatusAtual().getStatusSolicitacao(),
                historico.getDataAtualizacao()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", new Locale("pt", "BR"))),
                historico.getObservacao()
                );
    }
}
