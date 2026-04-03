package com.logistica.doisv.dto.registro_solicitacao;

import com.logistica.doisv.entities.HistoricoSolicitacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Schema(name = "HistoricoSolicitacao", description = "Dados do histórico de atualização de uma solicitação")
public record HistoricoSolicitacaoDTO(
        @Schema(description = "Identificador único do histórico", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Status anterior da solicitação", example = "Pendente", accessMode = Schema.AccessMode.READ_ONLY)
        String statusAnterior,

        @Schema(description = "Novo status da solicitação", example = "Em Análise", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O novo status da solicitação é obrigatório.") String statusNovo,

        @Schema(description = "Data da atualização", example = "01/04/2025 14:30", accessMode = Schema.AccessMode.READ_ONLY)
        String dataAtualizacao,

        @Schema(description = "Observação sobre a atualização", example = "Produto recebido e encaminhado para análise técnica", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "A observação é obrigatória.") String observacao) {

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
